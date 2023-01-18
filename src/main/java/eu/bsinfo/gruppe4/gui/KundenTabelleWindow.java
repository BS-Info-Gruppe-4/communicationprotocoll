package eu.bsinfo.gruppe4.gui;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KundenTabelleWindow extends JFrame{

    private final JTable tabelle;
    private final ArrayList<ZaehlerDatensatz> sessionData;


    KundenTabelleWindow(ArrayList<ZaehlerDatensatz> sessionData, int kundennr) throws IOException {

        this.sessionData = sessionData;

        setTitle("Datensätze für Kundennummer: " + kundennr);

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        List <ZaehlerDatensatz> datensaetze = getAllRecordsOfCustomer(kundennr);
        String[][] daten = new String[datensaetze.size()][4];
        for (int i = 0; i < datensaetze.size(); i++) {

                daten[i][0] = PropertyManagementApplication.convertDateToString(datensaetze.get(i).getDatum());
                daten[i][1] = capitalize(datensaetze.get(i).getZaehlerart().name());
                daten[i][2] = datensaetze.get(i).getZaehlernummer();
                daten[i][3] = String.valueOf(datensaetze.get(i).getZaehlerstand());

        }

        String[] spaltennamen = { "Datum", "Zählerart", "Zählernummer", "Zählerstand" };

        // Tabelle initialisieren
        tabelle = new JTable(daten, spaltennamen);
        tabelle.setBounds(30, 40, 200, 500);
        sortTable();

        tabelle.setAutoCreateRowSorter(true);

        JScrollPane sp = new JScrollPane(tabelle);
        con.add(sp, BorderLayout.CENTER);

        setSize(500, 500);
        setVisible(true);
    }

    private List<ZaehlerDatensatz> getAllRecordsOfCustomer(int customerId) {
        return this.sessionData
                .stream()
                .filter(zaehlerDatensatz -> zaehlerDatensatz.getKundennummer() == customerId)
                .collect(Collectors.toList());
    }

    private String capitalize(String name) {
        return name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
    }

    private void sortTable() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabelle.getModel());
        tabelle.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexForZaehlerart = 2;
        sortKeys.add(new RowSorter.SortKey(columnIndexForZaehlerart, SortOrder.ASCENDING));

        int columnIndexForDate = 1;
        sortKeys.add(new RowSorter.SortKey(columnIndexForDate, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

}
