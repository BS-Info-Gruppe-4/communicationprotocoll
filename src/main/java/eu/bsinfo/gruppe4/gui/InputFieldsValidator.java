package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.gui.util.MessageDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class InputFieldsValidator implements Serializable {

    protected ArrayList<String> validationErrorMessages;
    private InputMask inputMask;

    public InputFieldsValidator() {
    }

    public boolean isAnyInputFieldInvalid(InputMask inputMask) {

        this.inputMask = inputMask;
        // muss hier neu initialisiert werden, damit die vorherigen error messages
        // bei jedem neuen methodenaufruf wieder mit einer leeren liste überschrieben werden
        validationErrorMessages = new ArrayList<>();

        checkIfAnyInputIsEmpty();
        validateKundennummer();
        validateZaehlerstand();
        validateDate();

        if (validationErrorMessages.isEmpty()) return false;

        return true;
    }

    void validateDate() {
        if (!isDateStringValid(inputMask.getDatum())) {
            validationErrorMessages.add("Datum ist fehlerhaft");
        }
    }

    void validateZaehlerstand() {
        // überprüfen, ob Zählerstand ein int ist
        try {
            Integer.parseInt(inputMask.getZaehlerstand());
        } catch (NumberFormatException ex) {
            validationErrorMessages.add("Zählerstand ist fehlerhaft");
        }
    }

    void validateKundennummer() {
        // überprüfen, ob Kundennummer ein int ist
        try {
            Integer.parseInt(inputMask.getKundennummer());
        } catch (NumberFormatException ex) {
            validationErrorMessages.add("Kundennummer ist fehlerhaft");
        }
    }

    private void checkIfAnyInputIsEmpty() {
        // überprüfen, ob Textfelder leer sind
        if (isAnyInputEmpty()) validationErrorMessages.add("Mindestens ein Eingabefeld ist leer!");
    }

    private boolean isDateStringValid(String datum) {
        String regex = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
        Pattern.matches(regex, datum);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(datum);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        String CurrantDate = dtf.format(now);
        if ( !datum.equals(CurrantDate) ) return false;
        return matcher.matches();
    }

    private boolean isAnyInputEmpty() {
        String[] textFields = {
                inputMask.getKundennummer(),
                inputMask.getZaehlernummer(),
                inputMask.getDatum(),
                inputMask.getZaehlerstand()
        };

        for (String texField: textFields) {
            if (texField.isEmpty() || texField.isBlank()) return true;
        }

        return false;
    }

    void displayAllOccurredValidationErrors() {

        String allErrorMessagesWithLineBreak = this.validationErrorMessages
                .stream()
                .map(fehlermeldung -> "\n" + fehlermeldung)
                .collect(Collectors.joining());

        MessageDialog.showErrorMessage(allErrorMessagesWithLineBreak);
    }
}