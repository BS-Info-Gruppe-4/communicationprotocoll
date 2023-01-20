package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.server.model.Kunde;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KundeErstellenDialog extends JFrame {

    private JTextField tf_vorname;
    private JTextField tf_nachname;
    private JLabel lb_vorname;
    private JLabel lb_nachname;
    private JButton btn_ok;
    private JButton btn_abbrechen;

    public KundeErstellenDialog() {
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
            Kunde kunde = new Kunde(tf_nachname.getText(), tf_vorname.getText());
            WebClient webClient = new WebClient();
            Response r = webClient.createNewCustomer(kunde);
            System.out.println(r);
            String message = "Kunde konnte nicht erstellt werden\n\n";
            if (r.getStatus() == 201) {
                JOptionPane.showMessageDialog(this, "Kunde wurde erfolgreich erstellt", "Kunde erstellt", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setSize(400, 150);
        setVisible(true);
    }
}
