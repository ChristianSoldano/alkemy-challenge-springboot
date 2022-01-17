package com.soldano.AlkemySpringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDto {
    private Integer code;
    private Map<String,String> message;
}
