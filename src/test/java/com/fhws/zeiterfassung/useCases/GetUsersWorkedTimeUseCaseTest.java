package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.GetUsersTime;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUsersWorkedTimeUseCaseTest {

    private GetUsersTime getUsersTime;
    @Mock private UserGateway userGatewayMock;

    @BeforeEach
    void setUp() {
        getUsersTime = new GetUsersTimeUseCase(userGatewayMock);
    }

    @Test
    public void givenUserDoesNotExist() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        Assertions.assertThrows(UserDoesNotExistException.class, () -> getUsersTime.get("invalid"));
    }

    @Test
    public void givenTimeGatewayThrowsException() throws Exception {
        //
    }
}