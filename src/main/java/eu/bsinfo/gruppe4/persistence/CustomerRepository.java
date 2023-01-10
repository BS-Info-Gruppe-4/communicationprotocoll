package eu.bsinfo.gruppe4.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bsinfo.gruppe4.model.Kunde;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomerRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final CustomerRepository instance = new CustomerRepository();
    private final String FILEPATH = "target/kunden.json";
    private ArrayList<Kunde> alleKunden = new ArrayList<>();


    public static CustomerRepository getInstance() {
        return instance;
    }

    public void save(Kunde kunde) {
        alleKunden.add(kunde);
    }

    public void saveToJsonFile() {

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILEPATH), alleKunden);
        }
        catch (IOException e) {
            System.out.println("Die Daten konnten nicht gespeichert werden");
        }
    }

    public void loadJsonFile() {

        final File persistedCustomerData = new File(FILEPATH);

        if (persistedCustomerData.exists()) {
            try {
                alleKunden = objectMapper.readValue(persistedCustomerData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Kunde.class));

            } catch (Exception e) {
                System.out.printf("Die Daten konnten nicht aus der Datei %s gelesen werden", FILEPATH);
            }
        }
    }

    public ArrayList<Kunde> getAlleKunden() {
        return alleKunden;
    }
}
