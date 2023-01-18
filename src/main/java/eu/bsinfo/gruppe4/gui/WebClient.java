package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.server.Server;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class WebClient {
    private static final String URL_BASE_PATH = Server.PATH_TO_ENDPOINTS;
    private static final String PATH_CUSTOMER_ENDPOINTS = "kunden";
    private static final String PATH_READINGS_ENDPOINTS = "ablesungen";
    private static final Client client = ClientBuilder.newClient();
    private final WebTarget webTarget = client.target(URL_BASE_PATH);


    private Response createNewCustomer(Kunde kunde) {
        return webTarget.path(PATH_CUSTOMER_ENDPOINTS)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(kunde, MediaType.APPLICATION_JSON));
    }
}
