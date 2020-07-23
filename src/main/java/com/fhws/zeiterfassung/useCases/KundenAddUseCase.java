package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.KundenAdd;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
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
    private final ArrayList<Kunde> kundenToAdd = new ArrayList<>();
    private User user;

    @Autowired
    public KundenAddUseCase(KundeGateway kundeGateway, UserGateway userGateway) {
        this.kundeGateway = kundeGateway;
        this.userGateway = userGateway;
    }

    @Override
    public void add(ArrayList<KundenViewModel> kundenViewModels,
                    String username) throws InvalidDataException, UserDoesNotExistException {
        user = userGateway.getUserByUsername(username);
        addKundenToPersistFromViewModel(kundenViewModels);
        if (kundenToAdd.size() > 0 )
            kundeGateway.addKunden(kundenToAdd);
    }

    private void addKundenToPersistFromViewModel(ArrayList<KundenViewModel> kundenViewModels) throws InvalidDataException {
        for (KundenViewModel viewModel : kundenViewModels) {
            if (viewModel.kundenName == null || viewModel.kundenName.equals(""))
                throw new InvalidDataException();
            kundenToAdd.add(getKundeFromViewModel(viewModel));
        }
    }

    private Kunde getKundeFromViewModel(KundenViewModel viewModel) {
        Kunde kunde = new Kunde();
        kunde.setKundenName(viewModel.kundenName);
        kunde.setCreatedBy(user);
        return kunde;
    }
}
