package eu.bsinfo.gruppe4.gui.frames;

import eu.bsinfo.gruppe4.gui.DatePickerFormatter;
import eu.bsinfo.gruppe4.gui.DatenWindow;
import eu.bsinfo.gruppe4.gui.MessageDialog;
import eu.bsinfo.gruppe4.gui.Zaehlerart;
import eu.bsinfo.gruppe4.server.model.Ablesung;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.zip.DataFormatException;

public abstract class BaseReadingInputWindow extends JFrame {
    private ButtonGroup zaehlerartenRadioButtons;
    private JRadioButton rb_strom;
    private JRadioButton rb_gas;
    private JRadioButton rb_heizung;
    private JRadioButton rb_wasser;
    private JTextField kundennummer;
    private JTextField zaehlernummer;
    private JTextField zaehlerstand;
    private JTextField kommentar;
    private JCheckBox wurdeNeuEingebaut;
    private UtilDateModel model;

    private final JButton saveButton = new JButton("Speichern");
    private final JButton cancelButton = new JButton("Abbrechen");


    public BaseReadingInputWindow() throws HeadlessException {
        initializeComponents();
    }

    private void initializeComponents() {

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel inputFieldsPanel = new JPanel(new GridLayout(7, 2));
        con.add(inputFieldsPanel, BorderLayout.CENTER);

        final JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        con.add(buttonsPanel, BorderLayout.SOUTH);

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        // Radio Buttons konfigurieren
        final JPanel pn_radio_buttons = new JPanel(new GridLayout(1, 4));
        pn_radio_buttons.add(rb_strom = new JRadioButton("Strom"));
        pn_radio_buttons.add(rb_gas = new JRadioButton("Gas"));
        pn_radio_buttons.add(rb_heizung = new JRadioButton("Heizung"));
        pn_radio_buttons.add(rb_wasser = new JRadioButton("Wasser"));

        rb_strom.setSelected(true);
        rb_strom.setActionCommand("Strom");
        rb_gas.setActionCommand("Gas");
        rb_heizung.setActionCommand("Heizung");
        rb_wasser.setActionCommand("Wasser");

        zaehlerartenRadioButtons = new ButtonGroup();
        zaehlerartenRadioButtons.add(rb_gas);
        zaehlerartenRadioButtons.add(rb_heizung);
        zaehlerartenRadioButtons.add(rb_strom);
        zaehlerartenRadioButtons.add(rb_wasser);

        // Gridlayout mit Label und Textfelder befüllen
        inputFieldsPanel.add(new JLabel("Kundennummer"));
        inputFieldsPanel.add(kundennummer = new JTextField());
        kundennummer.setEditable(false);

        inputFieldsPanel.add(new JLabel("Zählerart (Strom, Gas, Heizung, Wasser)"));
        inputFieldsPanel.add(pn_radio_buttons);

        inputFieldsPanel.add(new JLabel("Zählernummer"));
        inputFieldsPanel.add(zaehlernummer = new JTextField());

        model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DatePickerFormatter());

        // Setzt das heutige Datum in das Datumsfeld ein
        model.setSelected(true);

        inputFieldsPanel.add(new JLabel("Datum"));
        inputFieldsPanel.add(datePicker);

        inputFieldsPanel.add(new JLabel("Neu eingebaut?"));
        inputFieldsPanel.add(wurdeNeuEingebaut = new JCheckBox());

        inputFieldsPanel.add(new JLabel("Zählerstand"));
        inputFieldsPanel.add(zaehlerstand = new JTextField());

        inputFieldsPanel.add(new JLabel("Kommentar"));
        inputFieldsPanel.add(kommentar = new JTextField());


        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> dispose());

        // Passe die Größe des Fensters an
        setLocationRelativeTo(null);
        setSize(650, 300);
        setVisible(true);
    }



    //FIXME: Will only be written to file if the window of PropertyManagementApplication class is closed
    private void save() {
        try {
            Ablesung reading = getReadingOfInputFields();
            saveReading(reading);
            MessageDialog.showSuccessMessage("Ablesung wurde gespeichert");
        }
        catch (Exception e) {
            MessageDialog.showWarningMessage(e.getMessage());
        }
    }

    protected abstract Ablesung saveReading(Ablesung reading);
    protected abstract Ablesung getReadingOfInputFields() throws DataFormatException;


    public void setKundennummer(String kundennummer) {
        this.kundennummer.setText(kundennummer);
    }

    public Zaehlerart getZaehlerart() {

        String ausgewaehlteZaehlerart = zaehlerartenRadioButtons.getSelection().getActionCommand();

        return DatenWindow.stringToZaehlerartMapper(ausgewaehlteZaehlerart);
    }

    public void setZaehlerart(String zaehlerart) {
        switch (zaehlerart) {
            case "Strom" -> rb_strom.setSelected(true);
            case "Gas" -> rb_gas.setSelected(true);
            case "Heizung" -> rb_heizung.setSelected(true);
            case "Wasser" -> rb_wasser.setSelected(true);
        }
    }

    public String getZaehlernummer() throws DataFormatException {

        String zaehlernummer = this.zaehlernummer.getText();

        if (isBlankOrEmpty(zaehlernummer)) throw new DataFormatException("Zählernummer ist leer");

        return zaehlernummer;
    }

    public void setZaehlernummer(String zaehlernummer) {
        this.zaehlernummer.setText(zaehlernummer);
    }

    public LocalDate getDatum() {
        // Das model zeigt komischerweise das datum einen monat in der vergangenheit an
        // von daher muss hier beim monat 1 hinzugefügt werden
        return LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay());
    }

    public void setDatum(LocalDate datum) {
        // wieder ein fehler bei der datumsanzeige im model, nur andersherum
        // → einen monat abziehen
        this.model.setDate(datum.getYear(), datum.getMonthValue() - 1, datum.getDayOfMonth());
    }

    public int getZaehlerstand() throws DataFormatException {

        String zaehlerstandAsString = zaehlerstand.getText();

        if (isBlankOrEmpty(zaehlerstandAsString)) throw new DataFormatException("Zählerstand ist leer");

        try {
            return Integer.parseInt(zaehlerstandAsString);
        }
        catch (NumberFormatException e) {
            throw new DataFormatException("Zählerstand muss eine Ganzzahl sein");
        }
    }

    public void setZaehlerstand(String zaehlerstand) {
        this.zaehlerstand.setText(zaehlerstand);
    }

    public String getKommentar() {
        return kommentar.getText();
    }

    public void setKommentar(String kommentar) {
        this.kommentar.setText(kommentar);
    }

    public boolean getWurdeNeuEingebaut() {
        return wurdeNeuEingebaut.isSelected();
    }

    public void setWurdeNeuEingebaut(boolean wurdeNeuEingebaut) {
        this.wurdeNeuEingebaut.setSelected(wurdeNeuEingebaut);
    }

    private boolean isBlankOrEmpty(String input) {
        return input.isEmpty() || input.isBlank();
    }
}
