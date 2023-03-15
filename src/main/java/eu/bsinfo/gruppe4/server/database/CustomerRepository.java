package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Kunde;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    void saveKunde(Kunde kunde);
    Optional<Kunde> getKundeById(UUID kundenId);
    ArrayList<Kunde> getAlleKunden();
    boolean doesKundeExist(UUID kundenId);
    void updateKunde(Kunde kunde);
    void deleteKunde(UUID kundenId);

}
