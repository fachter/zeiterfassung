package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.useCases.SaveUsersTime;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.*;
import com.fhws.zeiterfassung.boundaries.gateways.KundeGateway;
import com.fhws.zeiterfassung.boundaries.gateways.ProjektGateway;
import com.fhws.zeiterfassung.boundaries.gateways.UserGateway;
import com.fhws.zeiterfassung.boundaries.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.viewModels.KundenViewModel;
import com.fhws.zeiterfassung.viewModels.ProjektViewModel;
import com.fhws.zeiterfassung.viewModels.WorkedTimeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class SaveUsersTimeUseCase implements SaveUsersTime {

    private final UserGateway userGateway;
    private final WorkedTimeGateway workedTimeGateway;
    private final KundeGateway kundeGateway;
    private final ProjektGateway projektGateway;
    private ArrayList<Kunde> kundenFromDb;
    private ArrayList<Projekt> projekteFromDb;
    private ArrayList<Kunde> newKunden;
    private ArrayList<Projekt> newProjekte;
    private ArrayList<WorkedTime> workedTimesToPersist;
    private User loggedInUser;

    @Autowired
    public SaveUsersTimeUseCase(UserGateway userGateway,
                                WorkedTimeGateway workedTimeGateway,
                                KundeGateway kundeGateway,
                                ProjektGateway projektGateway) {
        this.userGateway = userGateway;
        this.workedTimeGateway = workedTimeGateway;
        this.kundeGateway = kundeGateway;
        this.projektGateway = projektGateway;
    }

    @Override
    public void save(ArrayList<WorkedTimeViewModel> workedTimeViewModels, String username) throws UserDoesNotExistException, InvalidDataException {
        loggedInUser = userGateway.getUserByUsername(username);
        initKundenAndProjekteLists();
        for (WorkedTimeViewModel timeViewModel : workedTimeViewModels)
            workedTimesToPersist.add(getWorkedTimeFromViewModel(timeViewModel));

        if (workedTimesToPersist.size() > 0)
            workedTimeGateway.saveAll(workedTimesToPersist);
    }

    private void initKundenAndProjekteLists() {
        kundenFromDb = kundeGateway.getAllByUser(loggedInUser);
        projekteFromDb = projektGateway.getAllByUser(loggedInUser);
        newKunden = new ArrayList<>();
        newProjekte = new ArrayList<>();
        workedTimesToPersist = new ArrayList<>();
    }

    private WorkedTime getWorkedTimeFromViewModel(WorkedTimeViewModel timeViewModel) throws InvalidDataException {
        WorkedTime workedTime = new WorkedTime()
                .setBeschreibung(timeViewModel.beschreibung)
                .setBreakInMinutes(timeViewModel.breakInMinutes)
                .setStartTime(getLocalDateTimeWithoutSecondsAndNanos(timeViewModel.startTimestamp))
                .setEndTime(getLocalDateTimeWithoutSecondsAndNanos(timeViewModel.endTimestamp))
                .setKunde(getKundeFromViewModel(timeViewModel.kundenViewModel))
                .setProjekt(getProjektFromViewModel(timeViewModel.projektViewModel));
        workedTime.setCreatedBy(loggedInUser);
        return workedTime;
    }

    private Projekt getProjektFromViewModel(ProjektViewModel projektViewModel) {
        if (projektViewModel == null)
            return null;
        try {
            return getProjektFromDb(projektViewModel);
        } catch (ProjektNotFoundException e) {
            return getNewProjekt(projektViewModel);
        }
    }

    private Projekt getNewProjekt(ProjektViewModel projektViewModel) {
        for (Projekt projekt : newProjekte) {
            if (projekt.getProjektName().equals(projektViewModel.projektName))
                return projekt;
        }
        Projekt projekt = new Projekt().setProjektName(projektViewModel.projektName);
        projekt.setCreatedBy(loggedInUser);
        newProjekte.add(projekt);
        return projekt;
    }

    private Projekt getProjektFromDb(ProjektViewModel viewModel) throws ProjektNotFoundException {
        for (Projekt projekt : projekteFromDb) {
            if (Objects.equals(projekt.getId(), viewModel.id) || Objects.equals(projekt.getProjektName(), viewModel.projektName))
                return projekt;
        }
        throw new ProjektNotFoundException();
    }

    private Kunde getKundeFromViewModel(KundenViewModel kundenViewModel) {
        if (kundenViewModel == null)
            return null;
        try {
            return getKundeFromDb(kundenViewModel);
        } catch (KundeNotFoundException e) {
            return getNewKunde(kundenViewModel);
        }
    }

    private Kunde getNewKunde(KundenViewModel kundenViewModel) {
        for (Kunde kunde : newKunden) {
            if (kunde.getKundenName().equals(kundenViewModel.kundenName))
                return kunde;
        }
        Kunde kunde = new Kunde().setKundenName(kundenViewModel.kundenName);
        kunde.setCreatedBy(loggedInUser);
        newKunden.add(kunde);
        return kunde;
    }

    private Kunde getKundeFromDb(KundenViewModel viewModel) throws KundeNotFoundException {
        for (Kunde kunde : kundenFromDb) {
            if (Objects.equals(kunde.getId(), viewModel.id) || Objects.equals(kunde.getKundenName(), viewModel.kundenName))
                return kunde;
        }
        throw new KundeNotFoundException();
    }

    private LocalDateTime getLocalDateTimeWithoutSecondsAndNanos(Timestamp timestamp) throws InvalidDataException {
        if (timestamp == null)
            throw new InvalidDataException();
        return timestamp.toLocalDateTime().withSecond(0).withNano(0);
    }
}
