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
public class UpdateCharacterDto {
    @NotEmpty
    @Schema(example = "New Name", required = true)
    private String name;

    @NotNull
    @Schema(example = "30", required = true)
    private Integer age;

    @NotNull
    @Schema(example = "70", required = true)
    private Float weight;

    @NotEmpty
    @Schema(example = "Updated Story", required = true)
    private String story;

    @NotEmpty
    @Schema(example = "https://www.fakeimgurls.com/new-best-character-img", required = true)
    private String image;

    @NotEmpty
    @Schema(example = "[1]", required = true)
    private List<Integer> movies;
}
