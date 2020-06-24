package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteAdd;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.ProjektGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjekteAddUseCase implements ProjekteAdd {

    private final UserGateway userGateway;
    private final ProjektGateway projektGateway;
    private User user;

    public ProjekteAddUseCase(UserGateway userGateway, ProjektGateway projektGateway) {
        this.userGateway = userGateway;
        this.projektGateway = projektGateway;
    }

    @Override
    public void add(ArrayList<ProjektViewModel> projektViewModels, String username) throws UserDoesNotExistException {
        user = userGateway.getUserByUsername(username);
        ArrayList<Projekt> projekte = getProjekte(projektViewModels);
        if (projekte.size() > 0)
            projektGateway.addProjekte(projekte);
    }

    private ArrayList<Projekt> getProjekte(ArrayList<ProjektViewModel> projektViewModels) {
        ArrayList<Projekt> projekte = new ArrayList<>();
        for (ProjektViewModel viewModel : projektViewModels) {
            projekte.add(getProjektViewModelFromProjekt(viewModel));
        }
        return projekte;
    }

    private Projekt getProjektViewModelFromProjekt(ProjektViewModel viewModel) {
        Projekt projekt = new Projekt();
        projekt.setCreatedBy(user);
        projekt.setProjektName(viewModel.projektName);
        return projekt;
    }
}
