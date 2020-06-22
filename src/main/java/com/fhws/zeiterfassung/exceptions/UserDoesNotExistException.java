package com.fhws.zeiterfassung.exceptions;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException() {
        super("User does not exist!");
    }
}
