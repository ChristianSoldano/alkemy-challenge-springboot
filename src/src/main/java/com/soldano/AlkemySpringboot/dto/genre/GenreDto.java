package com.soldano.AlkemySpringboot.dto.genre;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soldano.AlkemySpringboot.dto.movie.SimpleMovieDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    private Integer id;

    private String name;

    private String image;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    private List<SimpleMovieDto> movies;
}