package eu.bsinfo.gruppe4.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bsinfo.gruppe4.model.Ablesung;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadingRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final ReadingRepository instance = new ReadingRepository();
    private final String FILEPATH = "target/ablesungen.json";
    private ArrayList<Ablesung> alleAblesungen = new ArrayList<>();


    public static ReadingRepository getInstance() {
        return instance;
    }

    public void save(Ablesung ablesung) {
        alleAblesungen.add(ablesung);
    }

    public void saveToJsonFile() {

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILEPATH), alleAblesungen);
        }
        catch (IOException e) {
            System.out.println("Die Daten konnten nicht gespeichert werden");
        }
    }

    public void loadJsonFile() {

        final File persistedData = new File(FILEPATH);

        if (persistedData.exists()) {
            try {
                alleAblesungen = objectMapper.readValue(persistedData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Ablesung.class));

            } catch (Exception e) {
                System.out.printf("Die Daten konnten nicht aus der Datei %s gelesen werden", FILEPATH);
            }
        }
    }

    public ArrayList<Ablesung> getAlleAblesungen() {
        return alleAblesungen;
    }
}
