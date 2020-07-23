package com.fhws.zeiterfassung.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class WorkedTime extends BaseEntity {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String beschreibung;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kunde_id")
    private Kunde kunde;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projekt_id")
    private Projekt projekt;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public WorkedTime setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public WorkedTime setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public WorkedTime setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
        return this;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public WorkedTime setKunde(Kunde kunde) {
        this.kunde = kunde;
        return this;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public WorkedTime setProjekt(Projekt projekt) {
        this.projekt = projekt;
        return this;
    }
}
