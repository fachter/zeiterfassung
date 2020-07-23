package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.ProjekteAdd;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.ProjektNotFoundException;
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
    private ArrayList<Projekt> projekteToRemove;
    private ArrayList<Projekt> projekteFromDb;

    public ProjekteAddUseCase(UserGateway userGateway, ProjektGateway projektGateway) {
        this.userGateway = userGateway;
        this.projektGateway = projektGateway;
    }

    @Override
    public void add(ArrayList<ProjektViewModel> projektViewModels, String username) throws UserDoesNotExistException, InvalidDataException {
        user = userGateway.getUserByUsername(username);
        projekteToRemove = new ArrayList<>();
        projekteFromDb = projektGateway.getAllByUser(user);
        ArrayList<Projekt> projekte = getProjekte(projektViewModels);
        if (projekte.size() > 0)
            projektGateway.addProjekte(projekte);
        if (projekteToRemove.size() > 0)
            projektGateway.removeProjekte(projekteToRemove);
    }

    private ArrayList<Projekt> getProjekte(ArrayList<ProjektViewModel> projektViewModels) throws InvalidDataException {
        ArrayList<Projekt> projekte = new ArrayList<>();
        for (ProjektViewModel viewModel : projektViewModels) {
            if (viewModel.projektName == null || viewModel.projektName.equals(""))
                throw new InvalidDataException();
            if (!viewModel.deleted)
                projekte.add(getProjektViewModelFromProjekt(viewModel));
            else
                removeProjekt(viewModel);
        }
        return projekte;
    }

    private void removeProjekt(ProjektViewModel viewModel) {
        try {
            projekteToRemove.add(getProjektFromDb(viewModel));
        } catch (ProjektNotFoundException ignored) {
        }
    }

    private Projekt getProjektViewModelFromProjekt(ProjektViewModel viewModel) {
        try {
            return getProjektFromDb(viewModel);
        } catch (ProjektNotFoundException e) {
            Projekt projekt = new Projekt();
            projekt.setCreatedBy(user);
            projekt.setProjektName(viewModel.projektName);
            return projekt;
        }
    }

    private Projekt getProjektFromDb(ProjektViewModel viewModel) throws ProjektNotFoundException {
        for (Projekt projekt : projekteFromDb) {
            if (projekt.getId().equals(viewModel.id))
                return projekt.setProjektName(viewModel.projektName);
        }
        throw new ProjektNotFoundException();
    }
}
