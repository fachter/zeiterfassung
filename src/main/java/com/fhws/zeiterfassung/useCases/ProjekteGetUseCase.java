package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteGet;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjekteGetUseCase implements ProjekteGet {

    @Override
    public ArrayList<ProjektViewModel> get(String username) {
        return null;
    }
}
