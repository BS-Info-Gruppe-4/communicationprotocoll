package eu.bsinfo.gruppe4.server.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.bsinfo.gruppe4.server.database.CustomerSqlRepository;
import eu.bsinfo.gruppe4.server.database.ReadingSqlRepository;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonRepository {
    private final ReadingSqlRepository readingSqlRepository = new ReadingSqlRepository();
    private final CustomerSqlRepository customerSqlRepository = new CustomerSqlRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonRepository instance = new JsonRepository();
    private final String CUSTOMERS_FILEPATH = "target/kunden.json";
    private final String READINGS_FILEPATH = "target/ablesungen.json";
    private final String FILTERED_CUSTOMERS_FILEPATH = "target/gefilterte-kunden.json";
    private final String FILTERED_READINGS_FILEPATH = "target/gefilterte-ablesungen.json";


    public JsonRepository() {
        // konfiguriert jackson, sodass es auch LocalDate serialisieren kann
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static JsonRepository getInstance() {
        return instance;
    }


    public void persistDataInJsonFile() {

        try {
            exportAllCustomers();
            exportAllReadings();
        }
        catch (IOException e) {
            System.out.println("Die Daten konnten nicht exportiert werden");
        }

    }

    private void exportAllCustomers() throws IOException {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(CUSTOMERS_FILEPATH), customerSqlRepository.getAlleKunden());
    }

    private void exportAllReadings() throws IOException {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(READINGS_FILEPATH), readingSqlRepository.getAlleAblesungen());
    }

    public void exportFilteredData(List<Kunde> filteredCustomers, List<Ablesung> filteredReadings) {

        try {
            exportFilteredCustomers(filteredCustomers);
            exportFilteredReadings(filteredReadings);
        }
        catch (IOException e) {
            System.out.println("Die Daten konnten nicht exportiert werden");
        }

    }

    private void exportFilteredCustomers(List<Kunde> filteredCustomers) throws IOException {
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File(FILTERED_CUSTOMERS_FILEPATH), filteredCustomers);
    }

    private void exportFilteredReadings(List<Ablesung> filteredReadings) throws IOException {
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File(FILTERED_READINGS_FILEPATH), filteredReadings);
    }

}
