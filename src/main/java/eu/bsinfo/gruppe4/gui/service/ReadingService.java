package eu.bsinfo.gruppe4.gui.service;

import eu.bsinfo.gruppe4.gui.MessageDialog;
import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

public class ReadingService {

    private final WebClient webClient = new WebClient();
    private final PlausibilityService plausibilityService = new PlausibilityService();
    private final SessionStorage sessionStorage = SessionStorage.getInstance();


    public Ablesung createReading(Ablesung reading) {

        if (plausibilityService.isNotPlausible(reading)) {
            MessageDialog.showWarningMessage("Der Wert des Zählerstands liegt außerhalb des Normbereichs!\n" +
                    "Möglicherweise liegt ein Leck vor.");
        }

        Response response = webClient.createReading(reading);

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
        return response.readEntity(Ablesung.class);
    }

    public String updateReading(Ablesung reading) {

        if (plausibilityService.isNotPlausible(reading)) {
            MessageDialog.showWarningMessage("Der Wert des Zählerstands liegt außerhalb des Normbereichs!\n" +
                    "Möglicherweise liegt ein Leck vor.");
        }

        Response response = webClient.updateAblesung(reading);

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
        return response.readEntity(String.class);
    }
}
