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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private ClassMapper classMapper;

    @InjectMocks
    private GenreService genreService;

    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        genreList = Arrays.asList(
                new Genre(1, "Genre1", "image1", new Date(), new Date(), new ArrayList<>()),
                new Genre(2, "Genre2", "image2", new Date(), new Date(), new ArrayList<>()),
                new Genre(3, "Genre3", "image3", new Date(), new Date(), new ArrayList<>())
        );
    }

    @Test
    void getAllGenres() {
        when(genreRepository.findAll()).thenReturn(genreList);
        when(classMapper.genreListToSimpleGenreDtoList(anyList())).thenReturn(Arrays.asList(
                new SimpleGenreDto("Genre1", "image1"),
                new SimpleGenreDto("Genre2", "image2"),
                new SimpleGenreDto("Genre3", "image3")
        ));
        assertEquals(3, genreService.getAllGenres().size());
        assertEquals(new SimpleGenreDto("Genre2", "image2"), genreService.getAllGenres().get(1));
    }

    @Test
    void getGenreById() {
        when(genreRepository.findById(1))
                .thenReturn(Optional.of(new Genre(1, "Genre1", "image1", new Date(), new Date(), new ArrayList<>())));
        GenreDto genreDto = new GenreDto(1, "Genre1", "image1", new Date(), new Date(), new ArrayList<>());
        when(classMapper.genreToGenreDto(any())).thenReturn(genreDto);
        assertEquals(genreDto, genreService.getGenreById(1));
    }

    @Test
    void getByIdList() {
        when(genreRepository.findMany(Arrays.asList(1, 2, 3))).thenReturn(genreList);
        assertEquals(genreList.size(), genreService.getByIdList(Arrays.asList(1, 2, 3)).size());
        assertEquals(genreList.get(1), genreService.getByIdList(Arrays.asList(1, 2, 3)).get(1));
    }

    @Test
    void createGenre() throws UniqueException {
        Genre genre = genreList.get(0);
        CreateGenreDto createGenreDto = new CreateGenreDto("Genre1", "image1");
        GenreDto genreDto = new GenreDto(1, "Genre1", "image1", new Date(), new Date(), new ArrayList<>());

        when(genreRepository.existsByName("Genre1")).thenReturn(false);
        when(classMapper.createGenreDtoToGenre(createGenreDto)).thenReturn(genre);
        when(classMapper.genreToGenreDto(genre)).thenReturn(genreDto);
        when(genreRepository.save(genre)).thenReturn(genre);
        assertEquals(genreDto, genreService.createGenre(createGenreDto));
    }

    @Test
    void createGenreUniqueException() {
        when(genreRepository.existsByName("Genre1")).thenReturn(true);
        assertThrows(UniqueException.class, () -> genreService.createGenre(new CreateGenreDto("Genre1", "image1")));
    }

    @Test
    void updateGenre() throws UniqueException, EntityNotFoundException {
        UpdateGenreDto updateGenreDto = new UpdateGenreDto("Genre1", "image1");
        Genre genre = genreList.get(0);
        GenreDto genreDto = new GenreDto(1, "UpdatedName", "UpdatedImage", new Date(), new Date(), new ArrayList<>());

        when(genreRepository.findById(1)).thenReturn(Optional.ofNullable(genre));
        when(genreRepository.existsByName("Genre1")).thenReturn(false);
        doNothing().when(classMapper).updateGenreDtoToGenre(isA(UpdateGenreDto.class), isA(Genre.class));
        when(classMapper.genreToGenreDto(any())).thenReturn(genreDto);
        assert genre != null;
        when(genreRepository.save(genre)).thenReturn(genre);
        assertEquals(genreDto, genreService.updateGenre(1, updateGenreDto));
    }

    @Test
    void updateGenreEntityNotFoundException() {
        when(genreRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> genreService.updateGenre(1, new UpdateGenreDto("Genre1", "image1")));
    }

    @Test
    void updateGenreUniqueException() {
        when(genreRepository.existsByName(any())).thenReturn(true);
        when(genreRepository.findById(1)).thenReturn(Optional.of(genreList.get(0)));

        assertThrows(UniqueException.class, () -> genreService.updateGenre(1, new UpdateGenreDto("NewName", "image1")));
    }

    @Test
    void deleteGenre() throws EntityNotFoundException {
        when(genreRepository.existsById(1)).thenReturn(true);
        doNothing().when(genreRepository).deleteById(1);
        genreService.deleteGenre(1);
    }

    @Test
    void deleteGenreEntityNotFoundException() {
        when(genreRepository.existsById(1)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> genreService.deleteGenre(1));
    }
}