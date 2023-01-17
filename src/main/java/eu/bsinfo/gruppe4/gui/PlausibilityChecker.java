package eu.bsinfo.gruppe4.gui;

import java.io.Serializable;
import java.util.ArrayList;

public class PlausibilityChecker implements Serializable {
    private final ArrayList<ZaehlerDatensatz> sessionData;

    public PlausibilityChecker(ArrayList<ZaehlerDatensatz> sessionData) {
        this.sessionData = sessionData;
    }

    public boolean isPlausible(int zaehlerstand) {

        if (getAverageZaehlerstand() == 0) return true;

        double minPlausibleValue = getAverageZaehlerstand() * 0.5;
        double maxPlausibleValue = getAverageZaehlerstand() * 1.5;

        return zaehlerstand >= minPlausibleValue && zaehlerstand <= maxPlausibleValue;
    }

    private double getAverageZaehlerstand() {
        return sessionData
                .stream()
                .mapToInt(ZaehlerDatensatz::getZaehlerstand)
                .average()
                .orElse(0);
    }
}