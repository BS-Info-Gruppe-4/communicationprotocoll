package eu.bsinfo.gruppe4.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Kunde {
    @EqualsAndHashCode.Include
    private UUID id;
    private String name;
    private String vorname;

    public Kunde(String name, String vorname) {
        this.name = name;
        this.vorname = vorname;
    }
}
