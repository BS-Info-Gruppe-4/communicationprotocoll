package eu.bsinfo.gruppe4.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Ablesung {
    private UUID id;
    private String zaehlernummer;
    private LocalDate datum;
    private Kunde kunde;
    private String kommentar;
    private boolean neuEingebaut;
    private int zaehlerstand;

    public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut, int zaehlerstand) {
        this.zaehlernummer = zaehlernummer;
        this.datum = datum;
        this.kunde = kunde;
        this.kommentar = kommentar;
        this.neuEingebaut = neuEingebaut;
        this.zaehlerstand = zaehlerstand;
    }
}
