package eu.bsinfo.gruppe4.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.bsinfo.gruppe4.model.Ablesung;
import eu.bsinfo.gruppe4.model.Kunde;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public void save(Kunde kunde) {
        alleKunden.add(kunde);
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
                    .writeValue(new File(CUSTOMERS_FILEPATH), alleKunden);
    }

    private void persistReadingsData() throws IOException {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(READINGS_FILEPATH), alleAblesungen);
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

    public Optional<Kunde> getKunde(UUID kundenId) {
        return alleKunden.stream()
                .filter(kunde -> kunde.getId().equals(kundenId))
                .findFirst();
    }

    public Optional<Ablesung> getAblesung(UUID ablesungId) {
        return alleAblesungen.stream()
                .filter(kunde -> kunde.getId().equals(ablesungId))
                .findFirst();
    }

    public boolean kundeExists(UUID searchedKundenId) {
        return alleKunden.stream().anyMatch(kunde -> kunde.getId().equals(searchedKundenId));
    }

    public void deleteAblesung(UUID ablesungId) {
        alleAblesungen.removeIf(ablesung -> ablesung.getId().equals(ablesungId));
    }

    public void deleteKunde(UUID kundenId) {
        alleKunden.removeIf(kunden -> kunden.getId().equals(kundenId));
    }

    public ArrayList<Kunde> getAlleKunden() {
        return alleKunden;
    }

    public ArrayList<Ablesung> getAlleAblesungen() {
        return alleAblesungen;
    }

    public ArrayList<Ablesung> getCustomerAblesungenInDateRange(UUID customerId, LocalDate startDate, LocalDate endDate) {
        return getAlleAblesungen().stream()
                .filter(reading -> customerId == null || (reading.getKunde() != null && reading.getKunde().getId().equals(customerId)))
                .filter(reading -> startDate == null || reading.getDatum().isAfter(startDate))
                .filter(reading -> endDate == null || reading.getDatum().isBefore(endDate))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
