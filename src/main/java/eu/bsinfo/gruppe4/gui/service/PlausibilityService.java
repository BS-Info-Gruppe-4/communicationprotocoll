package eu.bsinfo.gruppe4.gui.service;

import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.server.model.Ablesung;

import java.io.Serializable;

public class PlausibilityService implements Serializable {
    private final SessionStorage sessionStorage = SessionStorage.getInstance();


    public boolean isNotPlausible(Ablesung reading) {
        int zaehlerstand = reading.getZaehlerstand();

        if (getAverageZaehlerstand() == 0) return false;

        double minPlausibleValue = getAverageZaehlerstand() * 0.5;
        double maxPlausibleValue = getAverageZaehlerstand() * 1.5;

        return zaehlerstand < minPlausibleValue || zaehlerstand > maxPlausibleValue || zaehlerstand < 0;
    }

    private double getAverageZaehlerstand() {
        return sessionStorage.getAblesungen()
                .stream()
                .mapToInt(Ablesung::getZaehlerstand)
                .average()
                .orElse(0);
    }
}