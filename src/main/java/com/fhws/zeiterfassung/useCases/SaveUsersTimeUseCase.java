package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.SaveUsersTime;
import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.gateways.UserGateway;
import com.fhws.zeiterfassung.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class SaveUsersTimeUseCase implements SaveUsersTime {

    private final UserGateway userGateway;
    private final WorkedTimeGateway workedTimeGateway;

    public SaveUsersTimeUseCase(UserGateway userGateway, WorkedTimeGateway workedTimeGateway) {
        this.userGateway = userGateway;
        this.workedTimeGateway = workedTimeGateway;
    }

    @Override
    public void save(ArrayList<WorkedTimeViewModel> workedTimeViewModels, String username) throws UserDoesNotExistException, InvalidDataException {
        User user = userGateway.getUserByUsername(username);
        ArrayList<WorkedTime> workedTimesToPersist = new ArrayList<>();
        for (WorkedTimeViewModel timeViewModel : workedTimeViewModels) {
            WorkedTime workedTime = new WorkedTime()
                    .setBeschreibung(timeViewModel.beschreibung)
                    .setStartTime(getLocalDateTimeWithoutSecondsAndNanos(timeViewModel.startTime))
                    .setEndTime(getLocalDateTimeWithoutSecondsAndNanos(timeViewModel.endTime))
                    .setKunde(getKundeFromViewModel(timeViewModel))
                    .setProjekt(getProjektFromViewModel(timeViewModel));
            workedTime.setCreatedBy(user);
            workedTimesToPersist.add(workedTime);
        }

        if (workedTimesToPersist.size() >0)
            workedTimeGateway.saveAll(workedTimesToPersist);

    }

    private Projekt getProjektFromViewModel(WorkedTimeViewModel timeViewModel) {
        if (timeViewModel.projektViewModel == null)
            return null;
        return new Projekt().setProjektName(timeViewModel.projektViewModel.projektName);
    }

    private Kunde getKundeFromViewModel(WorkedTimeViewModel timeViewModel) {
        if (timeViewModel.kundenViewModel == null)
            return null;
        return new Kunde().setKundenName(timeViewModel.kundenViewModel.kundenName);
    }

    private LocalDateTime getLocalDateTimeWithoutSecondsAndNanos(LocalDateTime time) throws InvalidDataException {
        if (time == null)
            throw new InvalidDataException();
        return time.withSecond(0).withNano(0);
    }
}
