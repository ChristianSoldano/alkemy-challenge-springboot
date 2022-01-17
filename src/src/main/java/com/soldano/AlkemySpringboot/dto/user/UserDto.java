package com.soldano.AlkemySpringboot.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotEmpty
    @Schema(example = "TestingUser", required = true)
    @Email
    private String username;

    @NotEmpty
    @Schema(example = "StrongPassword!", required = true)
    private String password;
}
