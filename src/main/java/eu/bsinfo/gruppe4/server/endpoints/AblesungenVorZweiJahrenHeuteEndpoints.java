package eu.bsinfo.gruppe4.server.endpoints;

import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.persistence.JsonRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.ArrayList;

@Path("hausverwaltung/ablesungenVorZweiJahrenHeute")
public class AblesungenVorZweiJahrenHeuteEndpoints {
    private final JsonRepository jsonRepository = JsonRepository.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response ablesungenVor2JahrenHeute() {

        ArrayList<Ablesung> alleAblesungen = jsonRepository.getAlleAblesungen();
        ArrayList<Ablesung> datumAblesungen = new ArrayList<>();

        int jahr = LocalDate.now().minusYears(2).getYear();
        LocalDate datum = LocalDate.of(jahr, 1, 1);

        for(int i = 0; i < alleAblesungen.size(); i++) {
            if(alleAblesungen.get(i).getDatum().isAfter(datum)){
                datumAblesungen.add(alleAblesungen.get(i));
            }
        }
        return Response.status(Response.Status.OK).entity(datumAblesungen).build();
    }
}
