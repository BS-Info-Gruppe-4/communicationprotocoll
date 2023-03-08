package eu.bsinfo.gruppe4.server.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Ablesung {
    @EqualsAndHashCode.Include
    @ToString.Include
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

    public boolean isEqualsWithoutCheckingId(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ablesung ablesung = (Ablesung) o;

        if (neuEingebaut != ablesung.neuEingebaut) return false;
        if (zaehlerstand != ablesung.zaehlerstand) return false;
        if (!zaehlernummer.equals(ablesung.zaehlernummer)) return false;
        if (!datum.equals(ablesung.datum)) return false;
        if (!Objects.equals(kunde, ablesung.kunde)) return false;
        return Objects.equals(kommentar, ablesung.kommentar);
    }

}
