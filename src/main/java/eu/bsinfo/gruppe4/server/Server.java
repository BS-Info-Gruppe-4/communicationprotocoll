package eu.bsinfo.gruppe4.server;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Server {

    static boolean serverIsOnline = false;

    public static void startServer(String url, boolean loadFromFile) {

        if (serverIsOnline) {
            System.out.println("Es wurde bereits ein Server gestartet!");
            return;
        }

        final String pack = "eu.bsinfo.gruppe4.endpoints";

        System.out.println("Trying to start server");
        System.out.println(url);

        final ResourceConfig rc = new ResourceConfig().packages(pack);
        final HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);

        System.out.println("Ready for Requests....");

        serverIsOnline = true;
    }
}
