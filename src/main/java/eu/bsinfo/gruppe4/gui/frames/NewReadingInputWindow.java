package eu.bsinfo.gruppe4.gui.frames;

import eu.bsinfo.gruppe4.gui.service.ReadingService;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;

import java.util.zip.DataFormatException;

public class NewReadingInputWindow extends BaseReadingInputWindow {

    private final Kunde customerOfReading;
    private final ReadingService readingService = new ReadingService();

    public NewReadingInputWindow(Kunde kunde) {
        customerOfReading = kunde;
        setKundennummer(customerOfReading.getId().toString());

    }

    @Override
    protected Ablesung getReadingOfInputFields() throws DataFormatException {

        //FIXME: reading type is missing
        return new Ablesung(
                getZaehlernummer(),
                getDatum(),
                customerOfReading,
                getKommentar(),
                getWurdeNeuEingebaut(),
                getZaehlerstand()
        );
    }

    @Override
    protected Ablesung saveReading(Ablesung reading) {
        return readingService.createReading(reading);
    }

}
