package eu.bsinfo.gruppe4.endpoints;

import eu.bsinfo.gruppe4.model.Kunde;
import eu.bsinfo.gruppe4.persistence.JsonRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
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
        if (jsonRepositoryO.kundeExists(dkunde.getId())==false) {
            return Response.status(Response.Status.NOT_FOUND).entity("Kunde existiert nicht").build();
        }

    }*/
    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKunden(){
        List<Kunde> AlleKunden=jsonRepositoryO.getAlleKunden();
        return Response.status(Response.Status.OK).entity(AlleKunden).build();
    }*/
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKunde(@PathParam(("id")) String kundenID){
        try{
            UUID kundenUUID = UUID.fromString(kundenID);
            Optional<Kunde> üKunde=jsonRepositoryO.getKunde(kundenUUID);
            if (üKunde.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).entity("Kunde existiert nicht").build();
            }
            return Response.status(Response.Status.OK).entity(üKunde.get()).build();
        }
        catch (IllegalArgumentException E){
            return Response.status(Response.Status.NOT_FOUND).entity("Kunde existiert nicht").build();
        }

    }
}