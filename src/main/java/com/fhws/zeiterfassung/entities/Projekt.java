package com.fhws.zeiterfassung.entities;

import javax.persistence.Entity;

@Entity
public class Projekt extends BaseEntity {

    private String projektName;

    public String getProjektName() {
        return projektName;
    }

    public Projekt setProjektName(String projektName) {
        this.projektName = projektName;
        return this;
    }
}
