package eu.bsinfo.gruppe4.gui.service;

import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class ReadingService {

    private final WebClient webClient = new WebClient();
    private final SessionStorage sessionStorage = SessionStorage.getInstance();

    public Ablesung createReading(Ablesung ablesung) {
        Response response = webClient.createAblesung(ablesung);

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new BadRequestException(errorMessage);
        }

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
        return response.readEntity(Ablesung.class);
    }

    public ArrayList<Ablesung> getReadingsWithRestrictions(UUID customerId, LocalDate startingDate, LocalDate endingDate) {
        Response response = webClient.getReadingsWithRestrictions(customerId, startingDate, endingDate);

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new BadRequestException(errorMessage);
        }

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
        return response.readEntity(new GenericType<>() {});
    }
}
