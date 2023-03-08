package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.gui.frames.NewReadingInputWindow;
import eu.bsinfo.gruppe4.gui.persistence.EditCustomerDataWindow;
import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.gui.service.CustomerService;
import eu.bsinfo.gruppe4.gui.service.ReadingService;
import eu.bsinfo.gruppe4.server.Server;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
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
import java.util.ArrayList;
import java.util.UUID;


public class AllCustomersTable extends JFrame {

    public static final int USER_ID_COLUMN_INDEX = 0;
    private final SessionStorage sessionStorage = SessionStorage.getInstance();
    private final CustomerService customerService = new CustomerService();
    private final ReadingService readingService = new ReadingService();
    private final JTable table;
    private final JTable table_readings;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final TableRowSorter<DefaultTableModel> sorter_readings;
    private final JButton editCustomerButton = new JButton("Kunde bearbeiten");
    private final JButton deleteButton = new JButton("Löschen");
    private final JButton newCustomerButton = new JButton("Neuer Kunde");
    private final JButton showDataButton = new JButton("Ablesungen anzeigen");
    private UtilDateModel datemodel, datemodel1;
    private final JButton newReadingButton = new JButton("Neue Ablesung");
    private final JButton showReadingsSelectedCustomer = new JButton("zeige Ablesungen");
    private final JButton editReadingButton = new JButton("Ablesung bearbeiten");
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel model_readings = new DefaultTableModel();
    private JMenu menu_file, menu_about, menu_settings, submenu_themes;
    private JMenuItem item_exit, item_about, item_nimbus, item_windows, item_metal, item_motif;
    private JMenuBar menubar;

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
        table = new JTable();
        table_readings = new JTable();

        // Buttons
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 6));
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(newReadingButton);
        buttonPanel.add(editCustomerButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showDataButton);

        // Datepicker
        final JPanel datumPanel = new JPanel(new GridLayout(1, 2));
        datemodel = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(datemodel);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DatePickerFormatter());
        datePicker.setBorder(BorderFactory.createTitledBorder("von"));
        datumPanel.add(datePicker);
        datumPanel.setBorder(BorderFactory.createTitledBorder("Datum"));
        datemodel1 = new UtilDateModel();
        JDatePanelImpl datePanel1 = new JDatePanelImpl(datemodel1);
        JDatePickerImpl datePicker1 = new JDatePickerImpl(datePanel1, new DatePickerFormatter());
        datePicker1.setBorder(BorderFactory.createTitledBorder("bis"));
        datumPanel.add(datePicker1);
        datemodel.setSelected(true); // Setzt das heutige Datum in das Datumsfeld ein
        datemodel1.setSelected(true);
        LocalDate start_date = LocalDate.of(datemodel.getYear(), datemodel.getMonth() + 1, datemodel.getDay());
        LocalDate end_date = LocalDate.of(datemodel1.getYear(), datemodel1.getMonth() + 1, datemodel1.getDay());
        buttonPanel.add(showReadingsSelectedCustomer);
        buttonPanel.add(editReadingButton);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        final JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(tablePanel, BorderLayout.CENTER);

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
        centerPanel.add(datumPanel, BorderLayout.NORTH);

        newCustomerButton.addActionListener(e -> new KundeErstellenDialog(this));

        newReadingButton.addActionListener(e -> openNewReadingsWindow());

        deleteButton.addActionListener(e -> attemptCustomerDeletion());

        editCustomerButton.addActionListener(e -> {
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

        showDataButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            boolean noRowIsSelected = selectedRow == -1;

            if (noRowIsSelected) {
                MessageDialog.showErrorMessage("Bitte wähle einen Kunden aus.");
                return;
            }

            String customerId = table.getValueAt(selectedRow, USER_ID_COLUMN_INDEX).toString();

            try {
                new KundenTabelleWindow(UUID.fromString(customerId), start_date, end_date);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Passe die Größe des Fensters an
        setSize(1300, 700);
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
        int selectedRow = table.getSelectedRow();
        boolean noRowIsSelected = selectedRow == -1;

        if (noRowIsSelected) {
            MessageDialog.showErrorMessage("Bitte wähle einen Kunden aus.");
            return;
        }

        ArrayList <Ablesung> current_readings = new ArrayList<>();
        String customerId = table.getValueAt(selectedRow, USER_ID_COLUMN_INDEX).toString();
        try {
            current_readings = readingService.getReadingsWithRestrictions(UUID.fromString(customerId), null, null);
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
            Object[] row = {reading.getId(), reading.getDatum(), reading.getZaehlernummer(), reading.getZaehlerstand(), reading.getKommentar()};
            model_readings.addRow(row);
        }

        model_readings.fireTableDataChanged();
    }

    private void exit() {
        Server.stopServer(true);
        System.exit(0);
    }
}
