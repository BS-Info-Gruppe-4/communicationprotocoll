package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ReadingSqlRepository implements ReadingRepository{

    private final Connection con = Util.getConnection("gm3");

    @Override
    public void saveAblesung(Ablesung ablesung) {
        PreparedStatement statement = null;
        String query = "INSERT INTO Ablesung (id, zaehlernummer, datum, kunde, kommentar, neuEingebaut, zaehlerstand) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {

            statement = con.prepareStatement(query);
            statement.setString(1, ablesung.getId().toString());
            statement.setString(2, ablesung.getZaehlernummer());
            statement.setString(3, ablesung.getDatum().toString());
            statement.setString(4, ablesung.getKunde().toString());
            statement.setString(5, ablesung.getKommentar());
            statement.setBoolean(6, ablesung.isNeuEingebaut());
            statement.setInt(7, ablesung.getZaehlerstand());
            statement.executeUpdate();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        finally {
            Util.close(statement);
        }
    }

    @Override
    public Optional<Ablesung> getAblesungById(UUID ablesungId) {
        Ablesung ablesung;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT zaehlernummer, datum, kunde, kommentar, neuEingebaut, zaehlerstand FROM Kunde WHERE id = ?";

        try {

            statement = con.prepareStatement(query);
            statement.setString(1, ablesungId.toString());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String zaehlernummer = resultSet.getString("zaehlernummer");
                LocalDate datum = LocalDate.parse(resultSet.getString("datum"));
                Kunde kunde = (Kunde) resultSet.getObject("kunde");
                String kommentar = resultSet.getString("kommentar");
                Boolean neuEingebaut = resultSet.getBoolean("neu eingebaut");
                int zaehlerstand = resultSet.getInt("zaehlerstand");
                ablesung = new Ablesung(zaehlernummer, datum, kunde, kommentar, neuEingebaut, zaehlerstand);
                ablesung.setId(ablesungId);

                return Optional.of(ablesung);
            }

            return Optional.empty();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        finally {
            Util.close(statement);
            Util.close(resultSet);
        }
    }

    @Override
    public ArrayList<Ablesung> getAlleAblesungen() {
        ArrayList<Ablesung> allReadings = new ArrayList<>();
        String query = "SELECT * FROM Ablesung";

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String zaehlernummer = rs.getString("zaehlernummer");
                LocalDate datum = LocalDate.parse(rs.getString("datum"));
                Kunde kunde = (Kunde) rs.getObject("kunde");
                String kommentar = rs.getString("kommentar");
                Boolean neuEingebaut = rs.getBoolean("neu eingebaut");
                int zaehlerstand = rs.getInt("zaehlerstand");
                Ablesung ablesung = new Ablesung(id, zaehlernummer, datum, kunde, kommentar, neuEingebaut, zaehlerstand);

                allReadings.add(ablesung);
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return allReadings;
    }


}
