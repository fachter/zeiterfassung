package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.SaveUsersTime;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.KundeGateway;
import com.fhws.zeiterfassung.gateways.ProjektGateway;
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

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveUsersTimeUseCaseTest {

    private SaveUsersTime saveUsersTime;
    @Mock
    private UserGateway userGatewayMock;
    @Mock
    private WorkedTimeGateway workedTimeGatewayMock;
    @Mock
    private KundeGateway kundeGatewayMock;
    @Mock
    private ProjektGateway projektGatewayMock;
    private final String validUsername = "validUsername";
    private User validUser;
    @Captor
    private ArgumentCaptor<ArrayList<WorkedTime>> captor;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername(validUsername);
        saveUsersTime = new SaveUsersTimeUseCase(userGatewayMock, workedTimeGatewayMock, kundeGatewayMock, projektGatewayMock);
    }

    @Test
    public void givenUserDoesNotExist() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        assertThrows(UserDoesNotExistException.class, () -> saveUsersTime.save(new ArrayList<>(), "invalid"));
    }

    @Test
    public void givenValidUserAddsNothing() throws Exception {
        prepareGatewayMock(new ArrayList<>(), new ArrayList<>());

        saveUsersTime.save(new ArrayList<>(), validUsername);

        verify(workedTimeGatewayMock, times(0)).saveAll(captor.capture());
    }

    @Test
    public void givenValidUserAddsOneTimeWithNonExistingKundeAndProjekt() throws Exception {
        prepareGatewayMock(new ArrayList<>(), new ArrayList<>());
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
        viewModel.startTime = LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6);
        viewModel.endTime = LocalDateTime.of(2020, 1, 2, 4, 4, 5, 6);
        workedTimeViewModels.add(viewModel);
        WorkedTime expectedWorkedTime = new WorkedTime()
                .setKunde(new Kunde().setKundenName("Test Kunde"))
                .setProjekt(new Projekt().setProjektName("Test Projekt"))
                .setStartTime(LocalDateTime.of(2020, 1, 2, 3, 4))
                .setEndTime(LocalDateTime.of(2020, 1, 2, 4, 4))
                .setBeschreibung("Test");
        expectedWorkedTime.setCreatedBy(validUser);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(1, workedTimes.size());
        assertThat(workedTimes.get(0)).usingRecursiveComparison().isEqualTo(expectedWorkedTime);
    }

    private void prepareGatewayMock(ArrayList<Kunde> kunden, ArrayList<Projekt> projekte) throws UserDoesNotExistException {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        when(kundeGatewayMock.getAllByUser(validUser)).thenReturn(kunden);
        when(projektGatewayMock.getAllByUser(validUser)).thenReturn(projekte);
    }

    @Test
    public void givenInvalidTime() throws Exception {
        prepareGatewayMock(new ArrayList<>(), new ArrayList<>());
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = new WorkedTimeViewModel();
        viewModel.startTime = null;
        viewModel.endTime = null;
        workedTimeViewModels.add(viewModel);

        assertThrows(InvalidDataException.class, () -> saveUsersTime.save(workedTimeViewModels, validUsername));
    }

    @Test
    public void givenNoKundeAndNoProjekt() throws Exception {
        prepareGatewayMock(new ArrayList<>(), new ArrayList<>());
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = getWorkedTimeViewModel();
        workedTimeViewModels.add(viewModel);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(1, workedTimes.size());
        assertNull(workedTimes.get(0).getKunde());
        assertNull(workedTimes.get(0).getProjekt());
    }

    @Test
    public void givenKundeAlreadyExists() throws Exception {
        ArrayList<Kunde> kunden = new ArrayList<>();
        Kunde kunde = new Kunde().setKundenName("Test Kundenname");
        kunde.setId(5L);
        kunden.add(kunde);
        prepareGatewayMock(kunden, new ArrayList<>());
        when(kundeGatewayMock.getAllByUser(validUser)).thenReturn(kunden);
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = getWorkedTimeViewModel();
        KundenViewModel kundenViewModel = new KundenViewModel();
        kundenViewModel.id = 5L;
        kundenViewModel.kundenName = "Test Kundenname";
        viewModel.kundenViewModel = kundenViewModel;
        workedTimeViewModels.add(viewModel);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(1, workedTimes.size());
        assertEquals(kunde, workedTimes.get(0).getKunde());
    }


    @Test
    public void givenProjektAlreadyExists() throws Exception {
        ArrayList<Projekt> projekte = new ArrayList<>();
        Projekt projekt = new Projekt().setProjektName("Test Projekt");
        projekt.setId(5L);
        projekte.add(projekt);
        prepareGatewayMock(new ArrayList<>(), projekte);

        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = getWorkedTimeViewModel();
        ProjektViewModel projektViewModel = new ProjektViewModel();
        projektViewModel.id = 5L;
        projektViewModel.projektName = "Test Projekt";
        viewModel.projektViewModel = projektViewModel;
        workedTimeViewModels.add(viewModel);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(1, workedTimes.size());
        assertEquals(projekt, workedTimes.get(0).getProjekt());
    }

    private WorkedTimeViewModel getWorkedTimeViewModel() {
        WorkedTimeViewModel viewModel = new WorkedTimeViewModel();
        viewModel.startTime = LocalDateTime.now();
        viewModel.endTime = LocalDateTime.now();
        viewModel.beschreibung = "Test";
        return viewModel;
    }

    @Test
    public void givenNewKundeAndNewProjekt() throws Exception {
        prepareGatewayMock(new ArrayList<>(), new ArrayList<>());
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = getWorkedTimeViewModel();
        viewModel.startTime = LocalDateTime.of(2020,1,1,20,15,0,0);
        viewModel.endTime = LocalDateTime.of(2020,1,1,22,15,0,0);
        viewModel.beschreibung = "Beschreibung";
        ProjektViewModel projektViewModel = new ProjektViewModel();
        projektViewModel.id = -5L;
        projektViewModel.projektName = "Test Projekt";
        viewModel.projektViewModel = projektViewModel;
        KundenViewModel kundenViewModel = new KundenViewModel();
        kundenViewModel.id = -3L;
        kundenViewModel.kundenName = "Test Kunde";
        viewModel.kundenViewModel = kundenViewModel;
        workedTimeViewModels.add(viewModel);
        WorkedTime expectedWorkedTime = new WorkedTime()
                .setBeschreibung("Beschreibung")
                .setStartTime(LocalDateTime.of(2020,1,1,20,15,0,0))
                .setEndTime(LocalDateTime.of(2020,1,1,22,15,0,0))
                .setKunde(new Kunde().setKundenName("Test Kunde"))
                .setProjekt(new Projekt().setProjektName("Test Projekt"));
        expectedWorkedTime.setCreatedBy(validUser);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(1, workedTimes.size());
        assertThat(workedTimes.get(0)).usingRecursiveComparison().isEqualTo(expectedWorkedTime);

    }

    @Test
    public void givenNewKundeAndNewProjekt2Times() throws Exception {
        prepareGatewayMock(new ArrayList<>(), new ArrayList<>());
        ArrayList<WorkedTimeViewModel> workedTimeViewModels = new ArrayList<>();
        WorkedTimeViewModel viewModel = getWorkedTimeViewModel();
        viewModel.startTime = LocalDateTime.of(2020,1,1,20,15,0,0);
        viewModel.endTime = LocalDateTime.of(2020,1,1,22,15,0,0);
        viewModel.beschreibung = "Beschreibung";
        ProjektViewModel projektViewModel1 = new ProjektViewModel();
        projektViewModel1.id = -5L;
        projektViewModel1.projektName = "Test Projekt";
        viewModel.projektViewModel = projektViewModel1;
        KundenViewModel kundenViewModel1 = new KundenViewModel();
        kundenViewModel1.id = -3L;
        kundenViewModel1.kundenName = "Test Kunde";
        viewModel.kundenViewModel = kundenViewModel1;
        workedTimeViewModels.add(viewModel);
        WorkedTimeViewModel viewModel2 = getWorkedTimeViewModel();
        viewModel2.startTime = LocalDateTime.of(2020,1,1,20,15,0,0);
        viewModel2.endTime = LocalDateTime.of(2020,1,1,22,15,0,0);
        viewModel2.beschreibung = "Beschreibung";
        ProjektViewModel projektViewModel2 = new ProjektViewModel();
        projektViewModel2.id = -5L;
        projektViewModel2.projektName = "Test Projekt";
        viewModel2.projektViewModel = projektViewModel2;
        KundenViewModel kundenViewModel2 = new KundenViewModel();
        kundenViewModel2.id = -3L;
        kundenViewModel2.kundenName = "Test Kunde";
        viewModel2.kundenViewModel = kundenViewModel2;
        workedTimeViewModels.add(viewModel2);
        WorkedTime expectedWorkedTime = new WorkedTime()
                .setBeschreibung("Beschreibung")
                .setStartTime(LocalDateTime.of(2020,1,1,20,15,0,0))
                .setEndTime(LocalDateTime.of(2020,1,1,22,15,0,0))
                .setKunde(new Kunde().setKundenName("Test Kunde"))
                .setProjekt(new Projekt().setProjektName("Test Projekt"));
        expectedWorkedTime.setCreatedBy(validUser);

        saveUsersTime.save(workedTimeViewModels, validUsername);

        verify(workedTimeGatewayMock, times(1)).saveAll(captor.capture());
        ArrayList<WorkedTime> workedTimes = captor.getValue();
        assertEquals(2, workedTimes.size());
        assertThat(workedTimes.get(0)).usingRecursiveComparison().isEqualTo(expectedWorkedTime);
        assertEquals(workedTimes.get(0).getKunde(), workedTimes.get(1).getKunde());
        assertEquals(workedTimes.get(0).getProjekt(), workedTimes.get(1).getProjekt());
    }
}