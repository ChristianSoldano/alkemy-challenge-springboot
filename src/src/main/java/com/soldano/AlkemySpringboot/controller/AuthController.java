package com.soldano.AlkemySpringboot.controller;

import com.soldano.AlkemySpringboot.dto.user.LoginResponse;
import com.soldano.AlkemySpringboot.dto.user.UserDto;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.service.AuthService;
import com.soldano.AlkemySpringboot.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(description = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDto user) throws UniqueException {
        authService.registerUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserDto user) throws BadCredentialsException {
        return ResponseEntity.ok(authService.authUser(user));
    }
}
