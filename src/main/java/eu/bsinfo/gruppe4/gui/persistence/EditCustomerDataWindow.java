package eu.bsinfo.gruppe4.gui.persistence;

import eu.bsinfo.gruppe4.gui.WebClient;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class EditCustomerDataWindow extends JFrame {

    private final JTextField tf_name;
    private final JTextField tf_surname;
    private final JButton btn_ok;
    private final JButton btn_abbrechen;

    public EditCustomerDataWindow(UUID uuid) {
        super("Kundendaten ändern für " + uuid);

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_eingabemaske = new JPanel(new GridLayout(2, 2));
        con.add(pn_eingabemaske, BorderLayout.CENTER);

        final JPanel pn_buttons = new JPanel(new GridLayout(1, 2));
        con.add(pn_buttons, BorderLayout.SOUTH);

        pn_eingabemaske.add(new JLabel("Vorname"));
        pn_eingabemaske.add(tf_name = new JTextField(""));
        pn_eingabemaske.add(new JLabel("Nachname"));
        pn_eingabemaske.add(tf_surname = new JTextField(""));

        pn_buttons.add(btn_abbrechen = new JButton("Abbrechen"));
        pn_buttons.add(btn_ok = new JButton("Änderungen speichern"));

        btn_ok.addActionListener(e -> {
            String name = tf_name.getText();
            String surname = tf_surname.getText();
            WebClient webClient = new WebClient();
            Response r = webClient.deleteCustomer(uuid); // tbd
        });

        btn_abbrechen.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(600, 150);
    }
}
