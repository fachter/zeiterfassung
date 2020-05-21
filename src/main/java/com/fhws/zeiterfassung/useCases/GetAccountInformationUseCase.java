package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.GetAccountInformation;
import com.fhws.zeiterfassung.models.AccountInformationViewModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GetAccountInformationUseCase implements GetAccountInformation {

    @Override
    public AccountInformationViewModel getViewModelFromUserDetails(UserDetails userDetails) {
        return null;
    }
}
