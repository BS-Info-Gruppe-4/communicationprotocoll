package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Kunde;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    public void saveKunde(Kunde kunde);
    public Optional<Kunde> getKundeById(UUID kundenId);
    public ArrayList<Kunde> getAlleKunden();
    public boolean doesKundeExist(UUID kundenId);
    public void deleteKunde(UUID kundenId);

}
