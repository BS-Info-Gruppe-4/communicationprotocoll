package eu.bsinfo.gruppe4.server.endpoints;

import eu.bsinfo.gruppe4.server.database.CustomerSqlRepository;
import eu.bsinfo.gruppe4.server.database.ReadingSqlRepository;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import eu.bsinfo.gruppe4.server.persistence.JsonRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("hausverwaltung/ablesungen")
public class AblesungenEndpoints {

    private final JsonRepository jsonRepository = JsonRepository.getInstance();
    private final ReadingSqlRepository readingSqlRepository = new ReadingSqlRepository();
    private final CustomerSqlRepository customerSqlRepository = new CustomerSqlRepository();

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
        Optional<Kunde> savedCustomerOfReading = customerSqlRepository.getKundeById(customerIdOfReading);

        if (savedCustomerOfReading.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Der Kunde der angegebenen Ablesung existiert nicht!")
                    .build();
        }

        ablesung.setId(UUID.randomUUID());
        //jsonRepository.save(ablesung);
        readingSqlRepository.saveAblesung(ablesung);

        return Response.status(Response.Status.CREATED).entity(ablesung).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingById(@PathParam("id") String readingId) {

        try {
            UUID readingUUID = UUID.fromString(readingId);
            Optional<Ablesung> queriedReading = readingSqlRepository.getAblesungById(readingUUID);

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

        Optional<Ablesung> queriedReading = readingSqlRepository.getAblesungById(providedReading.getId());

        if (queriedReading.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Die übergebene Ablesung existiert nicht!")
                    .build();
        }

        Ablesung reading = queriedReading.get();
        Kunde customerOfReading = reading.getKunde();

        if (customerOfReading == null || !customerSqlRepository.doesKundeExist(customerOfReading.getId())){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Der Kunde der Ablesung existiert nicht!")
                    .build();
        }

        readingSqlRepository.updateAblesung(providedReading);

        return Response.ok("Ablesung wurde aktualisiert").build();
    }
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReading(@PathParam(("id")) String readingID){
        try{
            UUID ReadingUUID = UUID.fromString(readingID);
            Optional<Ablesung> dReading=readingSqlRepository.getAblesungById(ReadingUUID);
            if (dReading.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).entity("Reading existiert nicht").build();
            }
            readingSqlRepository.deleteAblesung(ReadingUUID);
            return Response.status(Response.Status.OK).entity(dReading.get()).build();
        }
        catch (IllegalArgumentException E){
            return Response.status(Response.Status.NOT_FOUND).entity("ID fehlerhaft").build();
        }
    }

    //FIXME: Unit test fails bc start and ending date are also included instead of being excluded
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReadingsWithRestriction(
            @QueryParam("kunde") String customerId,
            @QueryParam("beginn") String startingDateAsString,
            @QueryParam("ende") String endingDateAsString
    ){

        UUID customerUUID = null;
        LocalDate startingDate = null;
        LocalDate endingDate = null;


        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (startingDateAsString != null) startingDate = LocalDate.parse(startingDateAsString, dateTimeFormatter);
            if (endingDateAsString != null)endingDate = LocalDate.parse(endingDateAsString, dateTimeFormatter);
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Die übergebenen Daten sind nicht im yyyy-MM-dd Format")
                    .build();
        }

        if (customerId != null) {
            try {
                customerUUID = UUID.fromString(customerId);
            } catch (IllegalArgumentException e) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Die übergebene ID ist ungültig")
                        .build();
            }
        }

        ArrayList<Ablesung> alleAblesungen = readingSqlRepository.getAlleAblesungen();
        ArrayList filteredReadings = filterReadings(customerUUID, startingDate, endingDate, alleAblesungen);

        return Response.ok(filteredReadings).build();
    }

    public ArrayList<Ablesung> filterReadings(UUID customerId, LocalDate startDate, LocalDate endDate, ArrayList<Ablesung> readings) {
        return readings.stream()
                .filter(reading -> customerId == null || (reading.getKunde() != null && reading.getKunde().getId().equals(customerId)))
                .filter(reading -> startDate == null || reading.getDatum().isAfter(startDate) || reading.getDatum().equals(startDate))
                .filter(reading -> endDate == null || reading.getDatum().isBefore(endDate) || reading.getDatum().equals(endDate))
                .collect(Collectors.toCollection(ArrayList::new));
    }


}

