package com.soldano.AlkemySpringboot.controller;

import com.soldano.AlkemySpringboot.dto.genre.CreateGenreDto;
import com.soldano.AlkemySpringboot.dto.genre.GenreDto;
import com.soldano.AlkemySpringboot.dto.genre.SimpleGenreDto;
import com.soldano.AlkemySpringboot.dto.genre.UpdateGenreDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.service.GenreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/genres")
@Tag(name = "Genres")
@SecurityRequirement(name = "Authorization")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("")
    public ResponseEntity<List<SimpleGenreDto>> getAllGenres() {
        List<SimpleGenreDto> genres = genreService.getAllGenres();

        if (genres.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Integer id) {
        GenreDto genre = genreService.getGenreById(id);

        if (genre == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(genre);
    }

    @PostMapping("")
    public ResponseEntity<GenreDto> createGenre(@RequestBody @Valid CreateGenreDto genre) throws UniqueException {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.createGenre(genre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable Integer id, @RequestBody @Valid UpdateGenreDto genreDto) throws UniqueException, EntityNotFoundException {
        return ResponseEntity.ok(genreService.updateGenre(id, genreDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Integer id) throws EntityNotFoundException {
        genreService.deleteGenre(id);
        return ResponseEntity.ok().build();
    }
}
