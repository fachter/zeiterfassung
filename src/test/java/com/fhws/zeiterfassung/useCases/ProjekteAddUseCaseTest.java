package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteAdd;
import com.fhws.zeiterfassung.gateways.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjekteAddUseCaseTest {

    private ProjekteAdd projekteAdd;
    @Mock private UserGateway userGatewayMock;

    @BeforeEach
    void setUp() {
    }
}