package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.KundenAdd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KundenAddUseCaseTest {

    private KundenAdd kundenAdd;

    @BeforeEach
    void setUp() {
        kundenAdd = new KundenAddUseCase();
    }

    @Test
    public void add_givenEmptyArray() {

        kundenAdd.add(new ArrayList<>());
    }
}