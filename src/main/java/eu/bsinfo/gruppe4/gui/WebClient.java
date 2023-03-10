package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.Main;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.UUID;

import java.util.ArrayList;

public class WebClient {
    private static final String URL_BASE_PATH = Main.SERVER_URL;
    public static final String PATH_PROPERTY_MANAGEMENT = "hausverwaltung";
    private static final String PATH_CUSTOMER_ENDPOINTS = "kunden";
    private static final String PATH_READINGS_ENDPOINTS = "ablesungen";
    private static final String PATH_READINGS_LAST_TWO_YEARS = "ablesungenVorZweiJahrenHeute";
    private static final Client client = ClientBuilder.newClient();
    private WebTarget webTarget;


    public WebClient() {
        webTarget = client.target(URL_BASE_PATH);
        webTarget = client.target(Main.SERVER_URL.concat(PATH_PROPERTY_MANAGEMENT));
    }

    public Response createNewCustomer(Kunde kunde) {
        return webTarget.path(PATH_CUSTOMER_ENDPOINTS)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(kunde, MediaType.APPLICATION_JSON));
    }

    public ArrayList<Kunde> getAllCustomers() {
        Response response = webTarget.path(PATH_CUSTOMER_ENDPOINTS)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return response.readEntity(new GenericType<>() {});
    }

    public Response updateCustomer(Kunde kunde) {
         return webTarget.path(PATH_CUSTOMER_ENDPOINTS)
                .request(MediaType.TEXT_PLAIN)
                .put(Entity.entity(kunde, MediaType.APPLICATION_JSON));
    }

    public Response updateAblesung(Ablesung ablesung) {
        return webTarget.path(PATH_READINGS_ENDPOINTS)
                .request(MediaType.TEXT_PLAIN)
                .put(Entity.entity(ablesung, MediaType.APPLICATION_JSON));
    }

    public Response createReading(Ablesung reading) {
        return webTarget.path(PATH_READINGS_ENDPOINTS)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(reading, MediaType.APPLICATION_JSON));
    }

    public Response getReadingById(UUID readingId) {
        String pfad = PATH_READINGS_ENDPOINTS + "/" + readingId;
        return webTarget.path(pfad)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
    }

    public Response getReadingsWithRestrictions(UUID customerId, LocalDate startingDate, LocalDate endingDate) {
        return  webTarget.path(PATH_READINGS_ENDPOINTS)
                .queryParam("kunde", customerId)
                .queryParam("beginn", startingDate)
                .queryParam("ende", endingDate)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();
    }

    public ArrayList<Ablesung> getReadingsOfLast2Years() {
        Response response = webTarget.path(PATH_READINGS_LAST_TWO_YEARS)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();
        return response.readEntity(new GenericType<>() {});
    }

    public ArrayList<Ablesung> getAllReadings() {
        Response response = webTarget.path(PATH_READINGS_ENDPOINTS)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();
        return response.readEntity(new GenericType<>() {});
    }

    public static boolean entityWasCreated(Response response) {
        return response.getStatus() == 201;
    }

    public Response deleteCustomer(UUID id) {
        String pfad = PATH_CUSTOMER_ENDPOINTS+"/"+id;
        return webTarget.path(pfad)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();
    }

    public Response getCustomer(UUID id) {
        String pfad = PATH_CUSTOMER_ENDPOINTS+"/"+id;

        return webTarget.path(pfad)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();
    }

    public Response deleteReadingById(UUID id) {
        String pfad = PATH_READINGS_ENDPOINTS + "/" + id;
        return webTarget.path(pfad)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();
    }
}
