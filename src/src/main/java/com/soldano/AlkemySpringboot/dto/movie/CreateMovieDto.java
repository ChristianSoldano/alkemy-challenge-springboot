package com.soldano.AlkemySpringboot.dto.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieDto {

    @NotEmpty
    @Schema(example = "Fake Movie", required = true)
    private String title;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "1996-12-12", required = true, format = "date", type = "string")
    private Date releaseDate;

    @NotEmpty
    @Schema(example = "https://www.fakeimgurls.com/fake-movie", required = true)
    private String image;

    @Min(1)
    @Max(5)
    @NotNull
    @Schema(example = "5", required = true)
    private Integer rating;

    @NotEmpty
    @Schema(example = "[1,2,3,4]", required = true)
    private List<Integer> genres;
}
