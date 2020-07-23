package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteAdd;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.ProjektGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.KundenViewModel;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjekteAddUseCaseTest {

    private ProjekteAdd projekteAdd;
    @Mock private UserGateway userGatewayMock;
    @Mock private ProjektGateway projektGatewayMock;
    private final String validUsername = "validUsername";
    private User validUser;
    @Captor private ArgumentCaptor<ArrayList<Projekt>> captor;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername(validUsername);
        projekteAdd = new ProjekteAddUseCase(userGatewayMock, projektGatewayMock);
    }

    @Test
    public void givenInvalidUsername() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        Assertions.assertThrows(UserDoesNotExistException.class, () -> projekteAdd.add(new ArrayList<>(), "invalid"));
    }

    @Test
    public void givenEmptyList() throws Exception {
        prepareUserGatewayMock();
        projekteAdd.add(new ArrayList<>(), validUsername);
        verify(projektGatewayMock, times(0)).addProjekte(captor.capture());
    }

    private void prepareUserGatewayMock() throws UserDoesNotExistException {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
    }

    @Test
    public void givenArrayWithProjektWithNameIsNull() throws Exception {
        ArrayList<ProjektViewModel> projektViewModels = new ArrayList<>();
        projektViewModels.add(new ProjektViewModel());

        assertThrows(InvalidDataException.class,
                () -> projekteAdd.add(projektViewModels, validUsername));
    }

    @Test
    public void givenArrayWithProjektWithNameIsEmptyString() throws Exception {
        ArrayList<ProjektViewModel> projektViewModels = new ArrayList<>();
        ProjektViewModel projektViewModel = new ProjektViewModel();
        projektViewModel.projektName = "";
        projektViewModels.add(projektViewModel);

        assertThrows(InvalidDataException.class,
                () -> projekteAdd.add(projektViewModels, validUsername));
    }

    @Test
    public void givenOneItemInArrayThenSaveToDb() throws Exception {
        prepareUserGatewayMock();
        ArrayList<ProjektViewModel> projekte = new ArrayList<>();
        ProjektViewModel projekt = new ProjektViewModel();
        projekt.id = 123L;
        projekt.projektName = "Projekt";
        projekte.add(projekt);
        Projekt expectedProjekt = new Projekt().setProjektName("Projekt");
        expectedProjekt.setCreatedBy(validUser);

        projekteAdd.add(projekte, validUsername);

        verify(projektGatewayMock, times(1)).addProjekte(captor.capture());
        ArrayList<Projekt> actualProjects = captor.getValue();
        assertEquals(1, actualProjects.size());
        assertThat(actualProjects.get(0)).usingRecursiveComparison().isEqualTo(expectedProjekt);
    }

    @Test
    public void add_givenDeleteNonPersitedProjekt() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<ProjektViewModel> projektViewModels = new ArrayList<>();
        ProjektViewModel p1 = new ProjektViewModel();
        p1.projektName = "Does not matter";
        p1.deleted = true;
        projektViewModels.add(p1);

        projekteAdd.add(projektViewModels, validUsername);

        verify(projektGatewayMock, times(0)).addProjekte(captor.capture());
    }

    @Test
    public void add_givenChangeExistingProjekt() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<Projekt> projekte = new ArrayList<>();
        Projekt projekt = new Projekt().setProjektName("Projekt");
        projekt.setId(123L);
        projekte.add(projekt);
        when(projektGatewayMock.getAllByUser(validUser)).thenReturn(projekte);
        ArrayList<ProjektViewModel> projektViewModels = new ArrayList<>();
        ProjektViewModel p1 = new ProjektViewModel();
        p1.projektName = "new Name";
        p1.id = 123L;
        projektViewModels.add(p1);

        projekteAdd.add(projektViewModels, validUsername);

        verify(projektGatewayMock, times(1)).addProjekte(captor.capture());
        ArrayList<Projekt> persistedProjekte = captor.getValue();
        assertEquals(1, persistedProjekte.size());
        assertEquals(projekt, persistedProjekte.get(0));
        assertEquals("new Name", persistedProjekte.get(0).getProjektName());
    }

    @Test
    public void add_givenDeleteExistingProjekt() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<Projekt> projekte = new ArrayList<>();
        Projekt projekt = new Projekt().setProjektName("Projekt");
        projekt.setId(123L);
        projekte.add(projekt);
        when(projektGatewayMock.getAllByUser(validUser)).thenReturn(projekte);
        ArrayList<ProjektViewModel> projektViewModels = new ArrayList<>();
        ProjektViewModel p1 = new ProjektViewModel();
        p1.projektName = "Projekt";
        p1.id = 123L;
        p1.deleted = true;
        projektViewModels.add(p1);

        projekteAdd.add(projektViewModels, validUsername);

        verify(projektGatewayMock, times(0)).addProjekte(any());
        verify(projektGatewayMock, times(1)).removeProjekte(captor.capture());
        ArrayList<Projekt> projekteToRemove = captor.getValue();
        assertEquals(projekt, projekteToRemove.get(0));
    }
}