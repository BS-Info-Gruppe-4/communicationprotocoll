package eu.bsinfo.gruppe4.server.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(keyUsing = KundeMapKeyDeserializer.class)
public class Kunde {
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id = UUID.randomUUID();
    private String name;
    private String vorname;

    public Kunde(String name, String vorname) {
        this.name = name;
        this.vorname = vorname;
    }
}
