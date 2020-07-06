package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.SaveUsersTime;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.KundeGateway;
import com.fhws.zeiterfassung.gateways.ProjektGateway;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.models.KundenViewModel;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final ArrayList<WorkedTime> workedTimesToPersist = new ArrayList<>();
    private final ArrayList<Kunde> nonPersistedKunden = new ArrayList<>();
    private final ArrayList<Projekt> nonPersistedProjekte = new ArrayList<>();
    private User loggedInUser;

    public SaveUsersTimeUseCase(UserGateway userGateway, WorkedTimeGateway workedTimeGateway, KundeGateway kundeGateway, ProjektGateway projektGateway) {
        this.userGateway = userGateway;
        this.workedTimeGateway = workedTimeGateway;
        this.kundeGateway = kundeGateway;
        this.projektGateway = projektGateway;
    }

    @Override
    public void save(ArrayList<WorkedTimeViewModel> workedTimeViewModels, String username) throws UserDoesNotExistException, InvalidDataException {
        loggedInUser = userGateway.getUserByUsername(username);
        getKundenAndProjekteFromDb();
        for (WorkedTimeViewModel timeViewModel : workedTimeViewModels) {
            workedTimesToPersist.add(getWorkedTimeFromViewModel(timeViewModel));
        }

        if (workedTimesToPersist.size() >0)
            workedTimeGateway.saveAll(workedTimesToPersist);

    }

    private void getKundenAndProjekteFromDb() {
        kundenFromDb = kundeGateway.getAllByUser(loggedInUser);
        projekteFromDb = projektGateway.getAllByUser(loggedInUser);
    }

    private WorkedTime getWorkedTimeFromViewModel(WorkedTimeViewModel timeViewModel) throws InvalidDataException {
        WorkedTime workedTime = new WorkedTime()
                .setBeschreibung(timeViewModel.beschreibung)
                .setStartTime(getLocalDateTimeWithoutSecondsAndNanos(timeViewModel.startTime))
                .setEndTime(getLocalDateTimeWithoutSecondsAndNanos(timeViewModel.endTime))
                .setKunde(getKundeFromViewModel(timeViewModel.kundenViewModel))
                .setProjekt(getProjektFromViewModel(timeViewModel.projektViewModel));
        workedTime.setCreatedBy(loggedInUser);
        return workedTime;
    }

    private Projekt getProjektFromViewModel(ProjektViewModel projektViewModel) {
        if (projektViewModel == null)
            return null;
        if (projektViewModel.id != null && projektViewModel.id > 0)
            return getProjektById(projektViewModel.id);
        for (Projekt projekt : nonPersistedProjekte) {
            if (projekt.getProjektName().equals(projektViewModel.projektName))
                return projekt;
        }
        Projekt newProjekt = new Projekt().setProjektName(projektViewModel.projektName);
        nonPersistedProjekte.add(newProjekt);
        return newProjekt;
    }

    private Projekt getProjektById(Long id) {
        for (Projekt projekt : projekteFromDb) {
            if (projekt.getId().equals(id))
                return projekt;
        }
        return null;
    }

    private Kunde getKundeFromViewModel(KundenViewModel kundenViewModel) {
        if (kundenViewModel == null)
            return null;
        if (kundenViewModel.id != null && kundenViewModel.id > 0)
            return getKundeById(kundenViewModel.id);
        for (Kunde kunde : nonPersistedKunden) {
            if (kunde.getKundenName().equals(kundenViewModel.kundenName))
                return kunde;
        }
        Kunde newKunde = new Kunde().setKundenName(kundenViewModel.kundenName);
        nonPersistedKunden.add(newKunde);
        return newKunde;
    }

    private Kunde getKundeById(Long id) {
        for (Kunde kunde : kundenFromDb) {
            if (Objects.equals(kunde.getId(), id))
                return kunde;
        }
        return null;
    }

    private LocalDateTime getLocalDateTimeWithoutSecondsAndNanos(LocalDateTime time) throws InvalidDataException {
        if (time == null)
            throw new InvalidDataException();
        return time.withSecond(0).withNano(0);
    }
}
