package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.KundenAdd;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.KundeGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.KundenViewModel;
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
class KundenAddUseCaseTest {

    @Mock private KundeGateway kundeGatewayMock;
    @Mock private UserGateway userGatewayMock;
    @Captor ArgumentCaptor<ArrayList<Kunde>> captor;

    private KundenAdd kundenAdd;
    private User validUser;
    private String validUsername;

    @BeforeEach
    void setUp() {
        validUsername = "fachter";
        validUser = new User();
        validUser.setFullName("Felix");
        validUser.setUsername(validUsername);
        kundenAdd = new KundenAddUseCase(kundeGatewayMock, userGatewayMock);
    }

    @Test
    public void add_givenInvalidUsername() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        assertThrows(UserDoesNotExistException.class, () -> kundenAdd.add(new ArrayList<>(), "invalid"));
    }

    @Test
    public void add_givenEmptyArray() throws Exception {
        kundenAdd.add(new ArrayList<>(), validUsername);
        verify(kundeGatewayMock, times(0)).addKunden(captor.capture());
    }

    @Test
    public void add_givenArrayWithKundeWithNameIsNull() {
        ArrayList<KundenViewModel> kundenViewModels = new ArrayList<>();
        kundenViewModels.add(new KundenViewModel());

        assertThrows(InvalidDataException.class,
                () -> kundenAdd.add(kundenViewModels, validUsername));

    }

    @Test
    public void add_givenArrayWithKundeWithNameIsEmptyString() {
        ArrayList<KundenViewModel> kundenViewModels = new ArrayList<>();
        KundenViewModel k = new KundenViewModel();
        k.kundenName = "";
        kundenViewModels.add(k);

        assertThrows(InvalidDataException.class,
                () -> kundenAdd.add(kundenViewModels, validUsername));
    }

    @Test
    public void add_givenArrayWithOneValidKunde() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<KundenViewModel> kundenViewModels = new ArrayList<>();
        KundenViewModel k1 = new KundenViewModel();
        k1.id = 0;
        k1.kundenName = "Test Kundenname";
        kundenViewModels.add(k1);
        Kunde expectedKunde = new Kunde().setKundenName("Test Kundenname");
        expectedKunde.setCreatedBy(validUser);

        kundenAdd.add(kundenViewModels, validUsername);

        verify(kundeGatewayMock, times(1)).addKunden(captor.capture());
        ArrayList<Kunde> persistedKunden = captor.getValue();
        assertEquals(1, persistedKunden.size());
        assertThat(persistedKunden.get(0)).usingRecursiveComparison().isEqualTo(expectedKunde);
    }

    @Test
    public void add_givenArrayWithMoreValidKundenWithIds() throws Exception {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
        ArrayList<KundenViewModel> kundenViewModels = new ArrayList<>();
        KundenViewModel k1 = new KundenViewModel();
        k1.id = 1;
        k1.kundenName = "Kunde 1";
        KundenViewModel k2 = new KundenViewModel();
        k2.id = 2;
        k2.kundenName = "Kunde 2";
        KundenViewModel k3 = new KundenViewModel();
        k3.id = 3;
        k3.kundenName = "Kunde 3";
        KundenViewModel k4 = new KundenViewModel();
        k4.id = 4;
        k4.kundenName = "Kunde 4";
        kundenViewModels.add(k1);
        kundenViewModels.add(k2);
        kundenViewModels.add(k3);
        kundenViewModels.add(k4);
        Kunde expectedKunde1 = new Kunde().setKundenName("Kunde 1");
        expectedKunde1.setCreatedBy(validUser);
        Kunde expectedKunde2 = new Kunde().setKundenName("Kunde 2");
        expectedKunde2.setCreatedBy(validUser);
        Kunde expectedKunde3 = new Kunde().setKundenName("Kunde 3");
        expectedKunde3.setCreatedBy(validUser);
        Kunde expectedKunde4 = new Kunde().setKundenName("Kunde 4");
        expectedKunde4.setCreatedBy(validUser);

        kundenAdd.add(kundenViewModels, validUsername);

        verify(kundeGatewayMock, times(1)).addKunden(captor.capture());
        ArrayList<Kunde> persistedKunden = captor.getValue();
        assertEquals(4, persistedKunden.size());
        assertThat(persistedKunden.get(0)).usingRecursiveComparison().isEqualTo(expectedKunde1);
        assertThat(persistedKunden.get(1)).usingRecursiveComparison().isEqualTo(expectedKunde2);
        assertThat(persistedKunden.get(2)).usingRecursiveComparison().isEqualTo(expectedKunde3);
        assertThat(persistedKunden.get(3)).usingRecursiveComparison().isEqualTo(expectedKunde4);
    }
}