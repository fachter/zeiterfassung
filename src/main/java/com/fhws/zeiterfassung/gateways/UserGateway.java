package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;

public interface UserGateway {

    User getUserByUsername(String username) throws UserDoesNotExistException;

    User getUserByEmail(String email) throws UserDoesNotExistException;

    void addUser(User user) throws InvalidDataException;
}
