package com.soldano.AlkemySpringboot.repository;

import com.soldano.AlkemySpringboot.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM CHARACTERS C INNER JOIN MOVIES_CHARACTERS MC ON C.ID = MC.CHARACTER_ID " +
            "WHERE (:name IS NULL OR C.NAME like %:name%) AND (:age IS NULL OR C.AGE = :age) AND " +
            "(:movie IS NULL OR MC.MOVIE_ID = :movie) GROUP BY C.ID", nativeQuery = true)
    List<Character> getCharactersByParams(@Param("name") String name, @Param("age") String age, @Param("movie") String movie);
}
