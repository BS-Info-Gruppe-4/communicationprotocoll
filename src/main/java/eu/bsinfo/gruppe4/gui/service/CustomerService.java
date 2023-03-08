package eu.bsinfo.gruppe4.gui.service;

import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class CustomerService {

    private final WebClient webClient = new WebClient();
    private final SessionStorage sessionStorage = SessionStorage.getInstance();

    public Kunde getCustomerById(UUID customerId) {

        Response response = webClient.getCustomer(customerId);

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
        return response.readEntity(Kunde.class);
    }

    public void deleteCustomerById(UUID customerId) throws NotFoundException, UnknownError {
        Response response = webClient.deleteCustomer(customerId);

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            throw new NotFoundException(errorMessage);
        }

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new UnknownError("Es ist ein unbekannter Fehler aufgetreten");
        }

        sessionStorage.syncWithBackend();
    }

    public String updateCustomer(Kunde customer) throws NotFoundException, UnknownError {
        Response response = webClient.updateCustomer(customer);

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
}
