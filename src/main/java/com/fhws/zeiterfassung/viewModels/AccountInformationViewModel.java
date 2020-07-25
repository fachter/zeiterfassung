package com.fhws.zeiterfassung.viewModels;

public class AccountInformationViewModel {

    String fullName;
    String username;
    String email;

    public String getFullName() {
        return fullName;
    }

    public AccountInformationViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AccountInformationViewModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountInformationViewModel setEmail(String email) {
        this.email = email;
        return this;
    }
}
