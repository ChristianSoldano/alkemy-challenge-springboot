package com.soldano.AlkemySpringboot.dto.character;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soldano.AlkemySpringboot.dto.movie.SimpleMovieDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDto {

    private Integer id;

    private String name;

    private Integer age;

    private Float weight;

    private String story;

    private String image;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    private List<SimpleMovieDto> movies;
}
