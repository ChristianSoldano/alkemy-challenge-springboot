package com.soldano.AlkemySpringboot.controller;

import com.soldano.AlkemySpringboot.dto.movie.CreateMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.MovieDto;
import com.soldano.AlkemySpringboot.dto.movie.SimpleMovieDto;
import com.soldano.AlkemySpringboot.dto.movie.UpdateMovieDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.service.MovieService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies")
@SecurityRequirement(name = "Authorization")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllMovies(@RequestParam(required = false) Map<String, String> reqParam) {
        if (reqParam.containsKey("title") || reqParam.containsKey("order") || reqParam.containsKey("genre")) {
            List<MovieDto> movies = movieService.getMoviesByParams(reqParam);
            if (movies.isEmpty())
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.ok(movies);
        } else {
            List<SimpleMovieDto> movies = movieService.getAllMovies();
            if (!movies.isEmpty())
                return ResponseEntity.ok(movies);
            else
                return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer id) {
        MovieDto movie = movieService.getMovieById(id);

        if (movie == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(movie);
    }

    @PostMapping("")
    public ResponseEntity<MovieDto> createMovie(@RequestBody @Valid CreateMovieDto movie) throws UniqueException, EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.addMovie(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Integer id, @RequestBody @Valid UpdateMovieDto movieDto) throws UniqueException, EntityNotFoundException {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteMovie(@PathVariable Integer id) throws EntityNotFoundException {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }
}
