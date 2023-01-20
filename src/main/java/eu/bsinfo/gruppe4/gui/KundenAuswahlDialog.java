package eu.bsinfo.gruppe4.gui;

import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class KundenAuswahlDialog extends JFrame {

    private final JTextField tf_kundennr;
    private final JButton btn_ok;
    private final JButton btn_abbrechen;
    private final JButton btn_loeschen;

    public KundenAuswahlDialog(ArrayList<ZaehlerDatensatz> sessionData){
        super("Kundennummer eingeben");

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_eingabemaske = new JPanel(new GridLayout(1, 2));
        con.add(pn_eingabemaske, BorderLayout.CENTER);

        final JPanel pn_buttons = new JPanel(new GridLayout(1, 3));
        con.add(pn_buttons, BorderLayout.SOUTH);

        pn_eingabemaske.add(new JLabel("Kundennummer:"));
        pn_eingabemaske.add(tf_kundennr = new JTextField("1234"));

        pn_buttons.add(btn_abbrechen = new JButton("Abbrechen"));
        pn_buttons.add(btn_ok = new JButton("OK"));
        pn_buttons.add(btn_loeschen = new JButton("löschen"));

        btn_ok.addActionListener(e -> {
            int kundennr;
            try {
                kundennr = Integer.parseInt(tf_kundennr.getText());
            } catch (NumberFormatException ex){
                MessageDialog.showErrorMessage("Ungültige Kundennummer");
                return;
            }
            try {
                new KundenTabelleWindow(sessionData, kundennr);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btn_loeschen.addActionListener(e -> {
            WebClient webClient = new WebClient();
            Response r = webClient.deleteCustomer(UUID.fromString(tf_kundennr.getText()));
            System.out.println(r);
            String message = "Kunde konnte nicht gelscht werden\n\n";
            if (r.getStatus() == 200) {
                MessageDialog.showSuccessMessage("Kunde wurde erfolgreich gelscht");
            } else {
                MessageDialog.showErrorMessage(message);
            }
        });

        btn_abbrechen.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(300, 100);
    }

}
