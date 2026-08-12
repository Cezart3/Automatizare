package gui.layout;

import gui.theme.UITheme;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private final ConfiguratorFrame frame;

    public HomePanel(ConfiguratorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_PRIMARY);

        // Header cu buton logout
        JPanel headerPanel = createHeaderPanel();

        // Conținut principal
        JPanel contentPanel = createContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.BG_SECONDARY);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_SUBTLE));
        headerPanel.setPreferredSize(new Dimension(headerPanel.getPreferredSize().width, 70));

        JPanel paddingContainer = new JPanel(new BorderLayout());
        paddingContainer.setOpaque(false);
        paddingContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        headerPanel.add(paddingContainer, BorderLayout.CENTER);

        // Welcome message
        JLabel welcomeLabel = new JLabel(
                "Bine ai venit, " + (frame.getCurrentUser() != null ? frame.getCurrentUser() : "Oaspete") + "!");
        UITheme.styleAsSubtitle(welcomeLabel);

        // Buton logout - folosind butonul secundar din temă
        JButton logoutButton = new UITheme.ModernButton("Deconectare", false);
        logoutButton.setPreferredSize(new Dimension(140, 40));
        logoutButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Sigur doriți să vă deconectați?",
                    "Confirmare Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                frame.logout();
            }
        });

        paddingContainer.add(welcomeLabel, BorderLayout.WEST);
        paddingContainer.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(UITheme.BG_PRIMARY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 25, 20, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titlu
        JLabel titleLabel = new JLabel("Configurator Cabine Duș", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE_LARGE);
        titleLabel.setForeground(UITheme.TEXT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        contentPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // ============================================================
        // PALETĂ SOFISTICATĂ - Nuanțe de Indigo/Violet/Blue-Grey
        // ============================================================

        // Configurare Manuală - Indigo principal
        gbc.gridx = 0;
        JButton manualBtn = new UITheme.ModernButton("Configurare Manuală", true); // Primary button
        manualBtn.setPreferredSize(new Dimension(240, 90));
        manualBtn.addActionListener(e -> frame.showManualConfig());
        contentPanel.add(manualBtn, gbc);

        // Panou Administrator - Violet/Purple subtil
        gbc.gridx = 1;
        Color adminColor = new Color(139, 92, 246); // Violet 500 - elegant și profesional
        Color adminHover = new Color(167, 139, 250); // Violet 400
        JButton adminBtn = new UITheme.ModernButton("Panou Administrator", adminColor, adminHover);
        adminBtn.setPreferredSize(new Dimension(240, 90));
        adminBtn.addActionListener(e -> frame.showAdminPanel());
        contentPanel.add(adminBtn, gbc);

        // Setări - Slate/Grey neutru
        gbc.gridx = 2;
        Color settingsColor = new Color(100, 116, 139); // Slate 500 - neutru și calm
        Color settingsHover = new Color(148, 163, 184); // Slate 400
        JButton settingsBtn = new UITheme.ModernButton("Setări", settingsColor, settingsHover);
        settingsBtn.setPreferredSize(new Dimension(240, 90));
        settingsBtn.addActionListener(e -> frame.showSettingsPanel());
        contentPanel.add(settingsBtn, gbc);

        // Descrieri sub butoane
        gbc.gridy = 2;
        gbc.insets = new Insets(12, 25, 0, 25);

        JLabel manualDesc = new JLabel("Configurează propria cabină", SwingConstants.CENTER);
        manualDesc.setFont(UITheme.FONT_SMALL);
        manualDesc.setForeground(UITheme.TEXT_SECONDARY);
        gbc.gridx = 0;
        contentPanel.add(manualDesc, gbc);

        JLabel adminDesc = new JLabel("Gestionează stocul și prețurile", SwingConstants.CENTER);
        adminDesc.setFont(UITheme.FONT_SMALL);
        adminDesc.setForeground(UITheme.TEXT_SECONDARY);
        gbc.gridx = 1;
        contentPanel.add(adminDesc, gbc);

        JLabel settingsDesc = new JLabel("Setări aplicație", SwingConstants.CENTER);
        settingsDesc.setFont(UITheme.FONT_SMALL);
        settingsDesc.setForeground(UITheme.TEXT_SECONDARY);
        gbc.gridx = 2;
        contentPanel.add(settingsDesc, gbc);

        return contentPanel;
    }
}