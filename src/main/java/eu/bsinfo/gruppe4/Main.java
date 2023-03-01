package eu.bsinfo.gruppe4;

import eu.bsinfo.gruppe4.gui.LoadingWindow;
import eu.bsinfo.gruppe4.gui.PropertyManagementApplication;
import eu.bsinfo.gruppe4.server.Server;

public class Main {

    public static String SERVER_URL = "http://localhost:8080/";
    public static void main(String[] args) throws InterruptedException {
        new LoadingWindow();

        Server.startServer(SERVER_URL, true);
    }
}
