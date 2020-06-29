package com.fhws.zeiterfassung.entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Projekt extends BaseEntity {

    private String projektName;

    @OneToMany
    private Set<WorkedTime> workedTimes;

    public String getProjektName() {
        return projektName;
    }

    public Projekt setProjektName(String projektName) {
        this.projektName = projektName;
        return this;
    }

    public Set<WorkedTime> getWorkedTimes() {
        return workedTimes;
    }

    public Projekt setWorkedTimes(Set<WorkedTime> workedTimes) {
        this.workedTimes = workedTimes;
        return this;
    }
}
