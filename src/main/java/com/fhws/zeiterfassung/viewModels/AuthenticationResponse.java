package com.fhws.zeiterfassung.viewModels;

public class AuthenticationResponse {

    private final String jwt;
    private final AccountInformationViewModel accountInformationViewModel;

    public AuthenticationResponse(String jwt, AccountInformationViewModel accountInformationViewModel) {
        this.jwt = jwt;
        this.accountInformationViewModel = accountInformationViewModel;
    }

    public String getJwt() {
        return jwt;
    }

    public AccountInformationViewModel getAccountInformationViewModel() {
        return accountInformationViewModel;
    }
}
