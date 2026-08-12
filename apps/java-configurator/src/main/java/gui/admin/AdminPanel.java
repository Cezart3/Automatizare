package gui.admin;

import gui.layout.ConfiguratorFrame;
import gui.theme.UITheme;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    public AdminPanel(ConfiguratorFrame frame) {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_PRIMARY);

        // --- Titlu ---
        JLabel titleLabel = new JLabel("Panou Administrator", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 25, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Panou lateral cu butoane ---
        JPanel sidePanel = new JPanel(new BorderLayout(10, 10));
        sidePanel.setBackground(UITheme.BG_SECONDARY);
        sidePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        sidePanel.setPreferredSize(new Dimension(280, 0));

        // Panou pentru butoanele principale
        JPanel mainButtonsPanel = new JPanel(new GridLayout(3, 1, 10, 15));
        mainButtonsPanel.setOpaque(false);

        JButton btnModify = new UITheme.ModernButton("Modificare elemente", false);
        JButton btnAddRemove = new UITheme.ModernButton("Adăugare / Ștergere", false);
        JButton btnChangeCabins = new UITheme.ModernButton("Cabine preconfigurate", false);

        // Setăm dimensiuni egale
        Dimension btnSize = new Dimension(240, 50);
        btnModify.setPreferredSize(btnSize);
        btnAddRemove.setPreferredSize(btnSize);
        btnChangeCabins.setPreferredSize(btnSize);

        mainButtonsPanel.add(btnModify);
        mainButtonsPanel.add(btnAddRemove);
        mainButtonsPanel.add(btnChangeCabins);

        sidePanel.add(mainButtonsPanel, BorderLayout.NORTH);

        // Buton Acasă în partea de jos - stil primar pentru a ieși în evidență
        JButton homeButton = new UITheme.ModernButton("← Acasă", true);
        homeButton.setPreferredSize(new Dimension(240, 45));
        sidePanel.add(homeButton, BorderLayout.SOUTH);

        add(sidePanel, BorderLayout.WEST);

        // --- Panou principal de conținut ---
        ModifyElementsPanel modifyPanel = new ModifyElementsPanel();
        AddRemovePanel addRemovePanel = new AddRemovePanel();
        SettingsPanel settingsPanel = new SettingsPanel(frame);

        JPanel changeCabinsPanel = new JPanel(new BorderLayout());
        changeCabinsPanel.setBackground(UITheme.BG_PRIMARY);
        JLabel comingSoonLabel = new JLabel("Funcționalitatea va fi implementată ulterior", SwingConstants.CENTER);
        comingSoonLabel.setFont(UITheme.FONT_BODY);
        comingSoonLabel.setForeground(UITheme.TEXT_SECONDARY);
        changeCabinsPanel.add(comingSoonLabel, BorderLayout.CENTER);

        contentPanel.setBackground(UITheme.BG_PRIMARY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(modifyPanel, "MODIFY");
        contentPanel.add(addRemovePanel, "ADD_REMOVE");
        contentPanel.add(changeCabinsPanel, "CHANGE_CABINS");
        contentPanel.add(settingsPanel, "SETTINGS");

        add(contentPanel, BorderLayout.CENTER);

        // --- Action Listeners ---
        btnModify.addActionListener(e -> cardLayout.show(contentPanel, "MODIFY"));
        btnAddRemove.addActionListener(e -> cardLayout.show(contentPanel, "ADD_REMOVE"));
        btnChangeCabins.addActionListener(e -> cardLayout.show(contentPanel, "CHANGE_CABINS"));
        homeButton.addActionListener(e -> frame.showHome());
    }
}