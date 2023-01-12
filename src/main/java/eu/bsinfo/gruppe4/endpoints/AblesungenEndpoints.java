package eu.bsinfo.gruppe4.endpoints;

import eu.bsinfo.gruppe4.model.Ablesung;
import eu.bsinfo.gruppe4.model.Kunde;
import eu.bsinfo.gruppe4.persistence.JsonRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import java.util.UUID;

@Path("hausverwaltung/ablesungen")
public class AblesungenEndpoints {

    private final JsonRepository jsonRepository = JsonRepository.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAblesung(Ablesung ablesung) {

        if (ablesung == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Der Request Body hat keine Ablesung enthalten")
                    .build();
        }

        if (ablesung.getKunde() == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Die angegebene Ablesung enthält keinen Kunden!")
                    .build();
        }

        UUID customerIdOfReading = ablesung.getKunde().getId();
        Optional<Kunde> savedCustomerOfReading = jsonRepository.getKunde(customerIdOfReading);

        if (savedCustomerOfReading.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Der Kunde der angegebenen Ablesung existiert nicht!")
                    .build();
        }

        ablesung.setId(UUID.randomUUID());
        jsonRepository.save(ablesung);

        return Response.status(Response.Status.CREATED).entity(ablesung).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingById(@PathParam("id") String readingId) {

        try {
            UUID readingUUID = UUID.fromString(readingId);
            Optional<Ablesung> queriedReading = jsonRepository.getAblesung(readingUUID);

            if (queriedReading.isEmpty()) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("Die Ablesung existiert nicht!")
                        .build();
            }

            return Response.ok(queriedReading.get()).build();
        }
        catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Die angegebene Id ist nicht gültig")
                    .build();
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateReading(Ablesung providedReading) {

        if (providedReading == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Der Request Body enthält keine Ablesung")
                    .build();
        }

        Optional<Ablesung> queriedReading = jsonRepository.getAblesung(providedReading.getId());

        if (queriedReading.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Die Ablesung existiert nicht!")
                    .build();
        }

        queriedReading.ifPresent(
                ablesung -> {
                    jsonRepository.deleteAblesung(ablesung.getId());
                    jsonRepository.save(providedReading);
                }
        );

        return Response.ok("Ablesung wurde aktualisiert").build();
    }
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAblesung(@PathParam(("id")) String readingID){
        try{
            UUID ReadingUUID = UUID.fromString(readingID);
            Optional<Ablesung> dReading=jsonRepository.getAblesung(ReadingUUID);
            if (dReading.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).entity("Reading existiert nicht").build();
            }
            jsonRepository.deleteAblesung(ReadingUUID);
            return Response.status(Response.Status.OK).entity(dReading.get()).build();
        }
        catch (IllegalArgumentException E){
            return Response.status(Response.Status.NOT_FOUND).entity("ID fehlerhaft").build();
        }

    }
}
