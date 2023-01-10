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
    public Response getReadingById(@PathParam("id") UUID readingId) {

        Optional<Ablesung> queriedReading = jsonRepository.getAblesung(readingId);

        if (queriedReading.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Der Kunde der angegebenen Ablesung existiert nicht!")
                    .build();
        }

        return Response.ok(queriedReading).build();
    }
}
