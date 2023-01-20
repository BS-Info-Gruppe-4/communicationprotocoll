package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.Main;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class WebClient {
    private static final String URL_BASE_PATH = Main.SERVER_URL;
    public static final String PATH_PROPERTY_MANAGEMENT = "hausverwaltung";
    private static final String PATH_CUSTOMER_ENDPOINTS = "kunden";
    private static final String PATH_READINGS_ENDPOINTS = "ablesungen";
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

    public static boolean entityWasCreated(Response response) {
        return response.getStatus() == 201;
    }
}
