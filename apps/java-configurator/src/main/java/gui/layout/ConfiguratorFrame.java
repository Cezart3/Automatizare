package gui.layout;

import db.ProductDAO;
import gui.navigation.StepNavigator;
import gui.admin.AdminPanel;
import gui.admin.SettingsPanel;
import gui.steps.ConfigPanel;
import gui.theme.UITheme;
import model.glass.Glass;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConfiguratorFrame extends JFrame implements StepNavigator {
    private LoginPanel loginPanel;
    private HomePanel homePanel;
    private ConfigPanel manualConfigPanel;
    private AdminPanel adminPanel;
    private SettingsPanel settingsPanel;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // DAO și lista de sticle (global în frame)
    private final ProductDAO productDAO;
    private final List<Glass> sticle;

    private String currentUser;

    public ConfiguratorFrame() {
        super("Configurator Cabine de Duș - Autentificare");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Aplică tema dark
        applyDarkTheme();

        // Inițializează DAO-ul și lista de sticle
        productDAO = new ProductDAO();
        sticle = productDAO.getAllGlass();

        // Creăm toate panourile
        loginPanel = new LoginPanel(this);
        // Celelalte panouri vor fi create la nevoie (lazy initialization)

        // Adăugăm panourile în mainPanel
        mainPanel.add(loginPanel, "LOGIN");

        add(mainPanel, BorderLayout.CENTER);

        // Afișează pagina de login la start
        showLoginPanel();
        setVisible(true);
    }

    private void applyDarkTheme() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");

            // Setări consistente pentru tema dark folosind UITheme
            UIManager.put("Panel.background", UITheme.BG_PRIMARY);
            UIManager.put("Button.background", UITheme.BG_SECONDARY);
            UIManager.put("Button.foreground", UITheme.TEXT_PRIMARY);

            // Alte componente
            UIManager.put("ComboBox.background", UITheme.BG_SECONDARY);
            UIManager.put("ComboBox.foreground", UITheme.TEXT_PRIMARY);
            UIManager.put("TextField.background", UITheme.BG_SECONDARY);
            UIManager.put("TextField.foreground", UITheme.TEXT_PRIMARY);
            UIManager.put("Label.foreground", UITheme.TEXT_PRIMARY);

            // Accente FlatLaf
            UIManager.put("Component.accentColor", UITheme.ACCENT_PRIMARY);
            UIManager.put("Component.focusColor", UITheme.ACCENT_PRIMARY);

            // Colțuri rotunjite
            UIManager.put("Component.arc", UITheme.CORNER_RADIUS);
            UIManager.put("Button.arc", UITheme.CORNER_RADIUS);
            UIManager.put("TextComponent.arc", UITheme.CORNER_RADIUS_SMALL);

        } catch (Exception ex) {
            System.err.println("Eroare la aplicarea temei: " + ex.getMessage());
        }
    }

    // === METODE DE NAVIGARE ===

    // Navigare către login
    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
        setTitle("Configurator Cabine de Duș - Autentificare");
        if (loginPanel != null) {
            loginPanel.resetForm();
        }
    }

    // Navigare către pagina principală (acasă) - METODĂ NOUĂ pentru compatibilitate
    public void showHome() {
        showHomePanel(); // Redirecționează către noua metodă
    }

    // Navigare către pagina principală (acasă)
    public void showHomePanel() {
        if (homePanel == null) {
            homePanel = new HomePanel(this);
            mainPanel.add(homePanel, "HOME");
        }
        cardLayout.show(mainPanel, "HOME");
        setTitle("Configurator Cabine de Duș - Panou Principal");
    }

    // Navigare către configurare manuală
    public void showManualConfig() {
        if (manualConfigPanel == null) {
            manualConfigPanel = new ConfigPanel(this, sticle, productDAO);
            mainPanel.add(manualConfigPanel, "MANUAL");
        }
        cardLayout.show(mainPanel, "MANUAL");
        setTitle("Configurator Cabine de Duș - Configurare Manuală");
    }

    // Navigare către panoul de administrare
    public void showAdminPanel() {
        if (adminPanel == null) {
            adminPanel = new AdminPanel(this);
            mainPanel.add(adminPanel, "ADMIN");
        }
        cardLayout.show(mainPanel, "ADMIN");
        setTitle("Configurator Cabine de Duș - Panou Administrator");
    }

    // Navigare către setări
    public void showSettingsPanel() {
        if (settingsPanel == null) {
            settingsPanel = new SettingsPanel(this);
            mainPanel.add(settingsPanel, "SETTINGS");
        }
        cardLayout.show(mainPanel, "SETTINGS");
        setTitle("Configurator Cabine de Duș - Setări");
    }

    // Logout
    public void logout() {
        currentUser = null;
        showLoginPanel();
    }

    // Navigare între pașii manuali (implementare StepNavigator)
    @Override
    public void goToStep(int step) {
        if (manualConfigPanel != null) {
            manualConfigPanel.showStep(step);
        }
    }

    // Notifică activarea unui pas (implementare StepNavigator)
    @Override
    public void onStepActivated(int step) {
        // Poate fi folosit pentru logica suplimentară când se activează un pas
        System.out.println("Frame: Step activated - " + step);
    }

    // Getters și setters
    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public List<Glass> getSticle() {
        return sticle;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConfiguratorFrame();
        });
    }
}