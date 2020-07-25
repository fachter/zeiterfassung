package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.viewModels.ProjektViewModel;

import java.util.ArrayList;

public interface ProjekteAdd {

    void add(ArrayList<ProjektViewModel> projektViewModels, String username) throws UserDoesNotExistException, InvalidDataException;
}
