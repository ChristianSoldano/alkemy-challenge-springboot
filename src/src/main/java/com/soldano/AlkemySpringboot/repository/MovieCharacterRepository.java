package com.soldano.AlkemySpringboot.repository;

import com.soldano.AlkemySpringboot.model.MovieCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCharacterRepository extends JpaRepository<MovieCharacter, Integer> {
    List<MovieCharacter> findIdsByCharacterId(Integer id);
}
