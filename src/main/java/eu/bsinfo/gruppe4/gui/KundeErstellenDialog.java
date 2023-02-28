package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.gui.persistence.SessionStorage;
import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KundeErstellenDialog extends JFrame {

    private final SessionStorage sessionStorage = SessionStorage.getInstance();

    private final JTextField tf_vorname;
    private final JTextField tf_nachname;
    private final JLabel lb_vorname;
    private final JLabel lb_nachname;
    private final JButton btn_ok;
    private final JButton btn_abbrechen;

    public KundeErstellenDialog(AllCustomersTable act) {
        super("Erstelle neuen Kunden");

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        final JPanel pn_eingabemaske = new JPanel(new GridLayout(2, 2));
        con.add(pn_eingabemaske, BorderLayout.CENTER);
        final JPanel pn_buttons = new JPanel(new GridLayout(1, 2));
        con.add(pn_buttons, BorderLayout.SOUTH);

        pn_eingabemaske.add(lb_vorname = new JLabel("Vorname:"));
        pn_eingabemaske.add(tf_vorname = new JTextField("Hansi"));
        tf_vorname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tf_nachname.requestFocusInWindow();
                }
            }
        });
        pn_eingabemaske.add(lb_nachname = new JLabel("Nachname: "));
        pn_eingabemaske.add(tf_nachname = new JTextField("Huber"));
        tf_nachname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btn_ok.requestFocusInWindow();
                }
            }
        });
        pn_buttons.add(btn_abbrechen = new JButton("Abbrechen"));
        pn_buttons.add(btn_ok = new JButton("OK"));
        btn_ok.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btn_ok.doClick();
                }
            }
        });

        btn_abbrechen.addActionListener(e -> dispose());
        btn_ok.addActionListener(e -> {

            String kundeNachname = tf_nachname.getText();
            String kundeVorname = tf_vorname.getText();

            if (isBlankOrEmpty(kundeNachname) || isBlankOrEmpty(kundeVorname)) {
                MessageDialog.showErrorMessage("Bitte gib einen Vor- und Nachnamen für den Kunden ein");
                return;
            }

            Kunde kunde = new Kunde(kundeNachname, kundeVorname);

            WebClient webClient = new WebClient();
            Response r = webClient.createNewCustomer(kunde);
            System.out.println(r);

            if (WebClient.entityWasCreated(r)) {
                MessageDialog.showSuccessMessage("Kunde wurde erfolgreich erstellt");

                act.refreshTable();
            }
            else {
                String errorMessage;

                try {
                    errorMessage = getErrorMessageOfResponse(r);
                }
                catch (Exception e1) {
                    errorMessage = "Es ist ein unbekannter Fehler aufgetreten";
                }

                MessageDialog.showErrorMessage(errorMessage);
            }
        });

        setLocationRelativeTo(null);
        setSize(400, 150);
        setVisible(true);
    }

    private boolean isBlankOrEmpty(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

    private String getErrorMessageOfResponse(Response response) {
        return response.readEntity(String.class);
    }
}
