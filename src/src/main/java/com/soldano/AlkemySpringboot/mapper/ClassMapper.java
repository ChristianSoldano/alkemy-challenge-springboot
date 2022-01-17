package com.soldano.AlkemySpringboot.mapper;

import com.soldano.AlkemySpringboot.dto.character.CharacterDto;
import com.soldano.AlkemySpringboot.dto.character.CreateCharacterDto;
import com.soldano.AlkemySpringboot.dto.character.SimpleCharacterDto;
import com.soldano.AlkemySpringboot.dto.character.UpdateCharacterDto;
import com.soldano.AlkemySpringboot.dto.genre.CreateGenreDto;
import com.soldano.AlkemySpringboot.dto.genre.GenreDto;
import com.soldano.AlkemySpringboot.dto.genre.SimpleGenreDto;
import com.soldano.AlkemySpringboot.dto.genre.UpdateGenreDto;
import com.soldano.AlkemySpringboot.dto.movie.CreateMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.MovieDto;
import com.soldano.AlkemySpringboot.dto.movie.SimpleMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.UpdateMovieDto;
import com.soldano.AlkemySpringboot.dto.user.UserDto;
import com.soldano.AlkemySpringboot.model.Character;
import com.soldano.AlkemySpringboot.model.Genre;
import com.soldano.AlkemySpringboot.model.Movie;
import com.soldano.AlkemySpringboot.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    //USERS
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto user);

    //CHARACTERS
    CharacterDto characterToCharacterDto(Character character);
    List<CharacterDto> characterListToCharacterDtoList(List<Character> character);

    SimpleCharacterDto characterToSimpleCharacterDto(Character character);
    List<SimpleCharacterDto> characterListToSimpleCharacterDtoList(List<Character> character);

    @Mapping(target = "movies", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCharacterToCharacter(UpdateCharacterDto dto, @MappingTarget Character character);

    @Mapping(target = "movies", ignore = true)
    Character createCharacterDtoToCharacter(CreateCharacterDto dto);

    //MOVIES
    MovieDto movieToMovieDto(Movie movie);
    List<MovieDto> movieListToMovieDtoList(List<Movie> movie);

    SimpleMovieDto movieToSimpleMovieDto(Movie movie);
    List<SimpleMovieDto> movieListToSimpleMovieDtoList(List<Movie> movies);

    @Mapping(target = "genres", ignore = true)
    Movie createMovieDtoToMovie(CreateMovieDto dto);

    @Mapping(target = "genres", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieDtoToMovie(UpdateMovieDto dto, @MappingTarget Movie movie);

    //GENRES

    GenreDto genreToGenreDto(Genre genre);
    Genre createGenreDtoToGenre(CreateGenreDto genre);
    List<GenreDto> genreListToGenreDtoList(List<Genre> genres);

    SimpleGenreDto genreToSimpleGenreDto(Genre genre);
    List<SimpleGenreDto> genreListToSimpleGenreDtoList(List<Genre> genres);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGenreDtoToGenre(UpdateGenreDto dto, @MappingTarget Genre genre);

}
