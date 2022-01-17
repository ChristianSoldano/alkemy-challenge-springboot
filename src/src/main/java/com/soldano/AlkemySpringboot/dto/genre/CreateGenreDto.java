package com.soldano.AlkemySpringboot.dto.genre;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGenreDto {
    @NotEmpty
    @Schema(example = "My new Genre", required = true)
    private String name;

    @NotEmpty
    @Schema(example = "https://www.fakeimgurls.com/new-genre", required = true)
    private String image;
}
