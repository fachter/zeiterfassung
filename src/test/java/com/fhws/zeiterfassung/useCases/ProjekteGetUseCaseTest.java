package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteGet;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.ProjektGateway;
import com.fhws.zeiterfassung.gateways.ProjektGatewayImpl;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjekteGetUseCaseTest {

    @Mock private UserGateway userGatewayMock;
    @Mock private ProjektGateway projektGatewayMock;
    private ProjekteGet projekteGet;
    private final String validUsername = "validUsername";
    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername(validUsername);
        projekteGet = new ProjekteGetUseCase(userGatewayMock, projektGatewayMock);
    }

    @Test
    public void givenInvalidUsername() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        Assertions.assertThrows(UserDoesNotExistException.class, () -> projekteGet.get("invalid"));
    }

    @Test
    public void givenValidUserHasNoProjekte() throws Exception {
        prepareUserGatewayMock();
        when(projektGatewayMock.getAllByUser(validUser)).thenReturn(new ArrayList<>());

        ArrayList<ProjektViewModel> projekte = projekteGet.get(validUsername);

        assertEquals(0, projekte.size());
    }

    private void prepareUserGatewayMock() throws UserDoesNotExistException {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
    }

    @Test
    public void givenValidUserHasOneProjekt() throws Exception {
        prepareUserGatewayMock();
        ArrayList<Projekt> projekte = new ArrayList<>();
        Projekt projekt = new Projekt().setProjektName("Projekt");
        projekt.setCreatedBy(validUser);
        projekt.setId(1L);
        projekte.add(projekt);
        ProjektViewModel expectedProjekt = new ProjektViewModel();
        expectedProjekt.id = 1L;
        expectedProjekt.projektName = "Projekt";
        when(projektGatewayMock.getAllByUser(validUser)).thenReturn(projekte);

        ArrayList<ProjektViewModel> projektViewModels = projekteGet.get(validUsername);

        assertEquals(1, projektViewModels.size());
        assertThat(projektViewModels.get(0)).usingRecursiveComparison().isEqualTo(expectedProjekt);
    }

    @Test
    public void givenValidUserHasProjekte() throws Exception {
        prepareUserGatewayMock();
        ArrayList<Projekt> projekte = new ArrayList<>();
        Projekt projekt1 = new Projekt().setProjektName("Projekt1");
        projekt1.setId(1L);
        Projekt projekt2 = new Projekt().setProjektName("Projekt2");
        projekt2.setId(1L);
        Projekt projekt3 = new Projekt().setProjektName("Projekt3");
        projekt3.setId(1L);
        projekte.add(projekt1);
        projekte.add(projekt2);
        projekte.add(projekt3);
        ProjektViewModel expectedProjekt1 = new ProjektViewModel();
        expectedProjekt1.id = 1L;
        expectedProjekt1.projektName = "Projekt1";
        ProjektViewModel expectedProjekt2 = new ProjektViewModel();
        expectedProjekt2.id = 1L;
        expectedProjekt2.projektName = "Projekt2";
        ProjektViewModel expectedProjekt3 = new ProjektViewModel();
        expectedProjekt3.id = 1L;
        expectedProjekt3.projektName = "Projekt3";
        when(projektGatewayMock.getAllByUser(validUser)).thenReturn(projekte);

        ArrayList<ProjektViewModel> projektViewModels = projekteGet.get(validUsername);

        assertEquals(3, projektViewModels.size());
        assertThat(projektViewModels.get(0)).usingRecursiveComparison().isEqualTo(expectedProjekt1);
        assertThat(projektViewModels.get(1)).usingRecursiveComparison().isEqualTo(expectedProjekt2);
        assertThat(projektViewModels.get(2)).usingRecursiveComparison().isEqualTo(expectedProjekt3);
    }
}