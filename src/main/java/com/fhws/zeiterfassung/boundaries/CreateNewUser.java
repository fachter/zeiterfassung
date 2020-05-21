package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.exceptions.EmailAlreadyExistsException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserAlreadyExistsException;
import com.fhws.zeiterfassung.models.RegisterRequest;

public interface CreateNewUser {

    void create(RegisterRequest registerRequest) throws UserAlreadyExistsException, InvalidDataException, EmailAlreadyExistsException;
}
