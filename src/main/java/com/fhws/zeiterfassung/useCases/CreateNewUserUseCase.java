package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.CreateNewUser;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserAlreadyExistsException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.RegisterRequest;
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
    public void create(RegisterRequest registerRequest) throws UserAlreadyExistsException, InvalidDataException {
        if (hasInvalidData(registerRequest))
            throw new InvalidDataException();
        try {
            userGateway.getUserByUsername(registerRequest.getUsername());
            throw new UserAlreadyExistsException();
        } catch (EntityNotFoundException e) {
            userGateway.addUser(getUserFromRegisterRequest(registerRequest));
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
