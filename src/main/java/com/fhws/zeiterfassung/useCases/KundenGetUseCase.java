package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.useCases.KundenGet;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.boundaries.gateways.KundeGateway;
import com.fhws.zeiterfassung.boundaries.gateways.UserGateway;
import com.fhws.zeiterfassung.viewModels.KundenViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class KundenGetUseCase implements KundenGet {
    private final KundeGateway kundeGateway;
    private final UserGateway userGateway;

    @Autowired
    public KundenGetUseCase(KundeGateway kundeGateway, UserGateway userGateway) {
        this.kundeGateway = kundeGateway;
        this.userGateway = userGateway;
    }

    @Override
    public ArrayList<KundenViewModel> get(String username) throws UserDoesNotExistException {
        User user = userGateway.getUserByUsername(username);
        return getKunden(kundeGateway.getAllByUser(user));
    }

    private ArrayList<KundenViewModel> getKunden(ArrayList<Kunde> allKundenForUser) {
        ArrayList<KundenViewModel> kunden = new ArrayList<>();
        for (Kunde kunde : allKundenForUser) {
            kunden.add(getKundenViewModelFromKunde(kunde));
        }
        return kunden;
    }

    private KundenViewModel getKundenViewModelFromKunde(Kunde kunde) {
        KundenViewModel kundeVm = new KundenViewModel();
        kundeVm.id = kunde.getId();
        kundeVm.kundenName = kunde.getKundenName();
        return kundeVm;
    }
}
