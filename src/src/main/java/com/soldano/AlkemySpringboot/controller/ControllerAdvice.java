package com.soldano.AlkemySpringboot.controller;

import com.soldano.AlkemySpringboot.dto.ValidationErrorDto;
import com.soldano.AlkemySpringboot.dto.error.ErrorDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return new ValidationErrorDto(1, errors);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UniqueException.class)
    public ErrorDto handleUniqueConstraint(UniqueException e) {
        return new ErrorDto(2, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorDto handleEntityNotFound(EntityNotFoundException e) {
        return new ErrorDto(3, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorDto handleBadCredentialsException(BadCredentialsException e) {
        return new ErrorDto(4, e.getMessage());
    }
}
