package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.server.model.Kunde;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;


public class AllCustomersTable extends JFrame {

    private final JTable table;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JButton editButton = new JButton("Bearbeiten");
    private final JButton deleteButton = new JButton("Löschen");
    private final JButton newCustomerButton = new JButton("Neuer Kunde");

    public AllCustomersTable() {
        setTitle("Kundenliste");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erzeuge die Tabelle
        table = new JTable();

        final JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);


        // Erzeuge die Tabellen-Header
        Object[] columns = {"ID", "Vorname", "Nachname"};
        var model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        // Erzeuge eine Liste von Kunden
        ArrayList<Kunde> customers = new ArrayList<>();
        customers.add(new Kunde("Max", "Mustermann"));
        customers.add(new Kunde("Maria", "Müller"));
        customers.add(new Kunde("Hans", "Huber"));

        // Füge die Kunden zur Tabelle hinzu
        for (Kunde customer : customers) {
            Object[] row = {customer.getId(), customer.getVorname(), customer.getName()};
            model.addRow(row);
        }

        // Erstelle Sorter
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Sortiere Tabelle nach ID aufsteigend
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        // Füge die Tabelle zum Fenster hinzu
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);

        // Passe die Größe des Fensters an
        setSize(500, 300);
        setVisible(true);
    }
}
