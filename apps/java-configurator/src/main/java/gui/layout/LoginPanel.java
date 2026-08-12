package gui.layout;

import gui.theme.UITheme;
import util.config.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {
    private final ConfiguratorFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginPanel(ConfiguratorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_PRIMARY);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        // Card premium cu gradient subtil și border
        JPanel card = new PremiumCard();
        card.setPreferredSize(new Dimension(420, 520));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titlu principal
        JLabel title = new JLabel("CONFIGURATOR");
        title.setFont(UITheme.FONT_TITLE_LARGE);
        title.setForeground(UITheme.TEXT_TITLE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(35, 0, 5, 0);
        card.add(title, gbc);

        JLabel title2 = new JLabel("CABINE DUȘ");
        title2.setFont(UITheme.FONT_SUBTITLE);
        title2.setForeground(UITheme.ACCENT_PRIMARY);
        title2.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        card.add(title2, gbc);

        // === UTILIZATOR ===
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 50, 8, 50);
        JLabel userLabel = new JLabel("UTILIZATOR");
        userLabel.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
        userLabel.setForeground(UITheme.TEXT_SECONDARY);
        card.add(userLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 50, 20, 50);
        usernameField = new JTextField(20);
        styleInputField(usernameField);
        setupPlaceholder(usernameField, "nume utilizator");
        card.add(usernameField, gbc);

        // === PAROLĂ ===
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 50, 8, 50);
        JLabel passLabel = new JLabel("PAROLĂ");
        passLabel.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
        passLabel.setForeground(UITheme.TEXT_SECONDARY);
        card.add(passLabel, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 50, 35, 50);
        passwordField = new JPasswordField(20);
        styleInputField(passwordField);
        setupPlaceholder(passwordField, "••••••••");
        card.add(passwordField, gbc);

        // === BUTOANE ===
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 50, 0, 50);
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);

        JButton loginBtn = new UITheme.ModernButton("Autentificare", true);
        loginBtn.setPreferredSize(new Dimension(140, 44));

        JButton exitBtn = new UITheme.ModernButton("Ieșire", false);
        exitBtn.setPreferredSize(new Dimension(140, 44));

        btnPanel.add(loginBtn);
        btnPanel.add(exitBtn);
        card.add(btnPanel, gbc);

        // Footer
        JLabel footer = new JLabel("© 2025 Tocaciu Cezar");
        footer.setFont(UITheme.FONT_SMALL);
        footer.setForeground(UITheme.TEXT_DISABLED);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 7;
        gbc.insets = new Insets(35, 0, 0, 0);
        card.add(footer, gbc);

        center.add(card);
        add(center, BorderLayout.CENTER);

        // Acțiuni
        loginBtn.addActionListener(e -> performLogin());
        exitBtn.addActionListener(e -> System.exit(0));
        usernameField.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private void styleInputField(JTextField field) {
        field.setFont(UITheme.FONT_BODY);
        field.setForeground(UITheme.TEXT_PRIMARY);
        field.setBackground(UITheme.BG_PRIMARY);
        field.setCaretColor(UITheme.ACCENT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                new UITheme.RoundedBorder(UITheme.BORDER_NORMAL, 1, UITheme.CORNER_RADIUS_SMALL),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        field.setPreferredSize(new Dimension(300, 48));
    }

    private void setupPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(UITheme.TEXT_DISABLED);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(UITheme.TEXT_PRIMARY);
                }
                // Highlight border on focus
                field.setBorder(BorderFactory.createCompoundBorder(
                        new UITheme.RoundedBorder(UITheme.ACCENT_PRIMARY, 2, UITheme.CORNER_RADIUS_SMALL),
                        BorderFactory.createEmptyBorder(11, 15, 11, 15)));
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(UITheme.TEXT_DISABLED);
                }
                // Reset border
                field.setBorder(BorderFactory.createCompoundBorder(
                        new UITheme.RoundedBorder(UITheme.BORDER_NORMAL, 1, UITheme.CORNER_RADIUS_SMALL),
                        BorderFactory.createEmptyBorder(12, 16, 12, 16)));
            }
        });
    }

    // === Card premium cu efect de glow subtil ===
    private static class PremiumCard extends JPanel {
        public PremiumCard() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int radius = UITheme.CORNER_RADIUS;

            // Outer glow / shadow
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fill(new RoundRectangle2D.Float(4, 6, w - 8, h - 8, radius + 4, radius + 4));

            // Card background
            g2.setColor(UITheme.BG_SECONDARY);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 4, h - 4, radius, radius));

            // Subtle border
            g2.setColor(UITheme.BORDER_SUBTLE);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 5, h - 5, radius, radius));

            // Accent line at top
            g2.setColor(UITheme.ACCENT_PRIMARY);
            g2.fillRoundRect(0, 0, w - 4, 4, radius, radius);

            g2.dispose();
        }
    }

    // === LOGICA 100% NESCHIMBATĂ ===
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() ||
                username.equals("nume utilizator") || password.equals("••••••••")) {
            showError("Vă rugăm introduceți username-ul și parola!");
            return;
        }

        if (authenticate(username, password)) {
            frame.setCurrentUser(username);
            JOptionPane.showMessageDialog(this,
                    "Autentificare reușită!\nBine ați venit, " + username + "!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.showHomePanel();
        } else {
            showError("Username sau parolă incorectă!");
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }

    private boolean authenticate(String username, String password) {
        return username.equals(Settings.getUsername()) && password.equals(Settings.getPassword());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Eroare Autentificare", JOptionPane.ERROR_MESSAGE);
    }

    public void resetForm() {
        usernameField.setText("nume utilizator");
        usernameField.setForeground(UITheme.TEXT_DISABLED);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                new UITheme.RoundedBorder(UITheme.BORDER_NORMAL, 1, UITheme.CORNER_RADIUS_SMALL),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        passwordField.setText("••••••••");
        passwordField.setForeground(UITheme.TEXT_DISABLED);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new UITheme.RoundedBorder(UITheme.BORDER_NORMAL, 1, UITheme.CORNER_RADIUS_SMALL),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
    }
}