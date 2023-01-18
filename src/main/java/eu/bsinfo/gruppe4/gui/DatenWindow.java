package eu.bsinfo.gruppe4.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DatenWindow extends JFrame {

    private JTable tabelle;
    private final PropertyManagementApplication propertyManagementApplication;
    private final int KUNDENNUMMER_COL_INDEX = 0;
    private final int DATUM_COL_INDEX = 1;
    private final int ZAEHLERART_COL_INDEX = 2;
    private final int ZAEHLERNUMMER_COL_INDEX = 3;
    private final int NEU_EINGEBAUT_COL_INDEX = 4;
    private final int ZAEHLERSTAND_COL_INDEX = 5;
    private final int KOMMENTAR_COL_INDEX = 6;

    DatenWindow(PropertyManagementApplication propertyManagementApplication) {

        this.propertyManagementApplication = propertyManagementApplication;

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_buttons = new JPanel(new GridLayout(1, 2));
        con.add(pn_buttons, BorderLayout.SOUTH);

        JButton btn_loeschen;
        pn_buttons.add(btn_loeschen = new JButton("Löschen"));
        JButton btn_bearbeiten;
        pn_buttons.add(btn_bearbeiten = new JButton("Bearbeiten"));

        fillTable();

        tabelle.setBounds(30, 40, 200, 500);

        JScrollPane scrollPane = new JScrollPane(tabelle);
        con.add(scrollPane, BorderLayout.CENTER);

        tabelle.setAutoCreateRowSorter(true);

        setSize(500, 500);
        setVisible(true);

        btn_bearbeiten.addActionListener(e -> edit());
        btn_loeschen.addActionListener(e -> delete());
    }

    private void fillTable() {
        List <ZaehlerDatensatz> datensaetze = getAllRecords();
        String[][] daten = new String[datensaetze.size()][7];
        for (int i = 0; i < datensaetze.size(); i++) {
            daten[i][0] = String.valueOf(datensaetze.get(i).getKundennummer());
            daten[i][1] = PropertyManagementApplication.convertDateToString(datensaetze.get(i).getDatum());
            daten[i][2] = capitalize(datensaetze.get(i).getZaehlerart().name());
            daten[i][3] = datensaetze.get(i).getZaehlernummer();
            daten[i][4] = booleanToStringMapper(datensaetze.get(i).isNeuEingebaut());
            daten[i][5] = String.valueOf(datensaetze.get(i).getZaehlerstand());
            daten[i][6] = datensaetze.get(i).getKommentar();
        }

        // Spaltennamen
        String[] spaltennamen = {
                "Kundennummer",
                "Datum",
                "Zählerart",
                "Zählernummer",
                "Neu eingebaut",
                "Zählerstand",
                "Kommentar"
        };

        // Tabelle initialisieren
        tabelle = new JTable(daten, spaltennamen);
        setTitle("Datensätze: " + datensaetze.size());
    }

    private void edit() {
        int selectedRowIndex = tabelle.getSelectedRow();

        // Bricht methode ab, wenn keine Reihe ausgewählt wurde
        if (selectedRowIndex == -1) return;

        ZaehlerDatensatz selectedZaehlerDatensatz = getZaehlerDatensatzOfRow(selectedRowIndex);

        EditingWindow editingWindow = new EditingWindow(propertyManagementApplication, selectedZaehlerDatensatz, this);

        editingWindow.withDefaultInputFields(
                getTableCellData(selectedRowIndex, KUNDENNUMMER_COL_INDEX),
                getTableCellData(selectedRowIndex, ZAEHLERART_COL_INDEX),
                getTableCellData(selectedRowIndex, ZAEHLERNUMMER_COL_INDEX),
                PropertyManagementApplication.convertStringToDate(getTableCellData(selectedRowIndex, DATUM_COL_INDEX)),
                stringToBooleanMapper(getTableCellData(selectedRowIndex, NEU_EINGEBAUT_COL_INDEX)),
                getTableCellData(selectedRowIndex, ZAEHLERSTAND_COL_INDEX),
                getTableCellData(selectedRowIndex, KOMMENTAR_COL_INDEX)
        );
    }

    private void delete() {
        int selectedRowIndex = tabelle.getSelectedRow();

        // Bricht methode ab, wenn keine Reihe ausgewählt wurde
        if (selectedRowIndex == -1) return;

        ZaehlerDatensatz selectedZaehlerDatensatz = getZaehlerDatensatzOfRow(selectedRowIndex);

        if (!doesUserConfirmDeletionOfDatensatz(selectedZaehlerDatensatz)) return;

        propertyManagementApplication.removeDatensatzFromSession(selectedZaehlerDatensatz);

        MessageDialog.showSuccessMessage("Datensatz wurde entfernt");

        // workaround zum aktualisieren der tabelle
        dispose();
        new DatenWindow(propertyManagementApplication);
    }

    public boolean doesUserConfirmDeletionOfDatensatz(ZaehlerDatensatz zaehlerDatensatz) {
        String message = "Bist du dir sicher, dass du folgenden Datensatz löschen möchtest?\n" +
                "Kundennummer: " + zaehlerDatensatz.getKundennummer() + "\n" +
        "Zählerart: " + zaehlerDatensatz.getZaehlerart() + "\n" +
                "Zählernummer: " + zaehlerDatensatz.getZaehlernummer() + "\n" +
                "Datum: " + PropertyManagementApplication.convertDateToString(zaehlerDatensatz.getDatum()) + "\n" +
                "Neu eingebaut: " + zaehlerDatensatz.isNeuEingebaut() + "\n" +
                "Zählerstand: " + zaehlerDatensatz.getZaehlerstand() + "\n"+
                "Kommentar: " + zaehlerDatensatz.getKommentar();

        int reply = JOptionPane.showConfirmDialog(
                null,
                message,
                "Datensatz löschen?",
                JOptionPane.YES_NO_OPTION);

        return reply == JOptionPane.YES_OPTION;
    }

    private ZaehlerDatensatz getZaehlerDatensatzOfRow(int rowIndex) {
        return new ZaehlerDatensatz(
                Integer.parseInt(getTableCellData(rowIndex,KUNDENNUMMER_COL_INDEX)),
                stringToZaehlerartMapper(getTableCellData(rowIndex,ZAEHLERART_COL_INDEX)),
                getTableCellData(rowIndex,ZAEHLERNUMMER_COL_INDEX),
                PropertyManagementApplication.convertStringToDate(getTableCellData(rowIndex,DATUM_COL_INDEX)),
                stringToBooleanMapper(getTableCellData(rowIndex, NEU_EINGEBAUT_COL_INDEX)),
                Integer.parseInt(getTableCellData(rowIndex,ZAEHLERSTAND_COL_INDEX)),
                getTableCellData(rowIndex, KOMMENTAR_COL_INDEX)
        );
    }

    private List<ZaehlerDatensatz> getAllRecords() {
        return new ArrayList<>(this.propertyManagementApplication.getSessionData());
    }

    private static String capitalize(String name) {
        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }

    private static String booleanToStringMapper(boolean input) {
        return input ? "Ja" : "Nein";
    }

    static Zaehlerart stringToZaehlerartMapper(String input) {

        Zaehlerart zaehlerart = Zaehlerart.STROM;

        switch (input) {
            case "Strom" -> zaehlerart = Zaehlerart.STROM;
            case "Gas" -> zaehlerart = Zaehlerart.GAS;
            case "Heizung" -> zaehlerart = Zaehlerart.HEIZUNG;
            case "Wasser" -> zaehlerart = Zaehlerart.WASSER;
        }
        return zaehlerart;
    }

    private static boolean stringToBooleanMapper(String input) {
        return input.equals("Ja");
    }

    private String getTableCellData(int row_index, int column_index) {
        return tabelle.getModel().getValueAt(row_index, column_index).toString();
    }
}