package eu.bsinfo.gruppe4.server.database;

import eu.bsinfo.gruppe4.server.model.Kunde;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class CustomerSqlRepository implements CustomerRepository {

    @Override
    public void saveKunde(Kunde kunde) {

    }

    public Optional<Kunde> getKundeById(UUID kundenId) {
        Kunde kunde;
        String query = "SELECT name, vorname FROM Kunde WHERE id = ?";

        try {
            Connection con = Util.getConnection("gm3");

            PreparedStatement statement = con.prepareStatement(query);
            statement.setObject(1, kundenId);
            ResultSet resultSet = statement.executeQuery();

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
