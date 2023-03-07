package eu.bsinfo.gruppe4.gui.service;

import eu.bsinfo.gruppe4.gui.MessageDialog;
import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.util.Optional;

public class ReadingService {

    private final WebClient webClient = new WebClient();
    private final PlausibilityService plausibilityService = new PlausibilityService();
    private final SessionStorage sessionStorage = SessionStorage.getInstance();


    public void createReading(Ablesung newReading) {

        if (plausibilityService.isNotPlausible(newReading)) {
            MessageDialog.showWarningMessage("Der Wert des Zählerstands liegt außerhalb des Normbereichs!\n" +
                    "Möglicherweise liegt ein Leck vor.");
        }

        // Checking for duplicate
        Optional<Ablesung> ablesungDuplicate = getDuplicateOf(newReading);

        if (ablesungDuplicate.isPresent()) {
            if (doesUserWantToKeepOldReading()) return;

            removeDuplicate(ablesungDuplicate.get());
        }

        Response response = webClient.createReading(newReading);

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
        MessageDialog.showSuccessMessage("Ablesung wurde gespeichert");
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
            throw new BadRequestException(errorMessage);
        }

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
        return response.readEntity(String.class);
    }

    public Ablesung deleteReading(Ablesung reading) {

        Response response = webClient.deleteReadingById(reading.getId());

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
        return response.readEntity(Ablesung.class);
    }



    private Optional<Ablesung> getDuplicateOf(Ablesung readingToCheck) {
        return sessionStorage.getAblesungen().stream()
                .filter(ablesung -> ablesung.isEqualsWithoutCheckingId(readingToCheck))
                .findFirst();
    }

    private boolean doesUserWantToKeepOldReading() {
        int reply = JOptionPane.showConfirmDialog(
                null,
                "Ein Datensatz mit den selben Werten ist bereits vorhanden. \n" +
                        "Möchtest du ihn ersetzen? Ansonsten wird der neue Datensatz nicht gespeichert",
                "Duplikat erkannt",
                JOptionPane.YES_NO_OPTION);

        return reply == JOptionPane.NO_OPTION;
    }

    private void removeDuplicate(Ablesung oldReading) {
        deleteReading(oldReading);
    }
}
