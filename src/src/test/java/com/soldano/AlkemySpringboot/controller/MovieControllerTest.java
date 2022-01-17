package com.soldano.AlkemySpringboot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soldano.AlkemySpringboot.dto.movie.CreateMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.MovieDto;
import com.soldano.AlkemySpringboot.dto.movie.SimpleMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.UpdateMovieDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private List<MovieDto> movieList;
    private MockMvc mvc;
    private JacksonTester<CreateMovieDto> jsonCreateMovie;
    private JacksonTester<UpdateMovieDto> jsonUpdateMovie;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(movieController)
                .setControllerAdvice(new ControllerAdvice())
                .build();

        this.movieList = Arrays.asList(
                new MovieDto(1, "Movie1", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "Image1", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie2", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-13"), "Image2", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>()),
                new MovieDto(1, "Movie3", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-14"), "Image3", 1, new Date(), new Date(), new ArrayList<>(), new ArrayList<>())
        );
    }

    @Test
    void getAllMoviesWithoutParams() throws ParseException {
        List<SimpleMovieDto> simpleMovieDtos = Arrays.asList(
                new SimpleMovieDto("Movie1", "Image1", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12")),
                new SimpleMovieDto("Movie2", "Image2", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-13")),
                new SimpleMovieDto("Movie3", "Image3", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-14"))
        );
        when(movieService.getAllMovies()).thenReturn(simpleMovieDtos);
        ResponseEntity<List<SimpleMovieDto>> response = new ResponseEntity<>(simpleMovieDtos, HttpStatus.OK);
        assertEquals(response, movieController.getAllMovies(Collections.emptyMap()));

    }

    @Test
    void getAllMoviesWithoutParamsNotFound() {
        when(movieService.getAllMovies()).thenReturn(Collections.emptyList());
        ResponseEntity<List<SimpleMovieDto>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertEquals(response, movieController.getAllMovies(Collections.emptyMap()));
    }

    @Test
    void getAllMoviesWithParams() {
        Map<String, String> reqParam = Map.of("title", "Movie1", "genre", "Genre1", "order", "DESC");
        when(movieService.getMoviesByParams(reqParam)).thenReturn(movieList);
        ResponseEntity<List<MovieDto>> response = new ResponseEntity<>(movieList, HttpStatus.OK);
        assertEquals(response, movieController.getAllMovies(reqParam));
    }

    @Test
    void getAllMoviesWithParamsNotFound() {
        Map<String, String> reqParam = Map.of("title", "Movie1", "genre", "Genre1", "order", "DESC");
        when(movieService.getMoviesByParams(reqParam)).thenReturn(Collections.emptyList());
        ResponseEntity<List<MovieDto>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertEquals(response, movieController.getAllMovies(reqParam));
    }

    @Test
    void getMovieById() {
        MovieDto movie = movieList.get(1);
        when(movieService.getMovieById(1)).thenReturn(movie);
        ResponseEntity<MovieDto> response = new ResponseEntity<>(movie, HttpStatus.OK);
        assertEquals(response, movieController.getMovieById(1));
    }

    @Test
    void getMovieByIdNotFound() {
        when(movieService.getMovieById(1)).thenReturn(null);
        ResponseEntity<MovieDto> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertEquals(response, movieController.getMovieById(1));
    }

    @Test
    void createMovie() throws UniqueException, EntityNotFoundException, ParseException {
        CreateMovieDto createMovieDto = new CreateMovieDto("Title", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "image", 1, Arrays.asList(1, 2, 3));
        when(movieService.addMovie(createMovieDto)).thenReturn(movieList.get(1));
        ResponseEntity<MovieDto> response = new ResponseEntity<>(movieList.get(1), HttpStatus.CREATED);
        assertEquals(response, movieController.createMovie(createMovieDto));
    }

    @Test
    void createMovieEntityNotFoundException() throws Exception {
        CreateMovieDto createMovieDto = new CreateMovieDto("Title", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "image", 1, Arrays.asList(1, 2, 3));
        when(movieService.addMovie(any())).thenThrow(new EntityNotFoundException("Genre"));

        MockHttpServletResponse response = mvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateMovie.write(createMovieDto).getJson()))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getContentAsString());

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("3", jsonResponse.get("code").toString());
        assertEquals("\"Genre not found\"", jsonResponse.get("message").toString());
    }

    @Test
    void createMovieUniqueException() throws Exception {
        CreateMovieDto createMovieDto = new CreateMovieDto("Title", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "image", 1, Arrays.asList(1, 2, 3));
        when(movieService.addMovie(any())).thenThrow(new UniqueException("title", "Title"));

        MockHttpServletResponse response = mvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateMovie.write(createMovieDto).getJson()))
                .andReturn().getResponse();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getContentAsString());

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertEquals("2", jsonResponse.get("code").toString());
        assertEquals("\"Duplicate entry 'Title' for key title\"", jsonResponse.get("message").toString());
    }

    @Test
    void updateMovie() throws ParseException, UniqueException, EntityNotFoundException {
        UpdateMovieDto updateMovieDto = new UpdateMovieDto("Title", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "image", 1, Arrays.asList(1, 2, 3));
        when(movieService.updateMovie(1, updateMovieDto)).thenReturn(movieList.get(1));
        ResponseEntity<MovieDto> response = new ResponseEntity<>(movieList.get(1), HttpStatus.OK);
        assertEquals(response, movieController.updateMovie(1, updateMovieDto));
    }

    @Test
    void updateMovieUniqueException() throws Exception {
        UpdateMovieDto updateMovieDto = new UpdateMovieDto("Title", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "image", 1, Arrays.asList(1, 2, 3));
        when(movieService.updateMovie(any(), any())).thenThrow(new UniqueException("title", "Title"));

        MockHttpServletResponse response = mvc.perform(put("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateMovie.write(updateMovieDto).getJson()))
                .andReturn().getResponse();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getContentAsString());

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertEquals("2", jsonResponse.get("code").toString());
        assertEquals("\"Duplicate entry 'Title' for key title\"", jsonResponse.get("message").toString());

    }

    @Test
    void updateMovieEntityNotFoundException() throws Exception {
        UpdateMovieDto updateMovieDto = new UpdateMovieDto("Title", new SimpleDateFormat("yyyy-MM-dd").parse("1996-12-12"), "image", 1, Arrays.asList(1, 2, 3));
        when(movieService.updateMovie(any(), any())).thenThrow(new EntityNotFoundException("Genre"));

        MockHttpServletResponse response = mvc.perform(put("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateMovie.write(updateMovieDto).getJson()))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getContentAsString());

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("3", jsonResponse.get("code").toString());
        assertEquals("\"Genre not found\"", jsonResponse.get("message").toString());
    }

    @Test
    void deleteMovie() throws EntityNotFoundException {
        doNothing().when(movieService).deleteMovie(1);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        assertEquals(response, movieController.deleteMovie(1));
    }

    @Test
    void deleteMovieEntityNotFoundException() throws Exception {
        doThrow(new EntityNotFoundException("Movie")).when(movieService).deleteMovie(1);

        MockHttpServletResponse response = mvc.perform(delete("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.getContentAsString());

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("3", jsonResponse.get("code").toString());
        assertEquals("\"Movie not found\"", jsonResponse.get("message").toString());
    }
}