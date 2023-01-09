package eu.bsinfo.gruppe4.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Kunde {
    private UUID id;
    private String name;
    private String vorname;

    public Kunde(String name, String vorname) {
        this.name = name;
        this.vorname = vorname;
    }
}
