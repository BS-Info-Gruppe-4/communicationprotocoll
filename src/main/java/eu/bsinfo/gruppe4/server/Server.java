package eu.bsinfo.gruppe4.server;

import com.sun.net.httpserver.HttpServer;
import eu.bsinfo.gruppe4.persistence.CustomerRepository;
import eu.bsinfo.gruppe4.persistence.ReadingRepository;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Server {

    public static final String PATH_TO_ENDPOINTS = "eu.bsinfo.gruppe4.endpoints";
    private static HttpServer server;
    private static boolean serverIsOnline = false;


    public static void startServer(String url, boolean loadFromFile) {

        if (serverIsOnline) {
            System.out.println("Es wurde bereits ein Server gestartet!");
            return;
        }

        if (loadFromFile) {
            CustomerRepository customerRepository = CustomerRepository.getInstance();
            ReadingRepository readingRepository = ReadingRepository.getInstance();

            customerRepository.loadJsonFile();
            readingRepository.loadJsonFile();
        }


        System.out.println("Trying to start server");
        System.out.println(url);

        final ResourceConfig rc = new ResourceConfig().packages(PATH_TO_ENDPOINTS);
        server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);

        System.out.println("Ready for Requests....");

        serverIsOnline = true;
    }

    public static void stopServer(boolean saveToFile) {

        if (!serverIsOnline) {
            System.out.println("Konnte Server nicht stoppen. Es wurde noch kein Server gestartet");
            return;
        }

        if (saveToFile) {
            CustomerRepository customerRepository = CustomerRepository.getInstance();
            ReadingRepository readingRepository = ReadingRepository.getInstance();

            customerRepository.saveToJsonFile();
            readingRepository.saveToJsonFile();
        }

        server.stop(0);
        serverIsOnline = false;
    }
}
