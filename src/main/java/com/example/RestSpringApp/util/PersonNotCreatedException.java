package com.example.RestSpringApp.util;

public class PersonNotCreatedException extends RuntimeException {

    public PersonNotCreatedException(String msg) {
        super(msg);
    }
}
