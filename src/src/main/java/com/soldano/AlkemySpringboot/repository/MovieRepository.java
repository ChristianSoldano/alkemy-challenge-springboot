package com.soldano.AlkemySpringboot.repository;

import com.soldano.AlkemySpringboot.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    boolean existsByTitle(String title);

    @Query(value = "SELECT * FROM MOVIES M INNER JOIN MOVIES_GENRES MG ON M.ID = MG.MOVIE_ID INNER JOIN GENRES G " +
            "ON G.ID = MG.GENRE_ID WHERE (:title IS NULL OR M.TITLE LIKE %:title%) AND (:genre IS NULL OR G.ID = :genre) " +
            "GROUP BY M.ID ORDER BY RELEASE_DATE ASC", nativeQuery = true)
    List<Movie> getMovieByParamsASC(@Param("title") String title, @Param("genre") String genre);

    @Query(value = "SELECT * FROM MOVIES M INNER JOIN MOVIES_GENRES MG ON M.ID = MG.MOVIE_ID INNER JOIN GENRES G " +
            "ON G.ID = MG.GENRE_ID WHERE (:title IS NULL OR M.TITLE LIKE %:title%) AND (:genre IS NULL OR G.ID = :genre) " +
            "GROUP BY M.ID ORDER BY RELEASE_DATE DESC", nativeQuery = true)
    List<Movie> getMovieByParamsDESC(@Param("title") String title, @Param("genre") String genre);

    @Query(value = "SELECT * FROM MOVIES WHERE ID IN (:ids)", nativeQuery = true)
    List<Movie> findMany(@Param("ids") List<Integer> ids);
}
