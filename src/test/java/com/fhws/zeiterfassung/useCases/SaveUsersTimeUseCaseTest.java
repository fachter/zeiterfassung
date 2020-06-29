package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.SaveUsersTime;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.models.KundenViewModel;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.lang.model.util.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveUsersTimeUseCaseTest {

    private SaveUsersTime saveUsersTime;
    @Mock private UserGateway userGatewayMock;
    @Mock private WorkedTimeGateway workedTimeGatewayMock;
    private final String validUsername = "validUsername";
    private User validUser;
    @Captor private ArgumentCaptor<ArrayList<WorkedTime>> captor;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername(validUsername);
        saveUsersTime = new SaveUsersTimeUseCase(userGatewayMock, workedTimeGatewayMock);
    }

    @Test
    public void givenUserDoesNotExist() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        assertThrows(UserDoesNotExistException.class, () -> saveUsersTime.save(new ArrayList<>(), "invalid"));
    }

    @Test
    public void givenValidUserAddsNothing() throws Exception {
        prepareUserGatewayMock();

        saveUsersTime.save(new ArrayList<>(), validUsername);

        verify(workedTimeGatewayMock, times(0)).saveAll(captor.capture());
    }

    @Test
    public void givenValidUserAddsOneTimeWithNonExistingKundeAndProjekt() throws Exception {
        prepareUserGatewayMock();
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = new WorkedTimeViewModel();
        viewModel.id = null;
        viewModel.beschreibung = "Test";
        ProjektViewModel projektViewModel = new ProjektViewModel();
        projektViewModel.id = null;
        projektViewModel.projektName = "Test Projekt";
        viewModel.projektViewModel = projektViewModel;
        KundenViewModel kundenViewModel = new KundenViewModel();
        kundenViewModel.id = null;
        kundenViewModel.kundenName = "Test Kunde";
        viewModel.kundenViewModel = kundenViewModel;
        viewModel.startTime = LocalDateTime.of(2020,1,2,3,4,5,6);
        viewModel.endTime = LocalDateTime.of(2020, 1,2,4,4,5,6);
        workedTimeViewModels.add(viewModel);
        WorkedTime expectedWorkedTime = new WorkedTime()
                .setKunde(new Kunde().setKundenName("Test Kunde"))
                .setProjekt(new Projekt().setProjektName("Test Projekt"))
                .setStartTime(LocalDateTime.of(2020,1,2,3,4))
                .setEndTime(LocalDateTime.of(2020,1,2,4,4))
                .setBeschreibung("Test");
        expectedWorkedTime.setCreatedBy(validUser);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(1, workedTimes.size());
        assertThat(workedTimes.get(0)).usingRecursiveComparison().isEqualTo(expectedWorkedTime);
    }

    private void prepareUserGatewayMock() throws UserDoesNotExistException {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
    }

    @Test
    public void givenInvalidTime() throws Exception {
        prepareUserGatewayMock();
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = new WorkedTimeViewModel();
        viewModel.startTime = null;
        viewModel.endTime = null;
        workedTimeViewModels.add(viewModel);

        assertThrows(InvalidDataException.class, () -> saveUsersTime.save(workedTimeViewModels, validUsername));
    }

    @Test
    public void givenNoKundeAndNoProjekt() throws Exception {
        prepareUserGatewayMock();
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = new WorkedTimeViewModel();
        viewModel.startTime = LocalDateTime.now();
        viewModel.endTime = LocalDateTime.now();
        viewModel.beschreibung = "Test";
        workedTimeViewModels.add(viewModel);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(1, workedTimes.size());
    }
}