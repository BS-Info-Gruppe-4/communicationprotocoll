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
import java.io.IOException;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
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
    private final JButton deleteButton = new JButton("Löschen");
    private final JButton newCustomerButton = new JButton("Neuer Kunde");
    private final JButton resetFilter = new JButton("Filter zurücksetzen");
    private UtilDateModel datemodel_start_date, datemodel_end_date;
    private final JButton newReadingButton = new JButton("Neue Ablesung");
    private final JButton showReadingsSelectedCustomerButton = new JButton("Filter Ablesungen");
    private final JButton editReadingButton = new JButton("Ablesung bearbeiten");
    DefaultTableModel model = new DefaultTableModel();
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
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 6));
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(newReadingButton);
        buttonPanel.add(editCustomerButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetFilter);

        // Datepicker
        final JPanel datumPanel = new JPanel(new GridLayout(1, 2));
        datumPanel.setBorder(BorderFactory.createTitledBorder("Datum"));

        datemodel_start_date = new UtilDateModel();
        JDatePanelImpl datePanel_start_date = new JDatePanelImpl(datemodel_start_date);
        JDatePickerImpl datePicker_start_date = new JDatePickerImpl(datePanel_start_date, new DatePickerFormatter());
        datePicker_start_date.setBorder(BorderFactory.createTitledBorder("von"));
        datumPanel.add(datePicker_start_date);
        datemodel_start_date.setSelected(true); // Setzt das heutige Datum in das Datumsfeld ein

        datemodel_end_date = new UtilDateModel();
        JDatePanelImpl datePanel_end_date = new JDatePanelImpl(datemodel_end_date);
        JDatePickerImpl datePicker_end_date = new JDatePickerImpl(datePanel_end_date, new DatePickerFormatter());
        datePicker_end_date.setBorder(BorderFactory.createTitledBorder("bis"));
        datumPanel.add(datePicker_end_date);
        datemodel_end_date.setSelected(true); // Setzt das heutige Datum in das Datumsfeld ein

        buttonPanel.add(showReadingsSelectedCustomerButton);
        buttonPanel.add(editReadingButton);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        final JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        // Erzeuge die Tabellen-Header
        Object[] columns = {"ID", "Vorname", "Nachname"};
        Object[] columns_readings = {"Kundennummer", "Datum", "Zählernummer", "Zählerstand", "Neu eingebaut?","Kommentar", "Ablesung-ID"};

        model.setColumnIdentifiers(columns);
        table_customers.setModel(model);
        model_readings.setColumnIdentifiers(columns_readings);
        table_readings.setModel(model_readings);

        loadInitialCustomerTableData();
        loadInitialTableDataReadings();

        // Erstelle Sorter
        sorter = new TableRowSorter<>(model);
        table_customers.setRowSorter(sorter);
        sorter_readings = new TableRowSorter<>(model_readings);
        table_readings.setRowSorter(sorter_readings);

        // Sortiere Tabelle nach ID aufsteigend
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        // Füge die Tabelle zum Fenster hinzu
        JScrollPane scrollPane = new JScrollPane(table_customers);
        tablePanel.add(scrollPane);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kunden"));
        JScrollPane scrollPane_reading = new JScrollPane(table_readings);
        tablePanel.add(scrollPane_reading);
        scrollPane_reading.setBorder(BorderFactory.createTitledBorder("Ablesungen"));

        add(buttonPanel, BorderLayout.SOUTH);
        centerPanel.add(datumPanel, BorderLayout.NORTH);

        newCustomerButton.addActionListener(e -> new KundeErstellenDialog(this));

        newReadingButton.addActionListener(e -> openNewReadingsWindow());

        deleteButton.addActionListener(e -> attemptCustomerDeletion());

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

        showReadingsSelectedCustomerButton.addActionListener(e -> {
            start_date = LocalDate.of(datePicker_start_date.getModel().getYear(), datePicker_start_date.getModel().getMonth() + 1, datePicker_start_date.getModel().getDay());
            end_date = LocalDate.of(datemodel_end_date.getYear(), datemodel_end_date.getMonth() + 1, datemodel_end_date.getDay());
            showReadingsForSelectedCustomer(start_date, end_date);
        });

        resetFilter.addActionListener(e -> {
            table_customers.clearSelection();
            table_readings.clearSelection();

            loadInitialCustomerTableData();
            loadInitialTableDataReadings();
        });

        editReadingButton.addActionListener(e -> openEditReadingsWindow());

        // Passe die Größe des Fensters an
        setSize(1500, 600);
        setVisible(true);
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

        String dateAsString = table_readings.getValueAt(selectedReadingsRow, 1).toString();
        String customerId =  table_readings.getValueAt(selectedReadingsRow, 0).toString();

        UUID readingId = UUID.fromString(table_readings.getValueAt(selectedReadingsRow, 6).toString());
        Kunde customerOfReading = customerId.equals("null") ? null : customerService.getCustomerById(UUID.fromString(customerId));
        String zaehlernummer = table_readings.getValueAt(selectedReadingsRow, 2).toString();
        LocalDate date = convertStringToDate(dateAsString);
        int zaehlerstand = Integer.parseInt(table_readings.getValueAt(selectedReadingsRow,3).toString());
        boolean wurdeNeuEingebaut = (boolean) table_readings.getValueAt(selectedReadingsRow,4);
        String kommentar = table_readings.getValueAt(selectedReadingsRow,5).toString();

        return new Ablesung(readingId, zaehlernummer, date, customerOfReading, kommentar, wurdeNeuEingebaut, zaehlerstand);
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
            refreshTable();
            MessageDialog.showSuccessMessage("Kunde wurde erfolgreich gelöscht");
        }
        catch (NotFoundException | UnknownError ex) {
            MessageDialog.showErrorMessage(ex.getMessage());
        }
        
    }

    public void loadInitialCustomerTableData() {

        model.setRowCount(0);

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
        model_readings.setRowCount(0);

        for (Ablesung reading : readings) {
            String customerIdAsString = reading.getKunde() == null ? "null" : reading.getKunde().getId().toString();

            Object[] row = {customerIdAsString, reading.getDatum(), reading.getZaehlernummer(), reading.getZaehlerstand(), reading.isNeuEingebaut(),reading.getKommentar(), reading.getId()};
            model_readings.addRow(row);
        }

        model_readings.fireTableDataChanged();
    }

    public void showReadingsForSelectedCustomer(LocalDate start, LocalDate end) {
        int selectedRow = table_customers.getSelectedRow();
        boolean noRowIsSelected = selectedRow == -1;

        if (noRowIsSelected) {
            MessageDialog.showErrorMessage("Bitte wähle einen Kunden aus.");
            return;
        }

        ArrayList <Ablesung> current_readings = new ArrayList<>();
        String customerId = table_customers.getValueAt(selectedRow, USER_ID_COLUMN_INDEX).toString();
        try {
            current_readings = readingService.getReadingsWithRestrictions(UUID.fromString(customerId), start, end);
            refreshTableReadings(current_readings);
        }
        catch (NotFoundException | UnknownError ex) {
            MessageDialog.showErrorMessage(ex.getMessage());
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

    public void refreshTableReadings(ArrayList<Ablesung> readings) {

        model_readings.setRowCount(0);

        for (Ablesung reading : readings) {
            String customerIdAsString = reading.getKunde() == null ? "null" : reading.getKunde().getId().toString();

            Object[] row = {customerIdAsString, reading.getDatum(), reading.getZaehlernummer(), reading.getZaehlerstand(), reading.isNeuEingebaut(),reading.getKommentar(), reading.getId()};
            model_readings.addRow(row);
        }

        model_readings.fireTableDataChanged();
    }

    static LocalDate convertStringToDate(String dateAsString) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.parse(dateAsString, dateTimeFormatter);
    }

    private void exit() {
        Server.stopServer(true);
        System.exit(0);
    }
}
