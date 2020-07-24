package com.fhws.zeiterfassung.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class WorkedTimeViewModel {

    public Long id;
    public Timestamp startTimestamp;
    public Timestamp endTimestamp;
    public String beschreibung;
    public KundenViewModel kundenViewModel;
    public ProjektViewModel projektViewModel;
    public int breakInMinutes = 0;

}
