package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.gateways.UserGateway;
import com.fhws.zeiterfassung.boundaries.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.boundaries.useCases.DeleteUsersWorkedTime;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUsersWorkedTimeUseCaseTest {

    @Mock private WorkedTimeGateway workedTimeGatewayMock;
    @Mock private UserGateway userGatewayMock;
    private DeleteUsersWorkedTime deleteUsersWorkedTime;
    private final String validUsername = "validUsername";
    private User validUser;
    @Captor private ArgumentCaptor<ArrayList<WorkedTime>> captor;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername(validUsername);
        deleteUsersWorkedTime = new DeleteUsersWorkedTimeUseCase(workedTimeGatewayMock, userGatewayMock);
    }

    @Test
    public void givenInvalidUsername() throws Exception {
        when(userGatewayMock.getUserByUsername("invalidUsername")).thenThrow(UserDoesNotExistException.class);
        assertThrows(UserDoesNotExistException.class,
                () -> deleteUsersWorkedTime.delete(new ArrayList<>(), "invalidUsername"));
    }

    @Test
    public void givenValidUserEmptyArray() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);

        deleteUsersWorkedTime.delete(new ArrayList<>(), validUsername);

        verify(workedTimeGatewayMock, times(0)).remove(captor.capture());
    }

    @Test
    public void givenDelete() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<WorkedTime> existingTimes = new ArrayList<>();
        WorkedTime workedTime = new WorkedTime();
        workedTime.setId(1234L);
        existingTimes.add(workedTime);
        when(workedTimeGatewayMock.getAllByUserOrderedByDate(validUser)).thenReturn(existingTimes);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1234L);

        deleteUsersWorkedTime.delete(ids, validUsername);

        verify(workedTimeGatewayMock, times(1)).remove(captor.capture());
        ArrayList<WorkedTime> deletedTimes = captor.getValue();
        assertEquals(1, deletedTimes.size());
        assertEquals(workedTime, deletedTimes.get(0));
    }

    @Test
    public void givenIdsNotFoundInList() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<WorkedTime> existingTimes = new ArrayList<>();
        WorkedTime workedTime1 = new WorkedTime();
        workedTime1.setId(1234L);
        existingTimes.add(workedTime1);
        WorkedTime workedTime2 = new WorkedTime();
        workedTime2.setId(12345L);
        existingTimes.add(workedTime2);
        WorkedTime workedTime3 = new WorkedTime();
        workedTime3.setId(123456L);
        existingTimes.add(workedTime3);
        when(workedTimeGatewayMock.getAllByUserOrderedByDate(validUser)).thenReturn(existingTimes);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(123456L);
        ids.add(3152L);
        ids.add(53681L);
        ids.add(1234L);

        deleteUsersWorkedTime.delete(ids, validUsername);

        verify(workedTimeGatewayMock, times(1)).remove(captor.capture());
        ArrayList<WorkedTime> deletedTimes = captor.getValue();
        assertEquals(2, deletedTimes.size());
        assertEquals(workedTime3, deletedTimes.get(0));
        assertEquals(workedTime1, deletedTimes.get(1));
    }
}