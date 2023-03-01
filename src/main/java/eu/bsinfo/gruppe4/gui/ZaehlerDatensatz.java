package eu.bsinfo.gruppe4.gui;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZaehlerDatensatz {

    private String kundennummer;
    private Zaehlerart zaehlerart;
    private String zaehlernummer;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate datum;
    private boolean neuEingebaut;
    private int zaehlerstand;
    private String kommentar;


    @Override
    public boolean equals(Object obj) {

        // Gibt true zurück, wenn das Objekt mit sich selbst verglichen wird
        if (obj == this) {
            return true;
        }

        // Gibt false zurück, wenn das Objekt nicht derselben Klasse angehört
        if (!(obj instanceof ZaehlerDatensatz)) {
            return false;
        }

        // Castet das Objekt auf ZaehlerDatensatz, damit auf die Attribute von ZaehlerDatensatz
        // zugegriffen werden kann
        ZaehlerDatensatz compareObject = (ZaehlerDatensatz) obj;

        // Vergleicht alle Felder beider Objekte
        return kundennummer == compareObject.kundennummer &&
                zaehlerart == compareObject.zaehlerart &&
                datum.isEqual(compareObject.datum) &&
                neuEingebaut == compareObject.neuEingebaut &&
                zaehlerstand == compareObject.zaehlerstand &&
                Objects.equals(kommentar, compareObject.kommentar);
    }
}
