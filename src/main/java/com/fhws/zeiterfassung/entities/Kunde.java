package com.fhws.zeiterfassung.entities;

import javax.persistence.Entity;

@Entity
public class Kunde extends BaseEntity {

    private String kundenName;

    public String getKundenName() {
        return kundenName;
    }

    public Kunde setKundenName(String kundenName) {
        this.kundenName = kundenName;
        return this;
    }
}
