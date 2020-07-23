package com.fhws.zeiterfassung.exceptions;

public class KundeNotFoundException extends Exception {

    public KundeNotFoundException() {
        super("Kunde nicht gefunden!");
    }
}
