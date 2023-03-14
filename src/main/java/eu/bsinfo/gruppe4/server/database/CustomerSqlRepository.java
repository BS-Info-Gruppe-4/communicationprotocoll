package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Kunde;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class CustomerSqlRepository implements CustomerRepository {

    @Override
    public void saveKunde(Kunde kunde) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "INSERT INTO Kunde (id, name, vorname) VALUES (?, ?, ?)";

        try (Connection con = Util.getConnection("gm3")) {

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
            Util.close(resultSet);
        }
    }

    public Optional<Kunde> getKundeById(UUID kundenId) {
        Kunde kunde;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT name, vorname FROM Kunde WHERE id = ?";

        try (Connection con = Util.getConnection("gm3")){

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
        return null;
    }

    @Override
    public boolean doesKundeExist(UUID kundenId) {
        return false;
    }

    @Override
    public void deleteKunde(UUID kundenId) {

    }
}
