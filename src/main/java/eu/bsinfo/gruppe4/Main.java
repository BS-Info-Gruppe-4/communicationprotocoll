package eu.bsinfo.gruppe4;

import eu.bsinfo.gruppe4.gui.PropertyManagementApplication;
import eu.bsinfo.gruppe4.server.Server;

public class Main {
    public static void main(String[] args) {

        String SERVER_URL = "http://localhost:8080/hausverwaltung";

        new PropertyManagementApplication();
        Server.startServer(SERVER_URL, true);
    }
}
