package com.soldano.AlkemySpringboot.exceptions;

public class EntityNotFoundException extends Exception{
    public EntityNotFoundException(String entity) {
        super(entity + " not found");
    }
}
