package com.soldano.AlkemySpringboot.dto.character;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCharacterDto {
    @NotEmpty
    @Schema(example = "The Best Character", required = true)
    private String name;

    @NotNull
    @Schema(example = "25", required = true)
    private Integer age;

    @NotNull
    @Schema(example = "80", required = true)
    private Float weight;

    @NotEmpty
    @Schema(example = "This is the story about the best character ever", required = true)
    private String story;

    @NotEmpty
    @Schema(example = "https://www.fakeimgurls.com/best-character-img", required = true)
    private String image;

    @NotEmpty
    @Schema(example = "[1,2,3]", required = true)
    private List<Integer> movies;
}
