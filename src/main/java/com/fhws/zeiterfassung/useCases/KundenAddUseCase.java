package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.KundenAdd;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.KundeNotFoundException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.KundeGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.models.KundenViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class KundenAddUseCase implements KundenAdd {

    private final KundeGateway kundeGateway;
    private final UserGateway userGateway;
    private ArrayList<Kunde> kundenToAdd;
    private User user;
    private ArrayList<Kunde> kundenFromDb;
    private ArrayList<Kunde> kundenToRemove;

    @Autowired
    public KundenAddUseCase(KundeGateway kundeGateway, UserGateway userGateway) {
        this.kundeGateway = kundeGateway;
        this.userGateway = userGateway;
    }

    @Override
    public void add(ArrayList<KundenViewModel> kundenViewModels,
                    String username) throws InvalidDataException, UserDoesNotExistException {
        user = userGateway.getUserByUsername(username);
        kundenToAdd = new ArrayList<>();
        kundenToRemove = new ArrayList<>();
        kundenFromDb = kundeGateway.getAllByUser(user);
        addKundenToPersistFromViewModel(kundenViewModels);
        if (kundenToAdd.size() > 0)
            kundeGateway.addKunden(kundenToAdd);
        if (kundenToRemove.size() > 0)
            kundeGateway.removeKunden(kundenToRemove);
    }

    private void addKundenToPersistFromViewModel(ArrayList<KundenViewModel> kundenViewModels) throws InvalidDataException {
        for (KundenViewModel viewModel : kundenViewModels) {
            if (viewModel.kundenName == null || viewModel.kundenName.equals(""))
                throw new InvalidDataException();
            if (!viewModel.deleted)
                kundenToAdd.add(getKundeFromViewModel(viewModel));
            else
                removeKunde(viewModel);
        }
    }

    private void removeKunde(KundenViewModel viewModel) {
        try {
            kundenToRemove.add(getKundeFromDb(viewModel));
        } catch (KundeNotFoundException ignored) {}
    }

    private Kunde getKundeFromViewModel(KundenViewModel viewModel) {
        try {
            return getKundeFromDb(viewModel);
        } catch (KundeNotFoundException e) {
            Kunde kunde = new Kunde();
            kunde.setKundenName(viewModel.kundenName);
            kunde.setCreatedBy(user);
            return kunde;
        }
    }

    private Kunde getKundeFromDb(KundenViewModel viewModel) throws KundeNotFoundException {
        if (viewModel.id != null)
            for (Kunde kunde : kundenFromDb)
                if (kunde.getId().equals(viewModel.id))
                    return kunde.setKundenName(viewModel.kundenName);
        throw new KundeNotFoundException();
    }
}
