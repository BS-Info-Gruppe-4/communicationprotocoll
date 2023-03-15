package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Ablesung;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public interface ReadingRepository {

    public void saveAblesung(Ablesung ablesung);
    public Optional<Ablesung> getAblesungById(UUID ablesungId);
    public ArrayList<Ablesung> getAlleAblesungen();

    boolean doesAblesungExist(UUID ablesungId);

    void updateAblesung(Ablesung ablesung);

    void deleteAblesung(UUID ablesungId);
}
