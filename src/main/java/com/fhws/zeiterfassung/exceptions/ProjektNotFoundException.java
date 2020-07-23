package com.fhws.zeiterfassung.exceptions;

public class ProjektNotFoundException extends Exception {

    public ProjektNotFoundException() {
        super("Projekt nicht gefunden!");
    }
}
