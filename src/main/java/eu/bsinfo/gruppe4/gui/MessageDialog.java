package eu.bsinfo.gruppe4.gui;

import javax.swing.*;

public class MessageDialog {

    public static void showErrorMessage(String fehlermeldung) {
        JOptionPane.showMessageDialog(
                null,
                "Fehlerhafte Eingabe: " + fehlermeldung,
                "Fehler",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Erfolg",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Achtung!",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
