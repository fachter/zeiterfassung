package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.GetUsersTime;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GetUsersTimeUseCase implements GetUsersTime {

    private final UserGateway userGateway;

    public GetUsersTimeUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public ArrayList<WorkedTimeViewModel> get(String username) throws UserDoesNotExistException {
        User user = userGateway.getUserByUsername(username);
        return new ArrayList<>();
    }
}
