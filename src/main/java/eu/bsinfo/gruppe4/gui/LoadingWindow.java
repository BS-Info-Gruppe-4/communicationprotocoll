package eu.bsinfo.gruppe4.gui;

import eu.bsinfo.gruppe4.server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadingWindow extends JFrame {

    private JLabel label_bild;
    private JLabel label_starting;
    private JLabel label_gruppe4;
    private JProgressBar progressBar;
    private static final String IMG_PATH = "src/images/maggus.png";

    public LoadingWindow() throws InterruptedException {
        super("Starting...");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                exit();
            }
        });

        final Container con = getContentPane();
        con.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        try {
            BufferedImage img = ImageIO.read(new File(IMG_PATH));
            ImageIcon icon = new ImageIcon(img);
            label_bild = new JLabel(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        progressBar = new JProgressBar();

        con.add(label_bild, BorderLayout.CENTER);
        con.add(panel, BorderLayout.NORTH);
        con.add(progressBar, BorderLayout.SOUTH);

        label_starting = new JLabel("Starting server...");
        label_starting.setHorizontalAlignment(0);
        label_starting.setFont(new Font("Serif", Font.PLAIN, 24));

        label_gruppe4 = new JLabel("Hausverwaltung");
        label_gruppe4.setHorizontalAlignment(0);
        label_gruppe4.setFont(new Font("Serif", Font.PLAIN, 20));

        panel.add(new JLabel("BS Info Gruppe 4"));
        panel.add(label_gruppe4);
        panel.add(new JLabel(""));
        panel.add(label_starting);

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(500, 400);

        progressBar.setStringPainted(true);
        progressBar.setToolTipText("Loading...");
        int i = 0;
        while (i <= 100) {

            // fill the menu bar
            progressBar.setValue(i);

            // delay the thread
            Thread.sleep(500);
            i += 20;
        }
        dispose();
        new PropertyManagementApplication();
    }

    private void exit() {
        Server.stopServer(true);
        System.exit(0);
    }

}
