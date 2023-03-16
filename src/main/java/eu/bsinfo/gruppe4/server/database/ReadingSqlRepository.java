package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ReadingSqlRepository implements ReadingRepository{

    private final Connection con = Util.getConnection("gm3");
    CustomerSqlRepository customerSqlRepository = new CustomerSqlRepository();

    @Override
    public void saveAblesung(Ablesung ablesung) {
        PreparedStatement statement = null;
        String query = "INSERT INTO Ablesung (id, zaehlernummer, datum, kunde, kommentar, neuEingebaut, zaehlerstand) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {

            statement = con.prepareStatement(query);
            statement.setString(1, ablesung.getId().toString());
            statement.setString(2, ablesung.getZaehlernummer());
            statement.setString(3, ablesung.getDatum().toString());
            statement.setString(4, ablesung.getKunde().getId().toString());
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
        String query = "SELECT zaehlernummer, datum, kunde, kommentar, neuEingebaut, zaehlerstand FROM Ablesung WHERE id = ?";

        try {

            statement = con.prepareStatement(query);
            statement.setString(1, ablesungId.toString());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String zaehlernummer = resultSet.getString("zaehlernummer");
                LocalDate datum = LocalDate.parse(resultSet.getString("datum"));
                UUID customerId = UUID.fromString(resultSet.getString("kunde"));
                String kommentar = resultSet.getString("kommentar");
                boolean neuEingebaut = resultSet.getBoolean("neuEingebaut");
                int zaehlerstand = resultSet.getInt("zaehlerstand");

                Kunde kunde = customerSqlRepository.getKundeById(customerId)
                        .orElseThrow(() -> new RuntimeException("Kunde der Ablesung konnte nicht gefunden werden"));

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
                UUID kundeId = UUID.fromString(rs.getString("kunde"));
                String kommentar = rs.getString("kommentar");
                boolean neuEingebaut = rs.getBoolean("neuEingebaut");
                int zaehlerstand = rs.getInt("zaehlerstand");
                Optional<Kunde> kunde = customerSqlRepository.getKundeById(kundeId);
                Ablesung ablesung = new Ablesung(id, zaehlernummer, datum, kunde.get(), kommentar, neuEingebaut, zaehlerstand);

                allReadings.add(ablesung);
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return allReadings;
    }
    @Override
    public boolean doesAblesungExist(UUID ablesungId) {
        return getAblesungById(ablesungId).isPresent();
    }
    @Override
    public void updateAblesung(Ablesung ablesung) {
        PreparedStatement updateStatement = null;

        try {
            String updateSql = "UPDATE Ablesung SET zaehlernummer=?, datum=?, kunde=?, kommentar=?, neuEingebaut=?, zaehlerstand=? WHERE id=?";
            updateStatement = con.prepareStatement(updateSql);
            updateStatement.setString(1, ablesung.getZaehlernummer());
            updateStatement.setDate(2, Date.valueOf(ablesung.getDatum()));
            updateStatement.setString(3, ablesung.getKunde().getId().toString());
            updateStatement.setString(4, ablesung.getKommentar());
            updateStatement.setBoolean(5, ablesung.isNeuEingebaut());
            updateStatement.setInt(6, ablesung.getZaehlerstand());
            updateStatement.setString(7, ablesung.getId().toString());
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated <= 0) throw new RuntimeException("Was not able to update reading");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        finally {
            Util.close(updateStatement);
        }
    }
    @Override
    public void deleteAblesung(UUID ablesungId) {
        String sql = "DELETE FROM Ablesung WHERE id=?";

        try(PreparedStatement deleteStatement = con.prepareStatement(sql)) {

            deleteStatement.setString(1, ablesungId.toString());
            int rowsUpdated = deleteStatement.executeUpdate();

            if (rowsUpdated <= 0) throw new RuntimeException("Was not able to delete reading");

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
