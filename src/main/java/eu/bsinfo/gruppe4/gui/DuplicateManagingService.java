package eu.bsinfo.gruppe4.gui;

import javax.swing.*;
import java.util.ArrayList;

public class DuplicateManagingService {

    private final ArrayList<ZaehlerDatensatz> sessionData;

    public DuplicateManagingService(ArrayList<ZaehlerDatensatz> sessionData) {
        this.sessionData = sessionData;
    }

    public boolean isDuplicate(ZaehlerDatensatz zaehlerDatensatz) {

        for (ZaehlerDatensatz sessionDatensatz: sessionData) {
            if (zaehlerDatensatz.equals(sessionDatensatz)) return true;
        }

        return false;
    }

    public boolean doesUserWantToKeepTheOriginal() {
        int reply = JOptionPane.showConfirmDialog(
                null,
                "Ein Datensatz mit den selben Werten ist bereits vorhanden. \n" +
                        "Möchtest du ihn überschreiben?",
                "Duplikat erkannt",
                JOptionPane.YES_NO_OPTION);

        return reply == JOptionPane.YES_OPTION;
    }

    public void removeDuplicateOf(ZaehlerDatensatz zaehlerDatensatz) {
        sessionData.removeIf(zaehlerDatensatz::equals);
    }


}
