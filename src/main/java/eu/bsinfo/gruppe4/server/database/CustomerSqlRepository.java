package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Kunde;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class CustomerSqlRepository implements CustomerRepository {

    private final Connection con = Util.getConnection("gm3");

    @Override
    public void saveKunde(Kunde kunde) {
        PreparedStatement statement = null;
        String query = "INSERT INTO Kunde (id, name, vorname) VALUES (?, ?, ?)";

        try {

            statement = con.prepareStatement(query);
            statement.setString(1, kunde.getId().toString());
            statement.setString(2, kunde.getName());
            statement.setString(3, kunde.getVorname());
            statement.executeUpdate();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        finally {
            Util.close(statement);
        }
    }

    public Optional<Kunde> getKundeById(UUID kundenId) {
        Kunde kunde;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT name, vorname FROM Kunde WHERE id = ?";

        try {

            statement = con.prepareStatement(query);
            statement.setString(1, kundenId.toString());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String vorname = resultSet.getString("vorname");
                kunde = new Kunde(name, vorname);
                kunde.setId(kundenId);

                return Optional.of(kunde);
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
    public ArrayList<Kunde> getAlleKunden() {

        ArrayList<Kunde> allCustomers = new ArrayList<>();
        String query = "SELECT * FROM Kunde";

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String name = rs.getString("name");
                String vorname = rs.getString("vorname");
                Kunde kunde = new Kunde(id, name, vorname);
                allCustomers.add(kunde);
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return allCustomers;
    }

    @Override
    public boolean doesKundeExist(UUID kundenId) {
        return getKundeById(kundenId).isPresent();
    }

    @Override
    public void updateKunde(Kunde kunde) {

        PreparedStatement updateStatement = null;

        try {
            String updateSql = "UPDATE Kunde SET name=?, vorname=? WHERE id=?";
            updateStatement = con.prepareStatement(updateSql);
            updateStatement.setString(1, kunde.getName());
            updateStatement.setString(2, kunde.getVorname());
            updateStatement.setString(3, kunde.getId().toString());
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated <= 0) throw new RuntimeException("Was not able to update customer");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        finally {
            Util.close(updateStatement);
        }
    }

    @Override
    public void deleteKunde(UUID kundenId) {

        String sql = "DELETE FROM Kunde WHERE id=?";

        try(PreparedStatement deleteStatement = con.prepareStatement(sql)) {

            deleteStatement.setString(1, kundenId.toString());
            int rowsUpdated = deleteStatement.executeUpdate();

            if (rowsUpdated <= 0) throw new RuntimeException("Was not able to delete customer");

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
