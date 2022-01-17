package com.soldano.AlkemySpringboot.exceptions;

public class UniqueException extends Exception{
    public UniqueException(String column, String value){
        super("Duplicate entry '" + value + "' for key " + column);
    }
}
