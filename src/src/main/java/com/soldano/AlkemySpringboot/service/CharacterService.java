package com.soldano.AlkemySpringboot.service;

import com.soldano.AlkemySpringboot.dto.character.CharacterDto;
import com.soldano.AlkemySpringboot.dto.character.CreateCharacterDto;
import com.soldano.AlkemySpringboot.dto.character.SimpleCharacterDto;
import com.soldano.AlkemySpringboot.dto.character.UpdateCharacterDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.mapper.ClassMapper;
import com.soldano.AlkemySpringboot.model.Character;
import com.soldano.AlkemySpringboot.model.Movie;
import com.soldano.AlkemySpringboot.model.MovieCharacter;
import com.soldano.AlkemySpringboot.repository.CharacterRepository;
import com.soldano.AlkemySpringboot.repository.MovieCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final ClassMapper classMapper;
    private final MovieService movieService;
    private final MovieCharacterRepository movieCharacterRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository, ClassMapper classMapper, MovieService movieService, MovieCharacterRepository movieCharacterRepository) {
        this.characterRepository = characterRepository;
        this.classMapper = classMapper;
        this.movieService = movieService;
        this.movieCharacterRepository = movieCharacterRepository;
    }

    public List<SimpleCharacterDto> getAllCharacters() {
        return classMapper.characterListToSimpleCharacterDtoList(characterRepository.findAll());
    }

    public List<CharacterDto> getCharactersByParams(Map<String, String> params) {
        return classMapper.characterListToCharacterDtoList(characterRepository.getCharactersByParams(
                params.get("name"),
                params.get("age"),
                params.get("movie")
        ));
    }

    public CharacterDto getCharacterById(Integer id) {
        return classMapper.characterToCharacterDto(characterRepository.findById(id).orElse(null));
    }

    public CharacterDto addCharacter(CreateCharacterDto character) throws UniqueException, EntityNotFoundException {
        if (characterRepository.existsByName(character.getName()))
            throw new UniqueException("name", character.getName());

        List<Movie> movieList = movieService.getByIdList(character.getMovies());

        if (character.getMovies().size() != movieList.size()) {
            character.getMovies().removeAll(movieList.stream().map(Movie::getId).collect(Collectors.toList()));
            throw new EntityNotFoundException("Movie id " + character.getMovies().toString());
        }

        Character newCharacter = classMapper.createCharacterDtoToCharacter(character);
        newCharacter.setCreatedAt(new Date());
        newCharacter.setUpdatedAt(new Date());
        newCharacter = characterRepository.save(newCharacter);

        for (Integer id : character.getMovies()) {
            movieCharacterRepository.save(MovieCharacter.builder().characterId(newCharacter.getId()).movieId(id).build());
        }

        newCharacter.setMovies(movieList);

        return classMapper.characterToCharacterDto(newCharacter);
    }

    public CharacterDto updateCharacter(Integer id, UpdateCharacterDto characterDto) throws EntityNotFoundException, UniqueException {
        Character character = characterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Character"));

        if (characterRepository.existsByName(characterDto.getName()) && !character.getName().equals(characterDto.getName()))
            throw new UniqueException("name", characterDto.getName());

        List<Movie> movieList = movieService.getByIdList(characterDto.getMovies());

        if (characterDto.getMovies().size() != movieList.size()) {
            characterDto.getMovies().removeAll(movieList.stream().map(Movie::getId).collect(Collectors.toList()));
            throw new EntityNotFoundException("Movie id " + characterDto.getMovies().toString());
        }

        classMapper.updateCharacterToCharacter(characterDto, character);
        character.setUpdatedAt(new Date());
        Character savedCharacter = characterRepository.save(character);

        movieCharacterRepository.deleteAll(movieCharacterRepository.findIdsByCharacterId(character.getId()));

        for (Integer movieId : characterDto.getMovies()) {
            movieCharacterRepository.save(MovieCharacter.builder().characterId(savedCharacter.getId()).movieId(movieId).build());
        }
        savedCharacter.setMovies(movieList);
        return classMapper.characterToCharacterDto(savedCharacter);
    }

    public void deleteCharacter(Integer id) throws EntityNotFoundException {
        if (!characterRepository.existsById(id))
            throw new EntityNotFoundException("Character");

        characterRepository.deleteById(id);
    }
}