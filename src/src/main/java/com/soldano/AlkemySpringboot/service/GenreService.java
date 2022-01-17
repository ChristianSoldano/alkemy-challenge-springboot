package com.soldano.AlkemySpringboot.service;

import com.soldano.AlkemySpringboot.dto.genre.CreateGenreDto;
import com.soldano.AlkemySpringboot.dto.genre.GenreDto;
import com.soldano.AlkemySpringboot.dto.genre.SimpleGenreDto;
import com.soldano.AlkemySpringboot.dto.genre.UpdateGenreDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.mapper.ClassMapper;
import com.soldano.AlkemySpringboot.model.Genre;
import com.soldano.AlkemySpringboot.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final ClassMapper classMapper;

    @Autowired
    public GenreService(GenreRepository genreRepository, ClassMapper classMapper) {
        this.genreRepository = genreRepository;
        this.classMapper = classMapper;
    }

    public List<SimpleGenreDto> getAllGenres() {
        return classMapper.genreListToSimpleGenreDtoList(genreRepository.findAll());
    }

    public GenreDto getGenreById(Integer id) {
        return classMapper.genreToGenreDto(genreRepository.findById(id).orElse(null));
    }

    public List<Genre> getByIdList(List<Integer> ids) {
        return genreRepository.findMany(ids);
    }

    public GenreDto createGenre(CreateGenreDto genre) throws UniqueException {
        if (genreRepository.existsByName(genre.getName()))
            throw new UniqueException("name", genre.getName());
        Genre newGenre = classMapper.createGenreDtoToGenre(genre);

        newGenre.setCreatedAt(new Date());
        newGenre.setUpdatedAt(new Date());
        newGenre.setMovies(new ArrayList<>());
        return classMapper.genreToGenreDto(genreRepository.save(newGenre));
    }

    public GenreDto updateGenre(Integer id, UpdateGenreDto genreDto) throws UniqueException, EntityNotFoundException {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre"));

        if (genreRepository.existsByName(genreDto.getName()) && !genre.getName().equals(genreDto.getName()))
            throw new UniqueException("name", genreDto.getName());

        classMapper.updateGenreDtoToGenre(genreDto, genre);
        genre.setUpdatedAt(new Date());

        return classMapper.genreToGenreDto(genreRepository.save(genre));
    }

    public void deleteGenre(Integer id) throws EntityNotFoundException {
        if (!genreRepository.existsById(id))
            throw new EntityNotFoundException("Genre");
        genreRepository.deleteById(id);
    }

}
