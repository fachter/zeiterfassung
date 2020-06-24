package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.models.ProjektViewModel;

import java.util.ArrayList;

public interface ProjekteGet {

    ArrayList<ProjektViewModel> get(String username);
}
