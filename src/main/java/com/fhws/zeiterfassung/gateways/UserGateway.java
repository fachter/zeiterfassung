package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;

public interface UserGateway {

    User getUserByUsername(String username) throws EntityNotFoundException;

    void addUser(User user) throws InvalidDataException;
}
