package eu.bsinfo.gruppe4.gui.persistence;

import eu.bsinfo.gruppe4.gui.AllCustomersTable;
import eu.bsinfo.gruppe4.gui.MessageDialog;
import eu.bsinfo.gruppe4.gui.WebClient;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.awt.*;

public class EditCustomerDataWindow extends JFrame {

    private final JTextField tf_name;
    private final JTextField tf_surname;
    private final JButton btn_saveChanges;
    private final JButton btn_abbrechen;
    private Kunde customerEdited;

    public EditCustomerDataWindow(Kunde customerToEdit, AllCustomersTable act) {
        super("Kundendaten ändern für UUID: " + customerToEdit.getId());

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_eingabemaske = new JPanel(new GridLayout(2, 2));
        con.add(pn_eingabemaske, BorderLayout.CENTER);

        final JPanel pn_buttons = new JPanel(new GridLayout(1, 2));
        con.add(pn_buttons, BorderLayout.SOUTH);

        pn_eingabemaske.add(new JLabel("Vorname"));
        pn_eingabemaske.add(tf_name = new JTextField(customerToEdit.getVorname()));
        pn_eingabemaske.add(new JLabel("Nachname"));
        pn_eingabemaske.add(tf_surname = new JTextField(customerToEdit.getName()));

        pn_buttons.add(btn_abbrechen = new JButton("Abbrechen"));
        pn_buttons.add(btn_saveChanges = new JButton("Änderungen speichern"));

        btn_saveChanges.addActionListener(e -> {
            String name = tf_name.getText();
            String surname = tf_surname.getText();

            if (isEmpty(surname, name)) {
                MessageDialog.showErrorMessage("Eingabefeld darf nicht leer sein!");
                return;
            }
            if (!eingabevalidierung(customerToEdit.getName(), customerToEdit.getVorname(), surname, name)) {
                MessageDialog.showErrorMessage("Es wurden keine Änderungen vorgenommen!");
                return;
            }

            customerEdited = new Kunde(customerToEdit.getId(), surname, name);
            WebClient webClient = new WebClient();

            Kunde customerToCompare = webClient.getCustomer(customerToEdit.getId());
            System.out.println(customerToCompare.toString());

            if (!customerToEdit.equals(customerToCompare)) {
                MessageDialog.showErrorMessage("Daten stimmen nicht mit Serverdaten überein!\n" +
                        "Auf dem Server speichert: "+customerToCompare.getVorname()+" "+customerToCompare.getName() +
                        "\nGerade eingegeben: "+ customerToEdit.getVorname() + " "+customerToEdit.getName());
            }

            Response r = webClient.updateCustomer(customerEdited);
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
        String surname = new_surname.trim();
        String name = new_name.trim();
        boolean edited, edited_surname, edited_name;
        edited_surname = !surname.equals(origin_surname);
        edited_name = !name.equals(origin_name);
        edited = edited_name || edited_surname;
        return edited;
    }

    public boolean isEmpty(String surname, String name) {
        boolean isEmpty;
        isEmpty = surname.equals("") || name.equals("");
        return isEmpty;
    }
}
