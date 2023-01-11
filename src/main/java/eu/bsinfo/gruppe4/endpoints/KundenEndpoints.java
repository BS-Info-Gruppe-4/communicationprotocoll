package eu.bsinfo.gruppe4.endpoints;

import eu.bsinfo.gruppe4.model.Kunde;
import eu.bsinfo.gruppe4.persistence.JsonRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("hausverwaltung/kunden")
public class KundenEndpoints {
    private final JsonRepository jsonRepositoryO=JsonRepository.getInstance();
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createKunde(Kunde kunde) {
        if (kunde == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Fehler").build();
        }

        kunde.setId(UUID.randomUUID());
        jsonRepositoryO.save(kunde);

        return Response.status(Response.Status.CREATED).entity(kunde).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response overwriteKunde(Kunde okunde) {
        if (okunde==null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Fehler").build();
        }
        if (jsonRepositoryO.kundeExists(okunde.getId())==false) {
            return Response.status(Response.Status.NOT_FOUND).entity("Kunde existiert nicht").build();
        }
        jsonRepositoryO.deleteKunde(okunde.getId());
        jsonRepositoryO.save(okunde);
        return Response.status(Response.Status.OK).entity("Kunde wurde geupdatet").build();
    }


    /*@DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public  Response deleteKunde(Kunde dkunde){

    }*/
}