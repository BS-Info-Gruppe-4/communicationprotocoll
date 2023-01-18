package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.server.Server;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
public class PropertyManagementApplication extends JFrame {

    private final ArrayList<ZaehlerDatensatz> sessionData = new ArrayList<>();
    private final JsonFileManager jsonFileManager = new JsonFileManager();
    private InputMask inputMask = new InputMask(this);
    private JButton btn_speichern;
    private JButton btn_kunde_auswaehlen;
    private JButton btn_alle_anzeigen;
    private JButton btn_Kunde_neu;
    private JMenu menu_file, menu_about, menu_settings, submenu_themes;
    private JMenuItem item_exit, item_about, item_nimbus, item_windows, item_metal, item_motif;
    private JMenuBar menubar;

    public PropertyManagementApplication() {

        super("Property Management");

        assembleJFrameElements();
        loadExistingDataIntoSessionStorage();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                exit();
            }
        });

        btn_speichern.addActionListener(e -> inputMask.save());
        btn_alle_anzeigen.addActionListener(e -> new DatenWindow(this));
        btn_kunde_auswaehlen.addActionListener(e -> new KundenAuswahlDialog(getSessionData()));
        btn_Kunde_neu.addActionListener(e -> new KundeErstellenDialog());
    }

    private void loadExistingDataIntoSessionStorage() {
        ArrayList<ZaehlerDatensatz> fileContent = jsonFileManager.getContentOfJsonFile();
        sessionData.addAll(fileContent);
    }

    static LocalDate convertStringToDate(String dateAsString) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMANY);

        return LocalDate.parse(dateAsString, dateTimeFormatter);
    }

    static String convertDateToString(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return date.format(formatter);
    }

    private void assembleJFrameElements() {

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_buttons = new JPanel(new GridLayout(1, 4));

        pn_buttons.add(btn_speichern = new JButton("Speichern"));
        pn_buttons.add(btn_kunde_auswaehlen = new JButton("Kunde auswählen"));
        pn_buttons.add(btn_alle_anzeigen = new JButton("Alle Daten anzeigen"));
        pn_buttons.add(btn_Kunde_neu = new JButton("neuer Kunde"));

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
                    SwingUtilities.updateComponentTreeUI(PropertyManagementApplication.this);
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
                    SwingUtilities.updateComponentTreeUI(PropertyManagementApplication.this);
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
                    SwingUtilities.updateComponentTreeUI(PropertyManagementApplication.this);
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
                    SwingUtilities.updateComponentTreeUI(PropertyManagementApplication.this);
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

        con.add(menubar, BorderLayout.NORTH);
        con.add(inputMask.getMainContainer(), BorderLayout.CENTER);
        con.add(pn_buttons, BorderLayout.SOUTH);

        setSize(630, 300);
        setVisible(true);
    }

    public void addToSessionData(ZaehlerDatensatz zaehlerDatensatz) {
        sessionData.add(zaehlerDatensatz);
    }

    public void removeDatensatzFromSession(ZaehlerDatensatz zaehlerDatensatz) {
        try {
            sessionData.removeIf(zaehlerDatensatz::equals);
        }
        catch (Exception e) {
            MessageDialog.showErrorMessage("Datensatz konnte nicht entfernt werden");
        }
    }

    private void exit() {
        Server.stopServer(true);
        System.exit(0);
    }
}