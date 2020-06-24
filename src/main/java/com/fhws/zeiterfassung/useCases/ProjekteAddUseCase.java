package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteAdd;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjekteAddUseCase implements ProjekteAdd {

    @Override
    public void add(ArrayList<ProjektViewModel> projektViewModels, String username) {

    }
}
