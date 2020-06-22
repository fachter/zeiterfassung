package com.fhws.zeiterfassung.services;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final UserGateway userGateway;

    @Autowired
    public UserRepositoryUserDetailsService(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        try {
            return userGateway.getUserByUsername(username);
        } catch (UserDoesNotExistException e) {
            throw new UsernameNotFoundException("User with the username '" + username + "' not found.");
        }
    }
}
