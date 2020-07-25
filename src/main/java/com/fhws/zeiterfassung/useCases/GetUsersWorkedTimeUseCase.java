package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.useCases.GetUsersWorkedTime;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.boundaries.gateways.UserGateway;
import com.fhws.zeiterfassung.boundaries.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.viewModels.KundenViewModel;
import com.fhws.zeiterfassung.viewModels.ProjektViewModel;
import com.fhws.zeiterfassung.viewModels.WorkedTimeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

@Service
public class GetUsersWorkedTimeUseCase implements GetUsersWorkedTime {

    private final UserGateway userGateway;
    private final WorkedTimeGateway workedTimeGateway;

    @Autowired
    public GetUsersWorkedTimeUseCase(UserGateway userGateway, WorkedTimeGateway workedTimeGateway) {
        this.userGateway = userGateway;
        this.workedTimeGateway = workedTimeGateway;
    }

    @Override
    public ArrayList<WorkedTimeViewModel> get(String username) throws UserDoesNotExistException {
        User user = userGateway.getUserByUsername(username);
        ArrayList<WorkedTime> times = workedTimeGateway.getAllByUserOrderedByDate(user);
        ArrayList<WorkedTimeViewModel> timeViewModels = new ArrayList<>();
        for (WorkedTime time : times) {
            timeViewModels.add(getWorkedTimeViewModelFromTime(time));
        }
        return timeViewModels;
    }

    private WorkedTimeViewModel getWorkedTimeViewModelFromTime(WorkedTime time) {
        WorkedTimeViewModel timeViewModel = new WorkedTimeViewModel();
        timeViewModel.id = time.getId();
        timeViewModel.beschreibung = time.getBeschreibung();
        timeViewModel.breakInMinutes = time.getBreakInMinutes();
        timeViewModel.startTimestamp = Timestamp.valueOf(time.getStartTime());
        timeViewModel.endTimestamp = Timestamp.valueOf(time.getEndTime());
        timeViewModel.kundenViewModel = getKundenViewModel(time);
        timeViewModel.projektViewModel = getProjektViewModel(time);
        return timeViewModel;
    }

    private ProjektViewModel getProjektViewModel(WorkedTime time) {
        if (time.getProjekt() != null) {
            ProjektViewModel projektViewModel = new ProjektViewModel();
            projektViewModel.id = time.getProjekt().getId();
            projektViewModel.projektName = time.getProjekt().getProjektName();
            return projektViewModel;
        }
        return null;
    }

    private KundenViewModel getKundenViewModel(WorkedTime time) {
        if (time.getKunde() != null) {
            KundenViewModel kundenViewModel = new KundenViewModel();
            kundenViewModel.id = time.getKunde().getId();
            kundenViewModel.kundenName = time.getKunde().getKundenName();
            return kundenViewModel;
        }
        return null;
    }
}
