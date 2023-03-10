package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.gui.service.ReadingService;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KundenTabelleWindow extends JFrame{

    private final JTable tabelle;
    DefaultTableModel model = new DefaultTableModel();

    KundenTabelleWindow(UUID uuid, LocalDate start, LocalDate end) throws IOException {
        ReadingService readingService = new ReadingService();
        setTitle("Datensätze für Kundennummer: " + uuid + " (" + start + " - " + end + ")");

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        String[] columns = { "UUID","Kunde", "Zählerart", "Zählernummer", "Zählerstand" , "neu eingebaut", "Kommentar"};

        // Tabelle initialisieren
        tabelle = new JTable();
        model.setColumnIdentifiers(columns);
        tabelle.setModel(model);

        ArrayList<Ablesung> datensaetze = readingService.getReadingsWithRestrictions(uuid, start, end);

        for (Ablesung ablesung : datensaetze) {
            Object[] row = {ablesung.getId(), ablesung.getKunde(), ablesung.getDatum(), ablesung.getZaehlernummer(), ablesung.getZaehlerstand(), ablesung.getId(), ablesung.getKommentar()};
            model.addRow(row);
        }

        tabelle.setBounds(30, 40, 200, 500);
        sortTable();

        tabelle.setAutoCreateRowSorter(true);

        JScrollPane sp = new JScrollPane(tabelle);
        con.add(sp, BorderLayout.CENTER);

        setSize(700, 600);
        setVisible(true);
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
