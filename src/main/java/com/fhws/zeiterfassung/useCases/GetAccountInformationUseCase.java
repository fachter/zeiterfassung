package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.GetAccountInformation;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.AccountInformationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GetAccountInformationUseCase implements GetAccountInformation {

    private final UserGateway userGateway;

    @Autowired
    public GetAccountInformationUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public AccountInformationViewModel getViewModelFromUserDetails(UserDetails userDetails) {
        try {
            if (userDetails != null)
                return getVmFromUser(userGateway.getUserByUsername(userDetails.getUsername()));
        } catch (UserDoesNotExistException ignored) {}
        return new AccountInformationViewModel();
    }

    private AccountInformationViewModel getVmFromUser(User user) {
        return new AccountInformationViewModel()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setFullName(user.getFullName());
    }
}
