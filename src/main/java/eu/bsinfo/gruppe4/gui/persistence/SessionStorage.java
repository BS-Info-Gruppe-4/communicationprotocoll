package eu.bsinfo.gruppe4.gui.persistence;

import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SessionStorage {

    @Getter
    private static final SessionStorage instance = new SessionStorage();
    @Getter
    @Setter
    private ArrayList<Kunde> kunden = new ArrayList<>();
    @Getter
    @Setter
    private ArrayList<Ablesung> ablesungen = new ArrayList<>();

    public SessionStorage() {
        WebClient webClient = new WebClient();

        var alleKunden = webClient.getAllCustomers();
        setKunden(alleKunden);

        var readingsOfLast2Years = webClient.getReadingsOfLast2Years();
        setAblesungen(readingsOfLast2Years);
    }

    public void addKunde(Kunde kunde) {
        kunden.add(kunde);
    }
}
