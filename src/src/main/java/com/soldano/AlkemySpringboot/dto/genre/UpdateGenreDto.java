package com.soldano.AlkemySpringboot.dto.genre;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGenreDto {

    @NotEmpty
    @Schema(example = "Updated Name", required = true)
    public String name;

    @NotEmpty
    @Schema(example = "https://www.fakeimgurls.com/updated-genre", required = true)
    public String image;
}
