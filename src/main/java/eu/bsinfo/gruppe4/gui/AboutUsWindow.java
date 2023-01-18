package eu.bsinfo.gruppe4.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class AboutUsWindow extends JFrame {

    private JButton btn_ok;
    private JLabel label_bild;
    private JLabel label_text;

    private static final String IMG_PATH = "src/images/maggus.png";

    public AboutUsWindow() {
        super("About us");

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        con.add(btn_ok = new JButton("OK"), BorderLayout.SOUTH);
        btn_ok.addActionListener(e -> dispose());

        try {
            BufferedImage img = ImageIO.read(new File(IMG_PATH));
            ImageIcon icon = new ImageIcon(img);
            label_bild = new JLabel(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        label_text = new JLabel("unsere Firmenphilosophie");
        label_text.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label_text.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        label_text.setForeground(Color.blue);
        Font font = label_text.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        label_text.setFont(font.deriveFont(attributes));
        goWebsite(label_text);

        con.add(label_bild, BorderLayout.CENTER);
        con.add(panel, BorderLayout.EAST);

        panel.add(new JLabel("BS Info Gruppe 4"));
        panel.add(new JLabel("Version: 0.0.1"));
        panel.add(label_text);

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(500, 300);
    }

    private void goWebsite(JLabel website) {
        website.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://twitter.com/markus_soeder/"));
                } catch (URISyntaxException | IOException ex) {
                    //It looks like there's a problem
                }
            }
        });
    }
}
