package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.KundenGet;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.KundeGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.KundenViewModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KundenGetUseCaseTest {

    private KundenGet kundenGet;
    @Mock private KundeGateway kundeGatewayMock;
    @Mock private UserGateway userGatewayMock;
    private User validUser;
    private final String validUsername = "validUsername";

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername(validUsername);
        validUser.setFullName("Full name");
        kundenGet = new KundenGetUseCase(kundeGatewayMock, userGatewayMock);
    }

    @Test
    public void givenUserDoesNotExist() throws Exception {
        when(userGatewayMock.getUserByUsername("invalid")).thenThrow(UserDoesNotExistException.class);
        Assertions.assertThrows(UserDoesNotExistException.class,() ->kundenGet.get("invalid"));
    }

    @Test
    public void givenValidUserHasNoKunden() throws Exception {
        prepareUserGatewayMock();
        when(kundeGatewayMock.getAllByUser(validUser)).thenReturn(new ArrayList<>());

        ArrayList<KundenViewModel> kunden = kundenGet.get(validUsername);

        assertEquals(0, kunden.size());
    }

    @Test
    public void givenValidUserHasOneKunde() throws Exception {
        prepareUserGatewayMock();
        ArrayList<Kunde> kunden = new ArrayList<>();
        Kunde kunde = new Kunde();
        kunde.setId(1L);
        kunde.setCreatedBy(validUser);
        kunde.setKundenName("Test Kunde");
        kunden.add(kunde);
        KundenViewModel expectedKunde = new KundenViewModel();
        expectedKunde.id = 1L;
        expectedKunde.kundenName = "Test Kunde";
        when(kundeGatewayMock.getAllByUser(validUser)).thenReturn(kunden);

        ArrayList<KundenViewModel> kundenViewModels = kundenGet.get(validUsername);

        assertEquals(1, kundenViewModels.size());
        assertThat(kundenViewModels.get(0)).usingRecursiveComparison().isEqualTo(expectedKunde);
    }

    @Test
    public void givenValidUserHasMoreKunden() throws Exception {
        prepareUserGatewayMock();
        ArrayList<Kunde> kunden = new ArrayList<>();
        Kunde kunde1 = new Kunde();
        kunde1.setId(1L);
        kunde1.setCreatedBy(validUser);
        kunde1.setKundenName("Kunde 1");
        Kunde kunde2 = new Kunde();
        kunde2.setId(2L);
        kunde2.setCreatedBy(validUser);
        kunde2.setKundenName("Kunde 2");
        Kunde kunde3 = new Kunde();
        kunde3.setId(3L);
        kunde3.setCreatedBy(validUser);
        kunde3.setKundenName("Kunde 3");
        kunden.add(kunde1);
        kunden.add(kunde2);
        kunden.add(kunde3);
        KundenViewModel expectedKunde1 = new KundenViewModel();
        expectedKunde1.id = 1L;
        expectedKunde1.kundenName = "Kunde 1";
        KundenViewModel expectedKunde2 = new KundenViewModel();
        expectedKunde2.id = 2L;
        expectedKunde2.kundenName = "Kunde 2";
        KundenViewModel expectedKunde3 = new KundenViewModel();
        expectedKunde3.id = 3L;
        expectedKunde3.kundenName = "Kunde 3";
        when(kundeGatewayMock.getAllByUser(validUser)).thenReturn(kunden);

        ArrayList<KundenViewModel> kundenViewModels = kundenGet.get(validUsername);

        assertEquals(3, kundenViewModels.size());
        assertThat(kundenViewModels.get(0)).usingRecursiveComparison().isEqualTo(expectedKunde1);
        assertThat(kundenViewModels.get(1)).usingRecursiveComparison().isEqualTo(expectedKunde2);
        assertThat(kundenViewModels.get(2)).usingRecursiveComparison().isEqualTo(expectedKunde3);
    }

    private void prepareUserGatewayMock() throws UserDoesNotExistException {
        when(userGatewayMock.getUserByUsername(validUsername)).thenReturn(validUser);
    }

}