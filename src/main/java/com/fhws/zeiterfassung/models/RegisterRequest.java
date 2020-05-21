package com.fhws.zeiterfassung.models;

import java.util.ArrayList;

public class RegisterRequest {

    private String username;
    private ArrayList<String> usernameErrors = new ArrayList<>();
    private String password;
    private ArrayList<String> passwordErrors = new ArrayList<>();
    private String fullName;
    private String email;
    private ArrayList<String> emailErrors = new ArrayList<>();

    public RegisterRequest() {}

    public RegisterRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public RegisterRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public ArrayList<String> getUsernameErrors() {
        return usernameErrors;
    }

    public RegisterRequest setUsernameErrors(ArrayList<String> usernameErrors) {
        this.usernameErrors = usernameErrors;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public ArrayList<String> getPasswordErrors() {
        return passwordErrors;
    }

    public RegisterRequest setPasswordErrors(ArrayList<String> passwordErrors) {
        this.passwordErrors = passwordErrors;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public RegisterRequest setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public ArrayList<String> getEmailErrors() {
        return emailErrors;
    }

    public RegisterRequest setEmailErrors(ArrayList<String> emailErrors) {
        this.emailErrors = emailErrors;
        return this;
    }
}
