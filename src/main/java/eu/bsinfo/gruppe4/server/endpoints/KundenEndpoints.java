package eu.bsinfo.gruppe4.server.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bsinfo.gruppe4.server.database.CustomerRepository;
import eu.bsinfo.gruppe4.server.database.CustomerSqlRepository;
import eu.bsinfo.gruppe4.server.database.ReadingRepository;
import eu.bsinfo.gruppe4.server.database.ReadingSqlRepository;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.stream.Collectors;

@Path("hausverwaltung/kunden")
public class KundenEndpoints {

    private final CustomerRepository customerRepository = new CustomerSqlRepository();
    private final ReadingRepository readingRepository = new ReadingSqlRepository();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createKunde(Kunde kunde) {
        if (kunde == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Es wurde kein gültigen Kundenobjekt übergeben")
                    .build();
        }

        //kunde.setId(UUID.randomUUID());
        customerRepository.saveKunde(kunde);

        return Response
                .status(Response.Status.CREATED)
                .entity(kunde)
                .build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response overwriteKunde(Kunde okunde) {
        if (okunde==null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Fehler").build();
        }
        if (customerRepository.doesKundeExist(okunde.getId())==false) {
            return Response.status(Response.Status.NOT_FOUND).entity("Kunde existiert nicht").build();
        }
        customerRepository.updateKunde(okunde);

        return Response.status(Response.Status.OK).entity("Kunde wurde aktualisiert").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlleKunden(){
        List<Kunde> AlleKunden=customerRepository.getAlleKunden();
        return Response.status(Response.Status.OK).entity(AlleKunden).build();
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKunde(@PathParam(("id")) String kundenID){
        try{
            UUID kundenUUID = UUID.fromString(kundenID);
            Optional<Kunde> üKunde = customerRepository.getKundeById(kundenUUID);
            if (üKunde.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).entity("Kunde existiert nicht").build();
            }
            return Response.status(Response.Status.OK).entity(üKunde.get()).build();
        }
        catch (IllegalArgumentException E){
            return Response.status(Response.Status.NOT_FOUND).entity("ID fehlerhaft").build();
        }

    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteKunde(@PathParam(("id")) String kundenID) {
        try {
            UUID kundenUUID = UUID.fromString(kundenID);
            Optional<Kunde> customerOptional = customerRepository.getKundeById(kundenUUID);

            if (customerOptional.isEmpty()) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("Der Kunde mit der ID existiert nicht")
                        .build();
            }

            Kunde customer = customerOptional.get();

            List<Ablesung> kundenAbl = readingRepository.getAlleAblesungen().stream()
                    .filter(ablesung -> ablesung.getKunde() != null)
                    .filter(ablesung -> ablesung.getKunde().getId().equals(customer.getId()))
                    .collect(Collectors.toList());

            customerRepository.deleteKunde(customer.getId());

            // I had to parse the customer object to a json string manually,
            // because jackson somehow wasn't able to do it.
            ObjectMapper mapper = new ObjectMapper();
            String customerAsJsonString = mapper.writeValueAsString(customer);

            Map<String, List<Ablesung>> customerWithItsReadings = new HashMap<>();
            customerWithItsReadings.put(customerAsJsonString, kundenAbl);

            return Response.status(Response.Status.OK).entity(customerWithItsReadings).build();

        } catch(IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("ID fehlerhaft").build();
        } catch (JsonProcessingException e) {
            //TODO: Return as response
            throw new RuntimeException(e);
        }

    }
}