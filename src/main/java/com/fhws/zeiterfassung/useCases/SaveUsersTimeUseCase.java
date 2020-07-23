package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.SaveUsersTime;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
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

        if (workedTimesToPersist.size() > 0)
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
        try {
            return getProjektFromDb(timeViewModel.projektViewModel);
        } catch (EntityNotFoundException e) {
            return new Projekt().setProjektName(timeViewModel.projektViewModel.projektName);
        }
    }

    private Projekt getProjektFromDb(ProjektViewModel viewModel) throws EntityNotFoundException {
        for (Projekt projekt : projekteFromDb) {
            if (Objects.equals(projekt.getId(), viewModel.id) || Objects.equals(projekt.getProjektName(), viewModel.projektName))
                return projekt;
        }
        throw new EntityNotFoundException();
    }

    private Kunde getKundeFromViewModel(WorkedTimeViewModel timeViewModel) {
        if (timeViewModel.kundenViewModel == null)
            return null;
        try {
            return getKundeFromDb(timeViewModel.kundenViewModel);
        } catch (EntityNotFoundException e) {
            return new Kunde().setKundenName(timeViewModel.kundenViewModel.kundenName);
        }
    }

    private Kunde getKundeFromDb(KundenViewModel viewModel) throws EntityNotFoundException {
        for (Kunde kunde : kundenFromDb) {
            if (Objects.equals(kunde.getId(), viewModel.id) || Objects.equals(kunde.getKundenName(), viewModel.kundenName))
                return kunde;
        }
        throw new EntityNotFoundException();
    }

    private LocalDateTime getLocalDateTimeWithoutSecondsAndNanos(LocalDateTime time) throws InvalidDataException {
        if (time == null)
            throw new InvalidDataException();
        return time.withSecond(0).withNano(0);
    }
}
