package eu.bsinfo.gruppe4.server.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class JsonRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonRepository instance = new JsonRepository();
    private final String CUSTOMERS_FILEPATH = "target/kunden.json";
    private final String READINGS_FILEPATH = "target/ablesungen.json";
    private ArrayList<Kunde> alleKunden = new ArrayList<>();
    private ArrayList<Ablesung> alleAblesungen = new ArrayList<>();


    public JsonRepository() {
        // konfiguriert jackson, sodass es auch LocalDate serialisieren kann
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static JsonRepository getInstance() {
        return instance;
    }

    public void save(Ablesung ablesung) {
        alleAblesungen.add(ablesung);
    }

    public void persistDataInJsonFile() {

        try {
            persistCustomerData();
            persistReadingsData();
        }
        catch (IOException e) {
            System.out.println("Die Daten konnten nicht gespeichert werden");
        }

    }

    private void persistCustomerData() throws IOException {

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(CUSTOMERS_FILEPATH), SessionStorage.getInstance().getKunden());
    }

    private void persistReadingsData() throws IOException {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(READINGS_FILEPATH), SessionStorage.getInstance().getAblesungen());
    }

    public void loadDataFromJsonFiles() {

        try {
            alleKunden = loadCustomerData();
            alleAblesungen = loadReadingsData();

        } catch (Exception e) {
            System.out.printf("Die Daten konnten nicht aus der Datei %s gelesen werden", CUSTOMERS_FILEPATH);
        }
    }

    private ArrayList<Kunde> loadCustomerData() throws IOException {
        final File persistedCustomerData = new File(CUSTOMERS_FILEPATH);

        if (persistedCustomerData.exists()) {
            return objectMapper.readValue(persistedCustomerData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Kunde.class));
        }

        return new ArrayList<>();
    }

    private ArrayList<Ablesung> loadReadingsData() throws IOException {
        final File persistedReadingsData = new File(READINGS_FILEPATH);

        if (persistedReadingsData.exists()) {
            return objectMapper.readValue(persistedReadingsData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Ablesung.class));
        }

        return new ArrayList<>();
    }


    public void deleteAblesung(UUID ablesungId) {
        alleAblesungen.removeIf(ablesung -> ablesung.getId().equals(ablesungId));
    }

    public ArrayList<Ablesung> getAlleAblesungen() {
        return alleAblesungen;
    }

}
