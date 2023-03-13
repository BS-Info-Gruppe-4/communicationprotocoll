package eu.bsinfo.gruppe4.gui.frames;

import eu.bsinfo.gruppe4.gui.AllCustomersTable;
import eu.bsinfo.gruppe4.gui.MessageDialog;
import eu.bsinfo.gruppe4.gui.service.ReadingService;
import eu.bsinfo.gruppe4.server.model.Ablesung;

import java.awt.*;

public class EditReadingInputWindow extends BaseReadingInputWindow {

    private final ReadingService readingService = new ReadingService();
    private final Ablesung currentEditingReading;


    public EditReadingInputWindow(Ablesung readingToEdit, AllCustomersTable allCustomersTable) throws HeadlessException {
        super(readingToEdit.getKunde(), allCustomersTable);
        currentEditingReading = readingToEdit;
        setDefaultInputValues();
    }

    private void setDefaultInputValues() {
        setZaehlernummer(currentEditingReading.getZaehlernummer());
        setDatum(currentEditingReading.getDatum());
        setKommentar(currentEditingReading.getKommentar());
        setWurdeNeuEingebaut(currentEditingReading.isNeuEingebaut());

        String zaehlerstandAsString = Integer.toString(currentEditingReading.getZaehlerstand());
        setZaehlerstand(zaehlerstandAsString);
    }

    @Override
    protected void saveReading(Ablesung reading) {
        reading.setId(currentEditingReading.getId());
        String responseMessage = readingService.updateReading(reading);
        MessageDialog.showSuccessMessage(responseMessage);
    }
}
