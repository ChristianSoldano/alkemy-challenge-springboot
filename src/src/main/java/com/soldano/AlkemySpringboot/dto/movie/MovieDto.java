package com.soldano.AlkemySpringboot.dto.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soldano.AlkemySpringboot.dto.character.SimpleCharacterDto;
import com.soldano.AlkemySpringboot.dto.genre.SimpleGenreDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Integer id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    private String image;

    private Integer rating;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    private List<SimpleGenreDto> genres;

    private List<SimpleCharacterDto> characters;
}
