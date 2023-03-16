package eu.bsinfo.gruppe4.gui.persistence;

import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class SessionStorage {

    private final WebClient webClient = new WebClient();
    @Getter
    private static final SessionStorage instance = new SessionStorage();
    @Getter
    @Setter
    private ArrayList<Kunde> kunden = new ArrayList<>();
    @Getter
    @Setter
    private ArrayList<Ablesung> ablesungen = new ArrayList<>();

    public SessionStorage() {
        var alleKunden = webClient.getAllCustomers();
        setKunden(alleKunden);

        var readingsOfLast2Years = webClient.getReadingsOfLast2Years();
        setAblesungen(readingsOfLast2Years);

    }

    public Optional<Ablesung> getReadingById(UUID readingId) {
        return ablesungen.stream()
                .filter(savedReading -> savedReading.getId().equals(readingId))
                .findFirst();
    }

    public void syncWithBackend() {
        var alleKunden = webClient.getAllCustomers();
        setKunden(alleKunden);

        var allReadings = webClient.getAllReadings();
        setAblesungen(allReadings);
    }
}
