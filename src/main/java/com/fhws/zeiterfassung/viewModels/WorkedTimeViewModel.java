package com.fhws.zeiterfassung.viewModels;

import java.sql.Timestamp;

public class WorkedTimeViewModel {

    public Long id;
    public Timestamp startTimestamp;
    public Timestamp endTimestamp;
    public String beschreibung;
    public KundenViewModel kundenViewModel;
    public ProjektViewModel projektViewModel;
    public int breakInMinutes = 0;

}
