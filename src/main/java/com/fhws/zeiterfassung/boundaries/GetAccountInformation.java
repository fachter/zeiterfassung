package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.models.AccountInformationViewModel;
import org.springframework.security.core.userdetails.UserDetails;

public interface GetAccountInformation {

    AccountInformationViewModel getViewModelFromUserDetails(UserDetails userDetails);
}
