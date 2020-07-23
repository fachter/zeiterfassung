package com.fhws.zeiterfassung.models;

import java.time.LocalDateTime;

public class WorkedTimeViewModel {

    public Long id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String beschreibung;
    public KundenViewModel kundenViewModel;
    public ProjektViewModel projektViewModel;
    public int breakInMinutes = 0;

}
