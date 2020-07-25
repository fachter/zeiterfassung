package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.viewModels.ProjektViewModel;

import java.util.ArrayList;

public interface ProjekteGet {

    ArrayList<ProjektViewModel> get(String username) throws UserDoesNotExistException;
}
