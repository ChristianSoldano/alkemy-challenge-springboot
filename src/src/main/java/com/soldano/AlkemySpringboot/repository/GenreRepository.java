package com.soldano.AlkemySpringboot.repository;

import com.soldano.AlkemySpringboot.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM GENRES WHERE ID IN (:ids)", nativeQuery = true)
    List<Genre> findMany(@Param("ids") List<Integer> ids);
}
