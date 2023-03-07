package eu.bsinfo.gruppe4.gui.frames;

import eu.bsinfo.gruppe4.gui.service.ReadingService;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;


public class NewReadingInputWindow extends BaseReadingInputWindow {

    private final ReadingService readingService = new ReadingService();

    public NewReadingInputWindow(Kunde customerOfReading) {
        super(customerOfReading);

    }

    @Override
    protected void saveReading(Ablesung reading) {
        readingService.createReading(reading);
    }

}
