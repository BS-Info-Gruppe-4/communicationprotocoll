package eu.bsinfo.gruppe4.server.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import org.nocrala.tools.texttablefmt.Table;

public class Util {

    // singelton
    private static Connection con = null;

    // close
    public static void close(final AutoCloseable obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (final Exception e) {
                // ignore
            }
        }
    }

    // factory methode
    public static Connection getConnection(final String db) {

        if (con == null) {
            try {
                final Properties prop = new Properties();
                prop.load(new FileReader(db + ".properties"));
                final String dburl = prop.getProperty("DBURL");
                final String dbuser = prop.getProperty("DBUSER");
                final String dbpw = prop.getProperty("DBPW");

                con = DriverManager.getConnection(dburl, dbuser, dbpw);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    // print resultSet
    public static void printRs(final ResultSet rs) {

        try {
            final ResultSetMetaData rsmeta = rs.getMetaData();
            final int cols = rsmeta.getColumnCount();
            final Table t = new Table(cols);

            for (int i = 1; i <= cols; i++) {
                final String label = rsmeta.getColumnLabel(i);
                t.addCell(label);
            }

            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    final Object obj = rs.getObject(i);
                    t.addCell(obj == null ? "" : obj.toString());
                }
            }
            System.out.println(t.render());
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Util() {
    }
}