package com.soldano.AlkemySpringboot.repository;

import com.soldano.AlkemySpringboot.model.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Integer> {

    List<MovieGenre> findIdsByMovieId(Integer id);

}
