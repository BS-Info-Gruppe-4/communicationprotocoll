package eu.bsinfo.gruppe4.gui.persistence;

import eu.bsinfo.gruppe4.gui.AllCustomersTable;
import eu.bsinfo.gruppe4.gui.MessageDialog;
import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class EditCustomerDataWindow extends JFrame {

    private final JTextField tf_name;
    private final JTextField tf_surname;
    private final JButton btn_ok;
    private final JButton btn_abbrechen;
    private Kunde kunde;

    public EditCustomerDataWindow(Kunde customer, AllCustomersTable act) {
        super("Kundendaten ändern für UUID: " + customer.getId());

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_eingabemaske = new JPanel(new GridLayout(2, 2));
        con.add(pn_eingabemaske, BorderLayout.CENTER);

        final JPanel pn_buttons = new JPanel(new GridLayout(1, 2));
        con.add(pn_buttons, BorderLayout.SOUTH);

        pn_eingabemaske.add(new JLabel("Vorname"));
        pn_eingabemaske.add(tf_name = new JTextField(customer.getVorname()));
        pn_eingabemaske.add(new JLabel("Nachname"));
        pn_eingabemaske.add(tf_surname = new JTextField(customer.getName()));

        pn_buttons.add(btn_abbrechen = new JButton("Abbrechen"));
        pn_buttons.add(btn_ok = new JButton("Änderungen speichern"));

        btn_ok.addActionListener(e -> {
            String name = tf_name.getText();
            String surname = tf_surname.getText();

            if (isEmpty(surname, name) == true) {
                MessageDialog.showErrorMessage("Eingabefeld darf nicht leer sein!");
                return;
            }
            if (eingabevalidierung(customer.getName(), customer.getVorname(), surname, name) == false) {
                MessageDialog.showErrorMessage("Es wurden keine Änderungen vorgenommen!");
                return;
            }

            kunde = new Kunde(customer.getId(), surname, name);
            WebClient webClient = new WebClient();

            Kunde vergleichsKunde = webClient.getCustomer(kunde.getId());
            System.out.println(vergleichsKunde.toString());

            if (!customer.equals(vergleichsKunde)) {
                MessageDialog.showErrorMessage("Daten stimmen nicht mit Serverdaten überein!");
            }

            Response r = webClient.updateCustomer(kunde);
            System.out.println(r);
            String message = "Kunde konnte nicht geändert werden\n\n";
            if (r.getStatus() == 200) {
                MessageDialog.showSuccessMessage("Kunde wurde erfolgreich geändert");
                act.refreshTable();
            } else {
                MessageDialog.showErrorMessage(message);
            }
        });

        btn_abbrechen.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(600, 150);
    }

    public boolean eingabevalidierung(String origin_surname, String origin_name, String new_surname, String new_name) {
        boolean edited;
        if (new_surname.equals(origin_surname)) {
            edited = false;
        } else {
            edited = true;
        }
        if (new_name.equals(origin_name)) {
            edited = false;
        } else {
            edited = true;
        }
        return edited;
    }

    public boolean isEmpty(String surname, String name) {
        boolean isEmpty;
        if (surname.equals("") || name.equals("")) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        return isEmpty;
    }
}
