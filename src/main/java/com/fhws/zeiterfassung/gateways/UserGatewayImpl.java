package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGatewayImpl implements UserGateway {

    //TODO: test it
    private final UserRepository userRepository;

    @Autowired
    public UserGatewayImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUsername(String username) throws UserDoesNotExistException {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            throw new UserDoesNotExistException();
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws UserDoesNotExistException {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new UserDoesNotExistException();
        return user;
    }

    @Override
    public void addUser(User user) throws InvalidDataException {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidDataException();
        }
    }
}
