package eu.bsinfo.gruppe4;

import eu.bsinfo.gruppe4.gui.AllCustomersTable;
import eu.bsinfo.gruppe4.server.Server;

public class Main {

    public static String SERVER_URL = "http://localhost:8080/";
    public static void main(String[] args) {

        Server.startServer(SERVER_URL, true);
        new AllCustomersTable();

    }
}
