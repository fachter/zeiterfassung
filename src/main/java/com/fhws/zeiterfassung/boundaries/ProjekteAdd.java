package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.models.ProjektViewModel;

import java.util.ArrayList;

public interface ProjekteAdd {

    void add(ArrayList<ProjektViewModel> projektViewModels, String username) throws UserDoesNotExistException;
}
