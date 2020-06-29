package com.fhws.zeiterfassung.entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Kunde extends BaseEntity {

    private String kundenName;

    @OneToMany
    private Set<WorkedTime> workedTimes;

    public String getKundenName() {
        return kundenName;
    }

    public Kunde setKundenName(String kundenName) {
        this.kundenName = kundenName;
        return this;
    }

    public Set<WorkedTime> getWorkedTimes() {
        return workedTimes;
    }

    public Kunde setWorkedTimes(Set<WorkedTime> workedTimes) {
        this.workedTimes = workedTimes;
        return this;
    }
}
