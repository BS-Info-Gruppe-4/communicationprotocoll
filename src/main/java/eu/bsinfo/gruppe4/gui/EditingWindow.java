package eu.bsinfo.gruppe4.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class EditingWindow extends JFrame {

    private final InputMask inputMask;
    private final PropertyManagementApplication propertyManagementApplication;

    public EditingWindow(PropertyManagementApplication propertyManagementApplication, ZaehlerDatensatz zaehlerDatensatz, DatenWindow daten) throws HeadlessException {
        super("Daten bearbeiten");

        setSize(630, 300);
        setVisible(true);

        this.propertyManagementApplication = propertyManagementApplication;

        final Container container = getContentPane();
        container.setLayout(new BorderLayout());

        inputMask = new InputMask(propertyManagementApplication);
        JButton speichernButton = new JButton("Speichern");

        container.add(speichernButton, BorderLayout.SOUTH);
        container.add(inputMask.getMainContainer(), BorderLayout.CENTER);


        speichernButton.addActionListener(e -> updateTableDatensatz(zaehlerDatensatz, daten));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                closeWindow();
            }
        });
    }

    void updateTableDatensatz(ZaehlerDatensatz zaehlerDatensatz, DatenWindow daten) {
        inputMask.save();
        propertyManagementApplication.removeDatensatzFromSession(zaehlerDatensatz);
        closeWindow();
        daten.dispose();
        new DatenWindow(propertyManagementApplication);
    }

    void withDefaultInputFields(
            String kundennummer,
            String zaehlerart,
            String zaehlernummer,
            LocalDate datum,
            boolean wurdeNeuEingebaut,
            String zaehlerstand,
            String kommentar
    ) {
        inputMask.setKundennummer(kundennummer);
        inputMask.setZaehlerart(zaehlerart);
        inputMask.setZaehlernummer(zaehlernummer);
        inputMask.setDatum(datum);
        inputMask.setWurdeNeuEingebaut(wurdeNeuEingebaut);
        inputMask.setZaehlerstand(zaehlerstand);
        inputMask.setKommentar(kommentar);
    }

    private void closeWindow() {dispose();}
}
