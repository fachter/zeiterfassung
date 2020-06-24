package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteGet;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.ProjektGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjekteGetUseCase implements ProjekteGet {

    private final UserGateway userGateway;
    private final ProjektGateway projektGateway;

    public ProjekteGetUseCase(UserGateway userGateway, ProjektGateway projektGateway) {
        this.userGateway = userGateway;
        this.projektGateway = projektGateway;
    }

    @Override
    public ArrayList<ProjektViewModel> get(String username) throws UserDoesNotExistException {
        User user = userGateway.getUserByUsername(username);
        return getProjektViewModels(projektGateway.getAllByUser(user));
    }

    private ArrayList<ProjektViewModel> getProjektViewModels(ArrayList<Projekt> projekte) {
        ArrayList<ProjektViewModel> projektViewModels = new ArrayList<>();
        for (Projekt projekt : projekte) {
            projektViewModels.add(getProjektViewModelFromProjekt(projekt));
        }
        return projektViewModels;
    }

    private ProjektViewModel getProjektViewModelFromProjekt(Projekt projekt) {
        ProjektViewModel viewModel = new ProjektViewModel();
        viewModel.id = projekt.getId();
        viewModel.projektName = projekt.getProjektName();
        return viewModel;
    }
}
