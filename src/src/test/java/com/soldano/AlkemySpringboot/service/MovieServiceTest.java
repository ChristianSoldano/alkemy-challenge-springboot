package com.soldano.AlkemySpringboot.service;

import com.soldano.AlkemySpringboot.dto.movie.MovieDto;
import com.soldano.AlkemySpringboot.dto.movie.SimpleMovieDto;
import com.soldano.AlkemySpringboot.mapper.ClassMapper;
import com.soldano.AlkemySpringboot.model.Movie;
import com.soldano.AlkemySpringboot.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ClassMapper classMapper;

    @InjectMocks
    private MovieService movieService;

    private List<Movie> movieList;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);
        this.movieList = Arrays.asList(
                new Movie(1, "Movie1", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "Image1", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new Movie(1, "Movie2", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-13"), "Image2", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new Movie(1, "Movie3", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-14"), "Image3", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>())
        );
    }

    @Test
    void getAllMovies() throws ParseException {
        when(movieRepository.findAll()).thenReturn(movieList);
        List<SimpleMovieDto> movieDtoList = Arrays.asList(
                new SimpleMovieDto("Movie1", "Image1", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12")),
                new SimpleMovieDto("Movie2", "Image2", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-13")),
                new SimpleMovieDto("Movie3", "Image3", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-14"))
        );
        when(classMapper.movieListToSimpleMovieDtoList(movieList)).thenReturn(movieDtoList);
        assertEquals(movieDtoList.size(), movieService.getAllMovies().size());
        assertEquals(movieDtoList, movieService.getAllMovies());
    }

    @Test
    void getMoviesByParamsWithoutOrder() throws ParseException {
        Map<String, String> params = Map.of("title", "Movie1", "genre", "Genre1");
        when(movieRepository.getMovieByParamsASC("Movie1", "Genre1")).thenReturn(movieList);
        List<MovieDto> movieDtoList = Arrays.asList(
                new MovieDto(1, "Movie1", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "Image1", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie2", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-13"), "Image2", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie3", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-14"), "Image3", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>())
        );
        when(classMapper.movieListToMovieDtoList(movieList)).thenReturn(movieDtoList);

        assertEquals(movieDtoList.size(), movieService.getMoviesByParams(params).size());
        assertEquals(movieDtoList, movieService.getMoviesByParams(params));
    }

    @Test
    void getMoviesByParamsOrderDesc() throws ParseException {
        Map<String, String> params = Map.of("title", "Movie1", "genre", "Genre1", "order", "DESC");
        when(movieRepository.getMovieByParamsDESC("Movie1", "Genre1")).thenReturn(movieList);
        List<MovieDto> movieDtoList = Arrays.asList(
                new MovieDto(1, "Movie1", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "Image1", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie2", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-13"), "Image2", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie3", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-14"), "Image3", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>())
        );
        when(classMapper.movieListToMovieDtoList(movieList)).thenReturn(movieDtoList);

        assertEquals(movieDtoList.size(), movieService.getMoviesByParams(params).size());
        assertEquals(movieDtoList, movieService.getMoviesByParams(params));
    }

    @Test
    void getMoviesByParamsOrderAsc() throws ParseException {
        Map<String, String> params = Map.of("title", "Movie1", "genre", "Genre1", "order", "ASC");
        when(movieRepository.getMovieByParamsASC("Movie1", "Genre1")).thenReturn(movieList);
        List<MovieDto> movieDtoList = Arrays.asList(
                new MovieDto(1, "Movie1", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "Image1", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie2", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-13"), "Image2", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie3", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-14"), "Image3", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>())
        );
        when(classMapper.movieListToMovieDtoList(movieList)).thenReturn(movieDtoList);

        assertEquals(movieDtoList.size(), movieService.getMoviesByParams(params).size());
        assertEquals(movieDtoList, movieService.getMoviesByParams(params));
    }
}