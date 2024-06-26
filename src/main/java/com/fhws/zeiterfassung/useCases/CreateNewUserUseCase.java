package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.useCases.CreateNewUser;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.*;
import com.fhws.zeiterfassung.boundaries.gateways.UserGateway;
import com.fhws.zeiterfassung.viewModels.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateNewUserUseCase implements CreateNewUser {

    private final UserGateway userGateway;

    @Autowired
    public CreateNewUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public void create(RegisterRequest registerRequest) throws UserAlreadyExistsException, InvalidDataException, EmailAlreadyExistsException {
        if (hasInvalidData(registerRequest))
            throw new InvalidDataException();
        try {
            userGateway.getUserByUsername(registerRequest.getUsername());
            registerRequest.getUsernameErrors().add("Username already exists");
            throw new UserAlreadyExistsException();
        } catch (UserDoesNotExistException e) {
            try {
                userGateway.getUserByEmail(registerRequest.getEmail());
                registerRequest.getEmailErrors().add("Email already exists");
                throw new EmailAlreadyExistsException();
            } catch (UserDoesNotExistException entityNotFoundException) {
                userGateway.addUser(getUserFromRegisterRequest(registerRequest));
            }
        }
    }

    private boolean hasInvalidData(RegisterRequest registerRequest) {
        return (registerRequest == null || registerRequest.getUsername() == null || stringIsEmptyOrNull(registerRequest.getUsername())
                || stringIsEmptyOrNull(registerRequest.getPassword()) || stringIsEmptyOrNull(registerRequest.getEmail()));
    }

    private boolean stringIsEmptyOrNull(String email) {
        return email == null || email.equals("");
    }

    private User getUserFromRegisterRequest(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setFullName(getFullName(registerRequest));
        return user;
    }

    private String getFullName(RegisterRequest registerRequest) {
        return !stringIsEmptyOrNull(registerRequest.getFullName()) ?
                registerRequest.getFullName() : registerRequest.getUsername();
    }
}
