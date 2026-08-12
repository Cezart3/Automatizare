package gui.steps;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import db.ProductDAO;
import gui.layout.ConfiguratorFrame;
import gui.navigation.StepNavigator;
import gui.navigation.StepSideNavPanel;
import model.glass.Glass;

public class ConfigPanel extends JPanel implements StepNavigator {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel stepPanel = new JPanel(cardLayout);
    private final StepSideNavPanel sideNav;

    private final List<Glass> sticle;
    private final ProductDAO productDAO;

    // Referințe către panouri pentru a le putea notifica
    private FeroneriePanel FeroneriePanel;
    private GlassPanel step6Panel; // Adaugă această referință

    public ConfigPanel(ConfiguratorFrame frame, List<Glass> sticle, ProductDAO productDAO) {
        this.sticle = sticle;
        this.productDAO = productDAO;

        setLayout(new BorderLayout());

        // Lista de pași în ordinea corectă
        List<String> steps = List.of(
                "Selectare Cabina",      // 0
                "Selectare Tipologie",   // 1 -> Step7
                "Culoare",               // 2 -> Step2
                "Feronerie",             // 3 -> Step3
                "Dimensiuni",            // 4 -> Step4
                "Selectare Sticlă",      // 5 -> Step6
                "Preț"                   // 6 -> Step5
        );

        sideNav = new StepSideNavPanel(steps, this, frame);
        add(sideNav, BorderLayout.WEST);

        // Creăm panourile și păstrăm referința la Step3Panel
        FeroneriePanel = new FeroneriePanel(this);
        step6Panel = new GlassPanel(this); // Creăm Step6Panel
        ConfigurationPanelBatanta step5Panel = new ConfigurationPanelBatanta(this, sticle, productDAO, step6Panel);


        // Adaugăm toate panourile în CardLayout în aceeași ordine cu lista steps
        stepPanel.add(new TipCabinaPanel(this), "0");                  // Selectare Cabina
        stepPanel.add(new BatantaSubtipPanel(this), "1");                  // Selectare Tipologie
        stepPanel.add(new ColorPanel(this), "2");                  // Culoare
        stepPanel.add(FeroneriePanel, "3");                            // Feronerie (păstrăm referința)
        stepPanel.add(new DimensiuniPanel(this), "4");                  // Dimensiuni
        stepPanel.add(new GlassPanel(this), "5");                  // Selectare Sticlă
        stepPanel.add(new ConfigurationPanelBatanta(this, sticle, productDAO), "6"); // Preț

        add(stepPanel, BorderLayout.CENTER);
    }

    // Navigare către un pas
    @Override
    public void goToStep(int step) {
        cardLayout.show(stepPanel, String.valueOf(step));
        sideNav.setActiveStep(step);

        // Notifică panoul curent că a fost activat
        onStepActivated(step);
    }

    // Metodă utilă pentru a naviga programatic
    public void showStep(int step) {
        goToStep(step);
    }

    // Notifică panoul că a fost activat
    @Override
    public void onStepActivated(int step) {
        System.out.println("Step activated: " + step);

        if (step == 3 && FeroneriePanel != null) {
            System.out.println("Notifying Step3Panel to refresh...");
            // Folosește SwingUtilities pentru a asigura că se execută pe EDT
            SwingUtilities.invokeLater(() -> {
                FeroneriePanel.refreshSelections();
            });
        }
    }
}