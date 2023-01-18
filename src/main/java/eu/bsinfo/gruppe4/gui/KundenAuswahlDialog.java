package eu.bsinfo.gruppe4.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class KundenAuswahlDialog extends JFrame {

    private final JTextField tf_kundennr;
    private final JButton btn_ok;
    private final JButton btn_abbrechen;

    public KundenAuswahlDialog(ArrayList<ZaehlerDatensatz> sessionData){
        super("Kundennummer eingeben");

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_eingabemaske = new JPanel(new GridLayout(1, 2));
        con.add(pn_eingabemaske, BorderLayout.CENTER);

        final JPanel pn_buttons = new JPanel(new GridLayout(1, 2));
        con.add(pn_buttons, BorderLayout.SOUTH);

        pn_eingabemaske.add(new JLabel("Kundennummer:"));
        pn_eingabemaske.add(tf_kundennr = new JTextField("1234"));

        pn_buttons.add(btn_abbrechen = new JButton("Abbrechen"));
        pn_buttons.add(btn_ok = new JButton("OK"));

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

        btn_abbrechen.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(300, 100);
    }

}
