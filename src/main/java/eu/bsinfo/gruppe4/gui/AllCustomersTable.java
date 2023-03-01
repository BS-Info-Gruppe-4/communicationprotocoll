package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.gui.persistence.EditCustomerDataWindow;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.gui.service.CustomerService;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

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
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JButton editButton = new JButton("Bearbeiten");
    private final JButton deleteButton = new JButton("Löschen");
    private final JButton newCustomerButton = new JButton("Neuer Kunde");
    DefaultTableModel model = new DefaultTableModel();

    public AllCustomersTable() {
        setTitle("Kundenliste");

        // Erzeuge die Tabelle
        table = new JTable();

        final JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Erzeuge die Tabellen-Header
        Object[] columns = {"ID", "Vorname", "Nachname"};

        model.setColumnIdentifiers(columns);
        table.setModel(model);

        loadInitialTableData();

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

        newCustomerButton.addActionListener(e -> new KundeErstellenDialog(this));

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
                    table.getValueAt(selectedRow, 1).toString(),
                    table.getValueAt(selectedRow, 2).toString());
            new EditCustomerDataWindow(customerSelected, this);
        });

        // Passe die Größe des Fensters an
        setSize(500, 300);
        setVisible(true);
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
