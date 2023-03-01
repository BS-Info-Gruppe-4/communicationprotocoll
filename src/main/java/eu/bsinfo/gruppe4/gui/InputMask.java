package eu.bsinfo.gruppe4.gui;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import static eu.bsinfo.gruppe4.gui.PropertyManagementApplication.convertStringToDate;

public class InputMask {

    private JPanel mainContainer;
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
    private final PropertyManagementApplication propertyManagementApplication;
    private UtilDateModel model;


    public InputMask(PropertyManagementApplication propertyManagementApplication) {
        assembleJFrameElements();
        this.propertyManagementApplication = propertyManagementApplication;
    }

    void save() {

        InputFieldsValidator inputFieldsValidator = new InputFieldsValidator();
        PlausibilityChecker plausibilityChecker = new PlausibilityChecker(propertyManagementApplication.getSessionData());
        DuplicateManagingService duplicateManagingService = new DuplicateManagingService(propertyManagementApplication.getSessionData());
        JsonFileManager jsonFileManager = new JsonFileManager();

        if (inputFieldsValidator.isAnyInputFieldInvalid(this)) {
            inputFieldsValidator.displayAllOccurredValidationErrors();
            return;
        }

        ZaehlerDatensatz currentInputData = getZaehlerDatensatzOfInputFields();

        if (!plausibilityChecker.isPlausible(currentInputData.getZaehlerstand())) {
            MessageDialog.showWarningMessage("Der Wert des Zählerstands liegt außerhalb des Normbereichs!\n" +
                    "Möglicherweise liegt ein Leck vor.");
        }

        if (duplicateManagingService.isDuplicate(currentInputData)) {

            if (!duplicateManagingService.doesUserWantToKeepTheOriginal()) {
                // Da sich nichts am alten Datenstand ändert, wird nichts in die JSON-Datei
                // geschrieben, und der Speichervorgang einfach abgebrochen
                MessageDialog.showSuccessMessage("Speichern erfolgreich");
                return;
            }

            duplicateManagingService.removeDuplicateOf(currentInputData);
            MessageDialog.showSuccessMessage("Duplikat wurde ersetzt");
        }

        propertyManagementApplication.addToSessionData(currentInputData);
        jsonFileManager.saveToJsonFile(propertyManagementApplication.getSessionData());

        MessageDialog.showSuccessMessage("Speichern erfolgreich");
    }

    private void assembleJFrameElements() {

        mainContainer = new JPanel(new BorderLayout());

        final JPanel inputFieldsPanel = new JPanel(new GridLayout(7, 2));
        mainContainer.add(inputFieldsPanel, BorderLayout.CENTER);

        final JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
        mainContainer.add(buttonsPanel, BorderLayout.SOUTH);

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
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }

    public ZaehlerDatensatz getZaehlerDatensatzOfInputFields() {

        return new ZaehlerDatensatz(
                getKundennummer(),
                getZaehlerart(),
                getZaehlernummer(),
                convertStringToDate(getDatum()),
                getWurdeNeuEingebaut(),
                Integer.parseInt(getZaehlerstand()),
                getKommentar()
        );
    }

    public String getKundennummer() {
        return kundennummer.getText();
    }

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

    public String getZaehlernummer() {
        return zaehlernummer.getText();
    }

    public void setZaehlernummer(String zaehlernummer) {
        this.zaehlernummer.setText(zaehlernummer);
    }

    public String getDatum() {
        // Das model zeigt komischerweise das datum einen monat in der vergangenheit an
        // von daher muss hier beim monat 1 hinzugefügt werden
        LocalDate date = LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay());
        return PropertyManagementApplication.convertDateToString(date);
    }

    public void setDatum(LocalDate datum) {
        // wieder ein fehler bei der datumsanzeige im model, nur andersherum
        // → einen monat abziehen
        this.model.setDate(datum.getYear(), datum.getMonthValue() - 1, datum.getDayOfMonth());
    }

    public String getZaehlerstand() {
        return zaehlerstand.getText();
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
}
