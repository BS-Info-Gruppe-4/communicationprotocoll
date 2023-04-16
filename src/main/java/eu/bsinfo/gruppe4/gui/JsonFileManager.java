package eu.bsinfo.gruppe4.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.bsinfo.gruppe4.gui.util.MessageDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JsonFileManager {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String FILEPATH = "target/kundendaten.json";

    public JsonFileManager() {
        // konfiguriert jackson, sodass es auch LocalDate serialisieren kann
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public void saveToJsonFile(ArrayList<ZaehlerDatensatz> kundenDatensaetze) {

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILEPATH), kundenDatensaetze);
        }
        catch (IOException e) {
            MessageDialog.showErrorMessage("Die Datei konnte nicht exportiert werden");
        }
    }

    public ArrayList<ZaehlerDatensatz> getContentOfJsonFile() {

        final File persistedCustomerData = new File(FILEPATH);

        if (persistedCustomerData.exists()) {
            try {
                return  objectMapper.readValue(persistedCustomerData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ZaehlerDatensatz.class));

            } catch (Exception e) {
                MessageDialog.showErrorMessage("Die Daten konnten nicht aus der Datei gelesen werden");
            }
        }

        return new ArrayList<>();
    }

}
