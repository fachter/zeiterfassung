package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.viewModels.AccountInformationViewModel;
import org.springframework.security.core.userdetails.UserDetails;

public interface GetAccountInformation {

    AccountInformationViewModel getViewModelFromUserDetails(UserDetails userDetails);
}
