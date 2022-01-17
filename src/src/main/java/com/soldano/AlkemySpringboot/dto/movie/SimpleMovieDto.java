package com.soldano.AlkemySpringboot.dto.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMovieDto {

    private String title;

    private String image;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
}
