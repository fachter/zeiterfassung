package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.viewModels.KundenViewModel;

import java.util.ArrayList;

public interface KundenGet {

    ArrayList<KundenViewModel> get(String username) throws UserDoesNotExistException;
}
