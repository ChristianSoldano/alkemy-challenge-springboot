package com.soldano.AlkemySpringboot.dto.character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCharacterDto {

    private String name;

    private String image;
}
