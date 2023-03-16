package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.gui.frames.EditReadingInputWindow;
import eu.bsinfo.gruppe4.gui.frames.NewReadingInputWindow;
import eu.bsinfo.gruppe4.gui.persistence.EditCustomerDataWindow;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.gui.service.CustomerService;
import eu.bsinfo.gruppe4.gui.service.ReadingService;
import eu.bsinfo.gruppe4.server.Server;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.NotFoundException;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;


public class AllCustomersTable extends JFrame {

    public static final int USER_ID_COLUMN_INDEX = 0;
    private final SessionStorage sessionStorage = SessionStorage.getInstance();
    private final CustomerService customerService = new CustomerService();
    private final ReadingService readingService = new ReadingService();
    private final JTable table_customers;
    private final JTable table_readings;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final TableRowSorter<DefaultTableModel> sorter_readings;
    private final JButton editCustomerButton = new JButton("Kunde bearbeiten");
    private final JButton deleteUserButton = new JButton("Kunde löschen");
    private final JButton newCustomerButton = new JButton("Neuer Kunde");
    private final JButton resetFilterButton = new JButton("Filter zurücksetzen");
    private UtilDateModel datemodel_start_date, datemodel_end_date;
    private final JButton newReadingButton = new JButton("Neue Ablesung");
    private final JButton filterReadingsButton = new JButton("Filter Ablesungen");
    private final JButton editReadingButton = new JButton("Ablesung bearbeiten");
    private final JButton deleteReadingButton = new JButton("Ablesung löschen");
    private final JTextField tf_filterCustomer = new JTextField();
    DefaultTableModel customerTableModel = new DefaultTableModel();
    DefaultTableModel model_readings = new DefaultTableModel();
    private JMenu menu_file, menu_about, menu_settings, submenu_themes;
    private JMenuItem item_exit, item_about, item_nimbus, item_windows, item_metal, item_motif;
    private JMenuBar menubar;
    private LocalDate start_date, end_date;

    public AllCustomersTable() {
        setTitle("Kundenliste");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                exit();
            }
        });

        // assemble menubar
        menubar = new JMenuBar();
        menu_file = new JMenu("File");
        menu_settings = new JMenu("Settings");
        menu_about = new JMenu("About");
        submenu_themes = new JMenu("Themes");
        item_exit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        item_about = new JMenuItem(new AbstractAction("About us") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutUsWindow();
            }
        });
        item_nimbus = new JMenuItem(new AbstractAction("Nimbus") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                    UIManager.put("control", new Color(128, 128, 128));
                    UIManager.put("info", new Color(128, 128, 128));
                    UIManager.put("nimbusBase", new Color(18, 30, 49));
                    UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
                    UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
                    UIManager.put("nimbusFocus", new Color(115, 164, 209));
                    UIManager.put("nimbusGreen", new Color(176, 179, 50));
                    UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
                    UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
                    UIManager.put("nimbusOrange", new Color(191, 98, 4));
                    UIManager.put("nimbusRed", new Color(169, 46, 34));
                    UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
                    UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
                    UIManager.put("text", new Color(230, 230, 230));
                    SwingUtilities.updateComponentTreeUI(AllCustomersTable.this);
                } catch (UnsupportedLookAndFeelException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame.setDefaultLookAndFeelDecorated(true);
            }
        });
        item_windows = new JMenuItem(new AbstractAction("Windows") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(AllCustomersTable.this);
                } catch (UnsupportedLookAndFeelException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame.setDefaultLookAndFeelDecorated(true);
            }
        });
        item_metal = new JMenuItem(new AbstractAction("Metal") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(AllCustomersTable.this);
                } catch (UnsupportedLookAndFeelException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame.setDefaultLookAndFeelDecorated(true);
            }
        });
        item_motif = new JMenuItem(new AbstractAction("Motif") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(AllCustomersTable.this);
                } catch (UnsupportedLookAndFeelException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame.setDefaultLookAndFeelDecorated(true);
            }
        });
        menu_file.add(item_exit);
        menu_about.add(item_about);
        menubar.add(menu_file);
        menubar.add(menu_settings);
        menubar.add(menu_about);

        // themes submenu
        menu_settings.add(submenu_themes);
        submenu_themes.add(item_nimbus);
        submenu_themes.add(item_metal);
        submenu_themes.add(item_windows);
        submenu_themes.add(item_motif);

        add(menubar, BorderLayout.NORTH);

        // Erzeuge die Tabelle
        table_customers = new JTable();
        table_readings = new JTable();

        // Buttons
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 7));
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(newReadingButton);
        buttonPanel.add(editCustomerButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(resetFilterButton);

        // Datepicker
        final JPanel filterPanel = new JPanel(new GridLayout(1, 3));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));

        datemodel_start_date = new UtilDateModel();
        JDatePanelImpl datePanel_start_date = new JDatePanelImpl(datemodel_start_date);
        JDatePickerImpl datePicker_start_date = new JDatePickerImpl(datePanel_start_date, new DatePickerFormatter());
        datePicker_start_date.setBorder(BorderFactory.createTitledBorder(" Datum von"));
        filterPanel.add(datePicker_start_date);
        datemodel_start_date.setValue(null);
        datemodel_start_date.setSelected(false); // Setzt das heutige Datum in das Datumsfeld ein

        datemodel_end_date = new UtilDateModel();
        JDatePanelImpl datePanel_end_date = new JDatePanelImpl(datemodel_end_date);
        JDatePickerImpl datePicker_end_date = new JDatePickerImpl(datePanel_end_date, new DatePickerFormatter());
        datePicker_end_date.setBorder(BorderFactory.createTitledBorder("bis"));
        filterPanel.add(datePicker_end_date);
        datemodel_end_date.setValue(null);
        datemodel_end_date.setSelected(false); // Setzt das heutige Datum in das Datumsfeld ein

        // Kundennummer Filter
        JPanel pn_filterCustomer = new JPanel(new GridLayout(1, 2));
        JButton btn_filterCustomer = new JButton("Kunden filtern");
        pn_filterCustomer.setBorder(BorderFactory.createTitledBorder("Kundennummer"));
        pn_filterCustomer.add(tf_filterCustomer);
        pn_filterCustomer.add(btn_filterCustomer);
        filterPanel.add(pn_filterCustomer);
        btn_filterCustomer.addActionListener(e -> filterByCustomerNumber(tf_filterCustomer.getText()));

        buttonPanel.add(filterReadingsButton);
        buttonPanel.add(editReadingButton);
        buttonPanel.add(deleteReadingButton);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        final JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        // Erzeuge die Tabellen-Header
        Object[] columns = {"ID", "Vorname", "Nachname"};
        Object[] columns_readings = {"Kundennummer", "Datum", "Zählernummer", "Zählerstand", "Neu eingebaut?","Kommentar", "Ablesung-ID"};

        customerTableModel.setColumnIdentifiers(columns);
        table_customers.setModel(customerTableModel);
        model_readings.setColumnIdentifiers(columns_readings);
        table_readings.setModel(model_readings);

        loadInitialCustomerTableData();
        loadInitialTableDataReadings();

        // Erstelle Sorter
        sorter = new TableRowSorter<>(customerTableModel);
        table_customers.setRowSorter(sorter);
        sorter_readings = new TableRowSorter<>(model_readings);
        table_readings.setRowSorter(sorter_readings);

        // Sortiere Kunden-Tabelle nach ID aufsteigend
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        // Sortiere Ablesung-Tabelle nach Datum absteigend
        ArrayList<RowSorter.SortKey> readingsSortKeys = new ArrayList<>();
        readingsSortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter_readings.setSortKeys(readingsSortKeys);

        // Füge die Tabelle zum Fenster hinzu
        JScrollPane scrollPane = new JScrollPane(table_customers);
        tablePanel.add(scrollPane);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kunden"));
        JScrollPane scrollPane_reading = new JScrollPane(table_readings);
        tablePanel.add(scrollPane_reading);
        scrollPane_reading.setBorder(BorderFactory.createTitledBorder("Ablesungen"));

        add(buttonPanel, BorderLayout.SOUTH);
        centerPanel.add(filterPanel, BorderLayout.NORTH);

        newReadingButton.addActionListener(e -> openNewReadingsWindow());
        editReadingButton.addActionListener(e -> openEditReadingsWindow());
        deleteUserButton.addActionListener(e -> attemptCustomerDeletion());

        //TODO: Maybe use observer pattern for notifying table on changes
        newCustomerButton.addActionListener(e -> new KundeErstellenDialog(this));
        editCustomerButton.addActionListener(e -> {
            int selectedRow = table_customers.getSelectedRow();
            String customerId = "";
            if(selectedRow >= 0) {
                customerId = table_customers.getValueAt(selectedRow, 0).toString();

            } else {
                MessageDialog.showErrorMessage("kein Kunde ausgewählt");
                return;
            }

            Kunde customerSelected = new Kunde(UUID.fromString(customerId),
                    table_customers.getValueAt(selectedRow, 2).toString(),
                    table_customers.getValueAt(selectedRow, 1).toString());
            new EditCustomerDataWindow(customerSelected, this);
        });

        deleteReadingButton.addActionListener(e -> attemptReadingDeletion());

        filterReadingsButton.addActionListener(e -> filterReadings());
        resetFilterButton.addActionListener(e -> resetFilter());

        // Passe die Größe des Fensters an
        setSize(1500, 600);
        setVisible(true);
    }

    private void attemptReadingDeletion() {

        try {
            Ablesung selectedReading = getSelectedReading();
            readingService.deleteReading(selectedReading);
            filterReadings();

            MessageDialog.showSuccessMessage("Ablesung wurde erfolgreich gelöscht");
        }
        catch (Exception e) {
            MessageDialog.showErrorMessage(e.getMessage());
        }
    }

    private void filterReadings() {
        LocalDate startingDate = getSelectedStartingDate();
        LocalDate endingDate = getSelectedEndingDate();

        ArrayList <Ablesung> filteredReadings;
        String customerIdAsString = tf_filterCustomer.getText();

        try {
            UUID customerId = customerIdAsString.isEmpty() ? null : UUID.fromString(customerIdAsString);
            filteredReadings = readingService.getReadingsWithRestrictions(customerId, startingDate, endingDate);
            setReadingsTableData(filteredReadings);
        }
        catch (Exception ex) {
            MessageDialog.showErrorMessage(ex.getMessage());
        }
    }

    private void filterByCustomerNumber(String customerNumber) {
        Kunde customer;
        try {
            customer = customerService.getCustomerById(UUID.fromString(customerNumber));
            customerTableModel.setRowCount(0);
            Object[] row = {customer.getId(), customer.getVorname(), customer.getName()};
            customerTableModel.addRow(row);
        } catch (IllegalArgumentException ex) {
            MessageDialog.showErrorMessage("Kundennummer existiert nicht");
        }
    }

    private void resetFilter() {

        table_customers.clearSelection();
        table_readings.clearSelection();
        tf_filterCustomer.setText(null);

        this.datemodel_start_date.setValue(null);
        this.datemodel_end_date.setValue(null);

        loadInitialCustomerTableData();
        loadInitialTableDataReadings();
    }

    private LocalDate getSelectedStartingDate() {

        if (datemodel_start_date.getValue() == null) return null;

        return LocalDate.of(
                datemodel_start_date.getYear(),
                datemodel_start_date.getMonth() + 1,
                datemodel_start_date.getDay());
    }

    private LocalDate getSelectedEndingDate() {

        if (datemodel_end_date.getValue() == null) return null;

        return LocalDate.of(
                datemodel_end_date.getYear(),
                datemodel_end_date.getMonth() + 1,
                datemodel_end_date.getDay());
    }

    private void openEditReadingsWindow() {
        try {
            Ablesung selectedReading = getSelectedReading();
            new EditReadingInputWindow(selectedReading, this);
        }
        catch (Exception e) {
            MessageDialog.showErrorMessage(e.getMessage());
        }
    }

    private Ablesung getSelectedReading() throws Exception {
        int selectedReadingsRow = table_readings.getSelectedRow();

        if (selectedReadingsRow == -1) throw new Exception("Bitte wähle zuerst eine Ablesung aus");

        String customerId =  table_readings.getValueAt(selectedReadingsRow, 0).toString();
        Kunde customerOfReading = customerId.equals("null") ? null : customerService.getCustomerById(UUID.fromString(customerId));

        LocalDate date = (LocalDate) table_readings.getValueAt(selectedReadingsRow, 1);
        String zaehlernummer = table_readings.getValueAt(selectedReadingsRow, 2).toString();
        int zaehlerstand = Integer.parseInt(table_readings.getValueAt(selectedReadingsRow,3).toString());
        boolean wurdeNeuEingebaut = (boolean) table_readings.getValueAt(selectedReadingsRow,4);
        String kommentar = table_readings.getValueAt(selectedReadingsRow,5).toString();
        UUID readingId = UUID.fromString(table_readings.getValueAt(selectedReadingsRow, 6).toString());

        return new Ablesung(
                readingId,
                zaehlernummer,
                date,
                customerOfReading,
                kommentar,
                wurdeNeuEingebaut,
                zaehlerstand);
    }

    private void openNewReadingsWindow() {
        int selectedRow = table_customers.getSelectedRow();
        String customerId = "";
        if(selectedRow >= 0) {
            customerId = table_customers.getValueAt(selectedRow, 0).toString();

        } else {
            MessageDialog.showErrorMessage("kein Kunde ausgewählt");
            return;
        }

        Kunde selectedCustomer = new Kunde(UUID.fromString(customerId),
                table_customers.getValueAt(selectedRow, 2).toString(),
                table_customers.getValueAt(selectedRow, 1).toString());

        new NewReadingInputWindow(selectedCustomer, this);
    }


    private void attemptCustomerDeletion() {
        int selectedRow = table_customers.getSelectedRow();
        boolean noRowIsSelected = selectedRow == -1;

        if (noRowIsSelected) {
            MessageDialog.showErrorMessage("Bitte wähle einen Kunden aus.");
            return;
        }

        String customerId = table_customers.getValueAt(selectedRow, USER_ID_COLUMN_INDEX).toString();

        try {
            customerService.deleteCustomerById(UUID.fromString(customerId));
            refreshCustomerTable();
            filterReadings();
            MessageDialog.showSuccessMessage("Kunde wurde erfolgreich gelöscht");
        }
        catch (NotFoundException | UnknownError ex) {
            MessageDialog.showErrorMessage(ex.getMessage());
        }
        
    }

    public void loadInitialCustomerTableData() {

        customerTableModel.setRowCount(0);

        var customers = sessionStorage.getKunden();

        // Füge die Kunden zur Tabelle hinzu
        for (Kunde customer : customers) {
            Object[] row = {customer.getId(), customer.getVorname(), customer.getName()};
            customerTableModel.addRow(row);
        }

    }

    public void loadInitialTableDataReadings() {

        var readings = sessionStorage.getAblesungen();

        // Füge alle Ablesungen zur Tabelle hinzu
        model_readings.setRowCount(0);

        for (Ablesung reading : readings) {
            String customerIdAsString = reading.getKunde() == null ? "null" : reading.getKunde().getId().toString();

            Object[] row = {customerIdAsString, reading.getDatum(), reading.getZaehlernummer(), reading.getZaehlerstand(), reading.isNeuEingebaut(),reading.getKommentar(), reading.getId()};
            model_readings.addRow(row);
        }

        model_readings.fireTableDataChanged();
    }

    public void showFilteredReadings(LocalDate start, LocalDate end) {
        int selectedRow = table_customers.getSelectedRow();
        boolean noRowIsSelected = selectedRow == -1;

        if (noRowIsSelected) {
            MessageDialog.showErrorMessage("Bitte wähle einen Kunden aus.");
            return;
        }

        ArrayList <Ablesung> filteredReadings;
        String customerId = table_customers.getValueAt(selectedRow, USER_ID_COLUMN_INDEX).toString();

        try {
            filteredReadings = readingService.getReadingsWithRestrictions(UUID.fromString(customerId), start, end);
            setReadingsTableData(filteredReadings);
        }
        catch (Exception ex) {
            MessageDialog.showErrorMessage(ex.getMessage());
        }

    }

    public void refreshCustomerTable() {
        sessionStorage.syncWithBackend();
        customerTableModel.setRowCount(0);

        for (Kunde customer : sessionStorage.getKunden()) {
            Object[] row = {customer.getId(), customer.getVorname(), customer.getName()};
            customerTableModel.addRow(row);
        }

        customerTableModel.fireTableDataChanged();
    }

    public void setReadingsTableData(ArrayList<Ablesung> readings) {

        model_readings.setRowCount(0);

        for (Ablesung reading : readings) {
            String customerIdAsString = reading.getKunde() == null ? "null" : reading.getKunde().getId().toString();

            Object[] row = {customerIdAsString, reading.getDatum(), reading.getZaehlernummer(), reading.getZaehlerstand(), reading.isNeuEingebaut(),reading.getKommentar(), reading.getId()};
            model_readings.addRow(row);
        }

        model_readings.fireTableDataChanged();
    }

    private void exit() {
        Server.stopServer(true);
        System.exit(0);
    }
}
