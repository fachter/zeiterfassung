package com.fhws.zeiterfassung.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
        super("This username already exists");
    }
}
