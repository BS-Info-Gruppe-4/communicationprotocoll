package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.gui.frames.NewReadingInputWindow;
import eu.bsinfo.gruppe4.gui.persistence.EditCustomerDataWindow;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.gui.service.CustomerService;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.NotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;


public class AllCustomersTable extends JFrame {

    public static final int USER_ID_COLUMN_INDEX = 0;
    private final SessionStorage sessionStorage = SessionStorage.getInstance();
    private final CustomerService customerService = new CustomerService();
    private final JTable table;
    private final JTable table_readings;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final TableRowSorter<DefaultTableModel> sorter_readings;
    private final JButton editButton = new JButton("Bearbeiten");
    private final JButton deleteButton = new JButton("Löschen");
    private final JButton newCustomerButton = new JButton("Neuer Kunde");
    private final JButton newReadingButton = new JButton("Neue Ablesung");
    private final JButton showReadingsSelectedCustomer = new JButton("zeige Ablesungen");
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel model_readings = new DefaultTableModel();

    public AllCustomersTable() {
        setTitle("Kundenliste");

        // Erzeuge die Tabelle
        table = new JTable();
        table_readings = new JTable();

        final JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(newReadingButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        final JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        add(tablePanel, BorderLayout.CENTER);

        // Erzeuge die Tabellen-Header
        Object[] columns = {"ID", "Vorname", "Nachname"};
        Object[] columns_readings = {"Kundennummer", "Datum", "Zählernummer", "Zählerstand", "Kommentar"};

        model.setColumnIdentifiers(columns);
        table.setModel(model);
        model_readings.setColumnIdentifiers(columns_readings);
        table_readings.setModel(model_readings);

        loadInitialTableData();
        loadInitialTableDataReadings();

        // Erstelle Sorter
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter_readings = new TableRowSorter<>(model_readings);
        table_readings.setRowSorter(sorter_readings);

        // Sortiere Tabelle nach ID aufsteigend
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        // Füge die Tabelle zum Fenster hinzu
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        JScrollPane scrollPane_reading = new JScrollPane(table_readings);
        tablePanel.add(scrollPane_reading);

        add(buttonPanel, BorderLayout.SOUTH);

        newCustomerButton.addActionListener(e -> new KundeErstellenDialog(this));

        newReadingButton.addActionListener(e -> openNewReadingsWindow());

        deleteButton.addActionListener(e -> attemptCustomerDeletion());

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            String customerId = "";
            if(selectedRow >= 0) {
                customerId = table.getValueAt(selectedRow, 0).toString();

            } else {
                MessageDialog.showErrorMessage("kein Kunde ausgewählt");
                return;
            }

            Kunde customerSelected = new Kunde(UUID.fromString(customerId),
                    table.getValueAt(selectedRow, 2).toString(),
                    table.getValueAt(selectedRow, 1).toString());
            new EditCustomerDataWindow(customerSelected, this);
        });

        showReadingsSelectedCustomer.addActionListener(e -> showReadingsForSelectedCustomer());

        // Passe die Größe des Fensters an
        setSize(1000, 500);
        setVisible(true);
    }

    private void openNewReadingsWindow() {
        int selectedRow = table.getSelectedRow();
        String customerId = "";
        if(selectedRow >= 0) {
            customerId = table.getValueAt(selectedRow, 0).toString();

        } else {
            MessageDialog.showErrorMessage("kein Kunde ausgewählt");
            return;
        }

        Kunde selectedCustomer = new Kunde(UUID.fromString(customerId),
                table.getValueAt(selectedRow, 2).toString(),
                table.getValueAt(selectedRow, 1).toString());

        new NewReadingInputWindow(selectedCustomer);
    }


    private void attemptCustomerDeletion() {
        int selectedRow = table.getSelectedRow();
        boolean noRowIsSelected = selectedRow == -1;

        if (noRowIsSelected) {
            MessageDialog.showErrorMessage("Bitte wähle einen Kunden aus.");
            return;
        }

        String customerId = table.getValueAt(selectedRow, USER_ID_COLUMN_INDEX).toString();

        try {
            customerService.deleteCustomerById(UUID.fromString(customerId));
            refreshTable();
            MessageDialog.showSuccessMessage("Kunde wurde erfolgreich gelöscht");
        }
        catch (NotFoundException | UnknownError ex) {
            MessageDialog.showErrorMessage(ex.getMessage());
        }
        
    }

    public void loadInitialTableData() {

        var customers = sessionStorage.getKunden();

        // Füge die Kunden zur Tabelle hinzu
        for (Kunde customer : customers) {
            Object[] row = {customer.getId(), customer.getVorname(), customer.getName()};
            model.addRow(row);
        }

    }

    public void loadInitialTableDataReadings() {

        var readings = sessionStorage.getAblesungen();

        // Füge alle Ablesungen zur Tabelle hinzu
        for (Ablesung reading : readings) {
            Object[] row = {reading.getId(), reading.getDatum(), reading.getZaehlernummer(), reading.getZaehlerstand(), reading.getKommentar()};
            model_readings.addRow(row);
        }
    }

    public void showReadingsForSelectedCustomer() {
        int selectedRow = table_readings.getSelectedRow();
        boolean noRowIsSelected = selectedRow == -1;

        if (noRowIsSelected) {
            MessageDialog.showErrorMessage("Bitte wähle einen Kunden aus.");
            return;
        }

        String customerId = table.getValueAt(selectedRow, USER_ID_COLUMN_INDEX).toString();

    }

    public void refreshTable() {
        sessionStorage.syncWithBackend();
        model.setRowCount(0);

        for (Kunde customer : sessionStorage.getKunden()) {
            Object[] row = {customer.getId(), customer.getVorname(), customer.getName()};
            model.addRow(row);
        }

        model.fireTableDataChanged();
    }
}
