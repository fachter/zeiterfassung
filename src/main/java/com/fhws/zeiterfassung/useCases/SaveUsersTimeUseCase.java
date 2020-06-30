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
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import org.springframework.stereotype.Service;

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
    private final ArrayList<WorkedTime> workedTimesToPersist = new ArrayList<>();
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
                .setKunde(getKundeFromViewModel(timeViewModel))
                .setProjekt(getProjektFromViewModel(timeViewModel));
        workedTime.setCreatedBy(loggedInUser);
        return workedTime;
    }

    private Projekt getProjektFromViewModel(WorkedTimeViewModel timeViewModel) {
        if (timeViewModel.projektViewModel == null)
            return null;
        if (timeViewModel.projektViewModel.id != null)
            return getProjektById(timeViewModel.projektViewModel.id);
        return new Projekt().setProjektName(timeViewModel.projektViewModel.projektName);
    }

    private Projekt getProjektById(Long id) {
        for (Projekt projekt : projekteFromDb) {
            if (projekt.getId().equals(id))
                return projekt;
        }
        return null;
    }

    private Kunde getKundeFromViewModel(WorkedTimeViewModel timeViewModel) {
        if (timeViewModel.kundenViewModel == null)
            return null;
        if (timeViewModel.kundenViewModel.id != null)
            return getKundeById(timeViewModel.kundenViewModel.id);
        return new Kunde().setKundenName(timeViewModel.kundenViewModel.kundenName);
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
