package gui.admin;

import gui.layout.ConfiguratorFrame;
import gui.theme.UITheme;
import util.config.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final JTextField tvaField;
    private final JTextField adaosComercialField;
    private final JTextField pretDecupajField;
    private final JTextField userField;
    private final JPasswordField passwordField;
    private final ConfiguratorFrame frame;

    public SettingsPanel(ConfiguratorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(UITheme.BG_PRIMARY);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new UITheme.RoundedBorder(UITheme.BORDER_SUBTLE, 1, UITheme.CORNER_RADIUS_SMALL),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Section Title - Credentiale
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel credLabel = new JLabel("CREDENTIALE LOGARE");
        credLabel.setForeground(UITheme.ACCENT_PRIMARY);
        credLabel.setFont(UITheme.FONT_HEADING);
        panel.add(credLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(UITheme.TEXT_PRIMARY);
        usernameLabel.setFont(UITheme.FONT_BODY);
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        userField = new JTextField(15);
        UITheme.styleTextField(userField);
        userField.setText(Settings.getUsername());
        panel.add(userField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(UITheme.TEXT_PRIMARY);
        passwordLabel.setFont(UITheme.FONT_BODY);
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        passwordField = new JPasswordField(15);
        UITheme.styleTextField(passwordField);
        passwordField.setText(Settings.getPassword());
        panel.add(passwordField, gbc);

        // Empty space as separator
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(Box.createVerticalStrut(15), gbc);

        // Section Title - Setări Calcul
        gbc.gridx = 0;
        gbc.gridy = row++;
        JLabel calcLabel = new JLabel("SETĂRI CALCUL");
        calcLabel.setForeground(UITheme.ACCENT_PRIMARY);
        calcLabel.setFont(UITheme.FONT_HEADING);
        panel.add(calcLabel, gbc);

        // TVA
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel tvaLabel = new JLabel("TVA (%):");
        tvaLabel.setForeground(UITheme.TEXT_PRIMARY);
        tvaLabel.setFont(UITheme.FONT_BODY);
        panel.add(tvaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        tvaField = new JTextField(10);
        UITheme.styleTextField(tvaField);
        tvaField.setText(String.valueOf(Settings.getTVA()));
        panel.add(tvaField, gbc);

        // Adaos Comercial
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel adaosLabel = new JLabel("Adaos Comercial (%):");
        adaosLabel.setForeground(UITheme.TEXT_PRIMARY);
        adaosLabel.setFont(UITheme.FONT_BODY);
        panel.add(adaosLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        adaosComercialField = new JTextField(10);
        UITheme.styleTextField(adaosComercialField);
        adaosComercialField.setText(String.valueOf(Settings.getAdaosComercial()));
        panel.add(adaosComercialField, gbc);

        // Preț Decupaj
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel pretDecupajLabel = new JLabel("Preț Decupaj (EUR):");
        pretDecupajLabel.setForeground(UITheme.TEXT_PRIMARY);
        pretDecupajLabel.setFont(UITheme.FONT_BODY);
        panel.add(pretDecupajLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        pretDecupajField = new JTextField(10);
        UITheme.styleTextField(pretDecupajField);
        pretDecupajField.setText(String.valueOf(Settings.getPretDecupaj()));
        panel.add(pretDecupajField, gbc);

        // Butoane
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 8, 8, 8);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton saveButton = new UITheme.ModernButton("Salvează Setări", true);
        saveButton.setPreferredSize(new Dimension(160, 42));
        buttonPanel.add(saveButton);

        JButton resetButton = new UITheme.ModernButton("Resetare Default", false);
        resetButton.setPreferredSize(new Dimension(160, 42));
        buttonPanel.add(resetButton);

        JButton homeButton = new UITheme.ModernButton("← Acasă", false);
        homeButton.setPreferredSize(new Dimension(120, 42));
        buttonPanel.add(homeButton);

        panel.add(buttonPanel, gbc);

        add(panel, BorderLayout.CENTER);

        saveButton.addActionListener(e -> saveSettings());
        resetButton.addActionListener(e -> resetToDefault());
        homeButton.addActionListener(e -> goHome());
    }

    private void saveSettings() {
        try {
            double tva = Double.parseDouble(tvaField.getText().trim());
            double adaos = Double.parseDouble(adaosComercialField.getText().trim());
            double pretDecupaj = Double.parseDouble(pretDecupajField.getText().trim());

            if (tva < 0 || tva > 100) {
                JOptionPane.showMessageDialog(this, "TVA-ul trebuie să fie între 0 și 100%!", "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (adaos < 0 || adaos > 100) {
                JOptionPane.showMessageDialog(this, "Adaosul comercial trebuie să fie între 0 și 100%!", "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (pretDecupaj < 0) {
                JOptionPane.showMessageDialog(this, "Prețul decupajului trebuie să fie pozitiv!", "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String username = userField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username-ul nu poate fi gol!", "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Settings.setTVA(tva);
            Settings.setAdaosComercial(adaos);
            Settings.setPretDecupaj(pretDecupaj);
            Settings.setUsername(username);
            Settings.setPassword(password);

            JOptionPane.showMessageDialog(this,
                    "Setări salvate cu succes!\n\n" +
                            "Credentiale:\n" +
                            "• Username: " + username + "\n\n" +
                            "Setări Calcul:\n" +
                            "• TVA: " + tva + "%\n" +
                            "• Adaos Comercial: " + adaos + "%\n" +
                            "• Preț Decupaj: " + pretDecupaj + " EUR",
                    "Succes",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Vă rugăm introduceți valori numerice valide pentru TVA, Adaos și Preț Decupaj!", "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetToDefault() {
        int response = JOptionPane.showConfirmDialog(this,
                "Sigur doriți să resetați la valorile default?\n\n" +
                        "Credentiale:\n" +
                        "• Username: Cezar\n" +
                        "• Password: Cezar2004!\n\n" +
                        "Setări Calcul:\n" +
                        "• TVA: 19%\n" +
                        "• Adaos Comercial: 0%\n" +
                        "• Preț Decupaj: 19 EUR",
                "Confirmare Resetare",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            Settings.setTVA(19.0);
            Settings.setAdaosComercial(0.0);
            Settings.setPretDecupaj(19.0);
            Settings.setUsername("Cezar");
            Settings.setPassword("Cezar2004!");

            tvaField.setText("19.0");
            adaosComercialField.setText("0.0");
            pretDecupajField.setText("19.0");
            userField.setText("Cezar");
            passwordField.setText("Cezar2004!");

            JOptionPane.showMessageDialog(this, "Setări resetate la valorile default!");
        }
    }

    private void goHome() {
        if (hasUnsavedChanges()) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Există modificări nesalvate! Doriți să le salvați înainte de a părăsi pagina?",
                    "Modificări nesalvate",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                saveSettings();
                frame.showHomePanel();
            } else if (response == JOptionPane.NO_OPTION) {
                frame.showHomePanel();
            }
        } else {
            frame.showHomePanel();
        }
    }

    private boolean hasUnsavedChanges() {
        try {
            double currentTVA = Double.parseDouble(tvaField.getText().trim());
            double currentAdaos = Double.parseDouble(adaosComercialField.getText().trim());
            double currentPretDecupaj = Double.parseDouble(pretDecupajField.getText().trim());
            String currentUsername = userField.getText().trim();
            String currentPassword = new String(passwordField.getPassword()).trim();

            return currentTVA != Settings.getTVA() ||
                    currentAdaos != Settings.getAdaosComercial() ||
                    currentPretDecupaj != Settings.getPretDecupaj() ||
                    !currentUsername.equals(Settings.getUsername()) ||
                    !currentPassword.equals(Settings.getPassword());

        } catch (NumberFormatException e) {
            return true;
        }
    }
}