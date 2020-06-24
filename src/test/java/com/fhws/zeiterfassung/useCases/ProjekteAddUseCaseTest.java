package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteAdd;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.ProjektGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
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
}