package com.soldano.AlkemySpringboot.service;

import com.soldano.AlkemySpringboot.dto.movie.CreateMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.MovieDto;
import com.soldano.AlkemySpringboot.dto.movie.SimpleMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.UpdateMovieDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.mapper.ClassMapper;
import com.soldano.AlkemySpringboot.model.Genre;
import com.soldano.AlkemySpringboot.model.Movie;
import com.soldano.AlkemySpringboot.model.MovieGenre;
import com.soldano.AlkemySpringboot.repository.MovieGenreRepository;
import com.soldano.AlkemySpringboot.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final ClassMapper classMapper;
    private final MovieGenreRepository movieGenreRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreService genreService, ClassMapper classMapper, MovieGenreRepository movieGenreRepository) {
        this.movieRepository = movieRepository;
        this.genreService = genreService;
        this.classMapper = classMapper;
        this.movieGenreRepository = movieGenreRepository;
    }

    public List<SimpleMovieDto> getAllMovies() {
        return classMapper.movieListToSimpleMovieDtoList(movieRepository.findAll());
    }

    public List<MovieDto> getMoviesByParams(Map<String, String> params) {
        if (params.containsKey("order")) {
            if (params.get("order").equalsIgnoreCase("DESC")) {
                return classMapper.movieListToMovieDtoList(movieRepository.getMovieByParamsDESC(params.get("title"), params.get("genre")));
            } else {
                return classMapper.movieListToMovieDtoList(movieRepository.getMovieByParamsASC(params.get("title"), params.get("genre")));
            }
        } else {
            return classMapper.movieListToMovieDtoList(movieRepository.getMovieByParamsASC(params.get("title"), params.get("genre")));
        }
    }

    public MovieDto getMovieById(Integer id) {
        return classMapper.movieToMovieDto(movieRepository.findById(id).orElse(null));
    }

    public MovieDto addMovie(CreateMovieDto movie) throws UniqueException, EntityNotFoundException {

        if (movieRepository.existsByTitle(movie.getTitle()))
            throw new UniqueException("title", movie.getTitle());

        List<Genre> genreList = genreService.getByIdList(movie.getGenres());

        if (movie.getGenres().size() != genreList.size()) {
            movie.getGenres().removeAll(genreList.stream().map(Genre::getId).collect(Collectors.toList()));
            throw new EntityNotFoundException("Genre id " + movie.getGenres().toString());
        }

        Movie newMovie = classMapper.createMovieDtoToMovie(movie);
        newMovie.setCreatedAt(new Date());
        newMovie.setUpdatedAt(new Date());
        newMovie = movieRepository.save(newMovie);

        for (Integer id : movie.getGenres()) {
            movieGenreRepository.save(MovieGenre.builder().movieId(newMovie.getId()).genreId(id).build());
        }

        newMovie.setGenres(genreList);
        newMovie.setCharacters(new ArrayList<>());
        return classMapper.movieToMovieDto(newMovie);
    }

    public MovieDto updateMovie(Integer id, UpdateMovieDto movieDto) throws UniqueException, EntityNotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie"));

        if (movieRepository.existsByTitle(movieDto.getTitle()) && !movie.getTitle().equals(movieDto.getTitle()))
            throw new UniqueException("title", movieDto.getTitle());

        List<Genre> genreList = genreService.getByIdList(movieDto.getGenres());

        if (movieDto.getGenres().size() != genreList.size()) {
            movieDto.getGenres().removeAll(genreList.stream().map(Genre::getId).collect(Collectors.toList()));
            throw new EntityNotFoundException("Genre id " + movieDto.getGenres().toString());
        }

        classMapper.updateMovieDtoToMovie(movieDto, movie);
        movie.setUpdatedAt(new Date());
        Movie savedMovie = movieRepository.save(movie);

        movieGenreRepository.deleteAll(movieGenreRepository.findIdsByMovieId(movie.getId()));

        for (Integer genreId : movieDto.getGenres()) {
            movieGenreRepository.save(MovieGenre.builder().movieId(savedMovie.getId()).genreId(genreId).build());
        }
        savedMovie.setGenres(genreList);
        return classMapper.movieToMovieDto(savedMovie);
    }

    public void deleteMovie(Integer id) throws EntityNotFoundException {
        if (!movieRepository.existsById(id))
            throw new EntityNotFoundException("Movie");
        movieRepository.deleteById(id);
    }

    public List<Movie> getByIdList(List<Integer> ids) {
        return movieRepository.findMany(ids);
    }
}
