package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.GetUsersWorkedTime;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.models.KundenViewModel;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUsersWorkedTimeUseCaseTest {

    private GetUsersWorkedTime getUsersWorkedTime;
    @Mock private UserGateway userGatewayMock;
    @Mock private WorkedTimeGateway workedTimeGatewayMock;
    private final String validUsername = "validUsername";
    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername(validUsername);
        getUsersWorkedTime = new GetUsersWorkedTimeUseCase(userGatewayMock, workedTimeGatewayMock);
    }

    @Test
    public void givenUserDoesNotExist() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        Assertions.assertThrows(UserDoesNotExistException.class, () -> getUsersWorkedTime.get("invalid"));
    }

    @Test
    public void givenUserHasNoDbEntries() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        when(workedTimeGatewayMock.getAllByUser(validUser)).thenReturn(new ArrayList<>());

        ArrayList<WorkedTimeViewModel> workedTimeViewModels = getUsersWorkedTime.get(validUsername);

        Assertions.assertEquals(0, workedTimeViewModels.size());
    }

    @Test
    public void givenUserHasOneDbEntries() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<WorkedTime> times = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.of(2020,1,1,20,20);
        LocalDateTime endTime = LocalDateTime.of(2020,1,1,21,0);
        Kunde kunde = new Kunde().setKundenName("Test Kunde");
        kunde.setId(123L);
        Projekt projekt = new Projekt().setProjektName("Test Projekt");
        projekt.setId(321L);
        WorkedTime time = new WorkedTime()
                .setBeschreibung("Beschreibung")
                .setBreakInMinutes(30)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setKunde(kunde)
                .setProjekt(projekt);
        time.setCreatedBy(validUser);
        time.setId(1L);
        times.add(time);
        WorkedTimeViewModel expectedViewModel = new WorkedTimeViewModel();
        expectedViewModel.id = 1L;
        expectedViewModel.beschreibung = "Beschreibung";
        expectedViewModel.startTime = LocalDateTime.of(2020,1,1,20,20);
        expectedViewModel.endTime = LocalDateTime.of(2020,1,1,21,0);
        expectedViewModel.breakInMinutes = 30;
        KundenViewModel kundenViewModel = new KundenViewModel();
        kundenViewModel.id = 123L;
        kundenViewModel.kundenName = "Test Kunde";
        expectedViewModel.kundenViewModel = kundenViewModel;
        ProjektViewModel projektViewModel = new ProjektViewModel();
        projektViewModel.id = 321L;
        projektViewModel.projektName = "Test Projekt";
        expectedViewModel.projektViewModel = projektViewModel;
        when(workedTimeGatewayMock.getAllByUser(validUser)).thenReturn(times);

        ArrayList<WorkedTimeViewModel> workedTimeViewModels = getUsersWorkedTime.get(validUsername);

        Assertions.assertEquals(1, workedTimeViewModels.size());
        assertThat(workedTimeViewModels.get(0)).usingRecursiveComparison().isEqualTo(expectedViewModel);
    }

    @Test
    public void givenUserHasOneDbEntriesWithoutKundeAndProjekt() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<WorkedTime> times = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.of(2020,1,1,20,20);
        LocalDateTime endTime = LocalDateTime.of(2020,1,1,21,0);
        WorkedTime time = new WorkedTime()
                .setBeschreibung("Beschreibung")
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setKunde(null)
                .setProjekt(null);
        time.setCreatedBy(validUser);
        time.setId(1L);
        times.add(time);
        WorkedTimeViewModel expectedViewModel = new WorkedTimeViewModel();
        expectedViewModel.id = 1L;
        expectedViewModel.beschreibung = "Beschreibung";
        expectedViewModel.startTime = LocalDateTime.of(2020,1,1,20,20);
        expectedViewModel.endTime = LocalDateTime.of(2020,1,1,21,0);
        expectedViewModel.projektViewModel = null;
        expectedViewModel.kundenViewModel = null;
        when(workedTimeGatewayMock.getAllByUser(validUser)).thenReturn(times);

        ArrayList<WorkedTimeViewModel> workedTimeViewModels = getUsersWorkedTime.get(validUsername);

        Assertions.assertEquals(1, workedTimeViewModels.size());
        assertThat(workedTimeViewModels.get(0)).usingRecursiveComparison().isEqualTo(expectedViewModel);
    }

    @Test
    public void givenUserHasMultipleDbEntries() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<WorkedTime> times = new ArrayList<>();
        WorkedTime time1 = new WorkedTime()
                .setBeschreibung("Beschreibung 1")
                .setStartTime(LocalDateTime.of(2018,1,2,20,20))
                .setEndTime(LocalDateTime.of(2018,1,2,21,0))
                .setKunde(null)
                .setProjekt(null);
        time1.setCreatedBy(validUser);
        time1.setId(1L);
        times.add(time1);
        WorkedTimeViewModel expectedViewModel1 = new WorkedTimeViewModel();
        expectedViewModel1.id = 1L;
        expectedViewModel1.beschreibung = "Beschreibung 1";
        expectedViewModel1.startTime = LocalDateTime.of(2018,1,2,20,20);
        expectedViewModel1.endTime = LocalDateTime.of(2018,1,2,21,0);
        expectedViewModel1.projektViewModel = null;
        expectedViewModel1.kundenViewModel = null;

        Projekt projekt = new Projekt().setProjektName("Test Projekt");
        projekt.setId(123L);
        WorkedTime time2 = new WorkedTime()
                .setBeschreibung("Beschreibung 2")
                .setStartTime(LocalDateTime.of(2019,1,1,20,20))
                .setEndTime(LocalDateTime.of(2019,1,1,21,0))
                .setKunde(null)
                .setProjekt(projekt);
        time2.setCreatedBy(validUser);
        time2.setId(1L);
        times.add(time2);
        WorkedTimeViewModel expectedViewModel2 = new WorkedTimeViewModel();
        expectedViewModel2.id = 1L;
        expectedViewModel2.beschreibung = "Beschreibung 2";
        expectedViewModel2.startTime = LocalDateTime.of(2019,1,1,20,20);
        expectedViewModel2.endTime = LocalDateTime.of(2019,1,1,21,0);
        ProjektViewModel projektViewModel = new ProjektViewModel();
        projektViewModel.id = 123L;
        projektViewModel.projektName = "Test Projekt";
        expectedViewModel2.projektViewModel = projektViewModel;
        expectedViewModel2.kundenViewModel = null;

        Kunde kunde = new Kunde().setKundenName("Test Kunde");
        kunde.setId(321L);
        WorkedTime time3 = new WorkedTime()
                .setBeschreibung("Beschreibung 3")
                .setStartTime(LocalDateTime.of(2020,1,1,20,20))
                .setEndTime(LocalDateTime.of(2020,1,1,21,0))
                .setKunde(kunde)
                .setProjekt(null);
        time3.setCreatedBy(validUser);
        time3.setId(1L);
        times.add(time3);
        WorkedTimeViewModel expectedViewModel3 = new WorkedTimeViewModel();
        expectedViewModel3.id = 1L;
        expectedViewModel3.beschreibung = "Beschreibung 3";
        expectedViewModel3.startTime = LocalDateTime.of(2020,1,1,20,20);
        expectedViewModel3.endTime = LocalDateTime.of(2020,1,1,21,0);
        expectedViewModel3.projektViewModel = null;
        KundenViewModel kundenViewModel = new KundenViewModel();
        kundenViewModel.id = 321L;
        kundenViewModel.kundenName = "Test Kunde";
        expectedViewModel3.kundenViewModel = kundenViewModel;
        when(workedTimeGatewayMock.getAllByUser(validUser)).thenReturn(times);

        ArrayList<WorkedTimeViewModel> workedTimeViewModels = getUsersWorkedTime.get(validUsername);

        Assertions.assertEquals(3, workedTimeViewModels.size());
        assertThat(workedTimeViewModels.get(0)).usingRecursiveComparison().isEqualTo(expectedViewModel1);
        assertThat(workedTimeViewModels.get(1)).usingRecursiveComparison().isEqualTo(expectedViewModel2);
        assertThat(workedTimeViewModels.get(2)).usingRecursiveComparison().isEqualTo(expectedViewModel3);
    }
}