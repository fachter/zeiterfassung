package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.CreateNewUser;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.*;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateNewUserUseCaseTest {

    private CreateNewUser createNewUser;
    @Mock
    private UserGateway userGatewayMock;
    private final String newUsername = "newUser";
    private final String validEmail = "valid@email";

    @BeforeEach
    void setUp() {
        createNewUser = new CreateNewUserUseCase(userGatewayMock);
    }

    @Test
    public void givenNoRegisterRequest_throwInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> createNewUser.create(null));
    }

    @ParameterizedTest
    @CsvSource(value = {",ok,ok", "ok,,ok", "ok,ok,"})
    public void givenEmptyStringAsValue_throwInvalidDataException(String name, String pw, String mail) {
        RegisterRequest registerRequest = new RegisterRequest()
                .setUsername(Objects.toString(name, ""))
                .setPassword(Objects.toString(pw, ""))
                .setEmail(Objects.toString(mail, ""));

        assertThrows(InvalidDataException.class, () -> createNewUser.create(registerRequest));
    }

    @ParameterizedTest
    @CsvSource(value = {",ok,ok", "ok,,ok", "ok,ok,"})
    public void givenNullAsValue_throwInvalidDataException(String name, String pw, String mail) {
        RegisterRequest registerRequest = new RegisterRequest()
                .setUsername(name)
                .setPassword(pw)
                .setEmail(mail);

        assertThrows(InvalidDataException.class, () -> createNewUser.create(registerRequest));
    }

    @Test
    public void givenUsernameAlreadyExists_throwException() throws Exception {
        when(userGatewayMock.getUserByUsername("existingUser")).thenReturn(new User());
        RegisterRequest registerRequest = new RegisterRequest()
                .setUsername("existingUser")
                .setPassword("test")
                .setEmail("mail");
        ArrayList<String> expectedErrors = new ArrayList<>();
        expectedErrors.add("Username already exists");

        boolean exceptionThrown = false;
        try {
            createNewUser.create(registerRequest);
        } catch (UserAlreadyExistsException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        assertThat(registerRequest.getUsernameErrors()).usingRecursiveFieldByFieldElementComparator().isEqualTo(expectedErrors);
    }

    @Test
    public void givenAllFieldsAreSet() throws Exception {
        prepareMocksBothThrowEntityNotFoundException();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        RegisterRequest registerRequest = new RegisterRequest()
                .setUsername(newUsername)
                .setPassword("test")
                .setEmail(validEmail)
                .setFullName("New User");
        User expectedUser = new User();
        expectedUser.setUsername(newUsername);
        expectedUser.setPassword("test");
        expectedUser.setEmail(validEmail);
        expectedUser.setFullName("New User");

        createNewUser.create(registerRequest);

        verify(userGatewayMock, times(1)).addUser(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    public void givenNoFullName_thenUseUsername() throws Exception {
        prepareMocksBothThrowEntityNotFoundException();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        RegisterRequest registerRequest = new RegisterRequest()
                .setUsername(newUsername)
                .setPassword("test")
                .setEmail(validEmail);
        User expectedUser = new User();
        expectedUser.setUsername(newUsername);
        expectedUser.setPassword("test");
        expectedUser.setEmail(validEmail);
        expectedUser.setFullName(newUsername);

        createNewUser.create(registerRequest);

        verify(userGatewayMock, times(1)).addUser(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    public void givenEmailExists_thenThrowException() throws Exception {
        when(userGatewayMock.getUserByUsername(newUsername)).thenThrow(UserDoesNotExistException.class);
        when(userGatewayMock.getUserByEmail("invalid@email")).thenReturn(new User());
        RegisterRequest registerRequest = new RegisterRequest()
                .setUsername(newUsername)
                .setEmail("invalid@email")
                .setPassword("valid")
                .setFullName("Valid");
        ArrayList<String> expectedErrors = new ArrayList<>();
        expectedErrors.add("Email already exists");

        boolean exceptionThrown = false;
        try {
            createNewUser.create(registerRequest);
        } catch (EmailAlreadyExistsException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        assertThat(registerRequest.getEmailErrors()).usingRecursiveFieldByFieldElementComparator().isEqualTo(expectedErrors);
    }

    private void prepareMocksBothThrowEntityNotFoundException() throws UserDoesNotExistException {
        when(userGatewayMock.getUserByUsername(newUsername)).thenThrow(UserDoesNotExistException.class);
        when(userGatewayMock.getUserByEmail(validEmail)).thenThrow(UserDoesNotExistException.class);
    }
}