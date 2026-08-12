package gui.steps;

import gui.navigation.StepNavigator;
import util.session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TipCabinaPanel extends JPanel {

    private JButton selectedButton = null;
    private final Color SELECTED_BORDER_COLOR = new Color(255, 120, 160);
    private final Color NORMAL_BORDER_COLOR = new Color(100, 100, 100);
    private final Color HOVER_BORDER_COLOR = new Color(150, 150, 150);

    public TipCabinaPanel(StepNavigator navigator) {
        setLayout(new BorderLayout());
        setBackground(new Color(42, 42, 42));

        JLabel title = new JLabel("Tip Produs", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(42, 42, 42));

        JPanel opts = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        opts.setOpaque(false);

        // === CABINĂ BATANTĂ → merge la subtipuri ===
        JButton batantaBtn = createImageButton("Cabină Batantă", "Pictures/Batanta.jpg", "batanta");
        batantaBtn.addActionListener(e -> {
            selectButton(batantaBtn, "batanta", "Cabină Batantă");
            Session.selectedProductType = "batanta";
            Session.selectedCabinaType = "batanta"; // compatibilitate cu codul vechi
            navigator.goToStep(1); // pagina de subtipuri
        });

        // === PANOU → sare direct la dimensiuni ===
        JButton panouBtn = createImageButton("Panou Fix", "Pictures/panou.png", "panou");
        panouBtn.addActionListener(e -> {
            selectButton(panouBtn, "panou", "Panou Fix");
            Session.selectedProductType = "panou";
            Session.selectedCabinaType = "panou"; // compatibilitate
            navigator.goToStep(3); // direct la dimensiuni
        });

        // === CABINĂ CULISANTĂ → direct la dimensiuni (sau altă pagină mai târziu) ===
        JButton culisantaBtn = createImageButton("Cabină Culisantă", "Pictures/Culisanta.jpg", "culisanta");
        culisantaBtn.addActionListener(e -> {
            selectButton(culisantaBtn, "culisanta", "Cabină Culisantă");
            Session.selectedProductType = "culisanta";
            Session.selectedCabinaType = "culisanta";
            navigator.goToStep(3);
        });

        // === BALUSTRADĂ → direct la dimensiuni ===
        JButton balustradaBtn = createImageButton("Balustradă", "Pictures/balustrada.jpg", "balustrada");
        balustradaBtn.addActionListener(e -> {
            selectButton(balustradaBtn, "balustrada", "Balustradă");
            Session.selectedProductType = "balustrada";
            Session.selectedCabinaType = "balustrada";
            navigator.goToStep(3);
        });

        opts.add(batantaBtn);
        opts.add(panouBtn);
        opts.add(culisantaBtn);
        opts.add(balustradaBtn);

        center.add(opts, new GridBagConstraints() {{
            gridx = 0; gridy = 0;
            weightx = 1; weighty = 1;
            anchor = GridBagConstraints.CENTER;
        }});
        add(center, BorderLayout.CENTER);

        // === BUTON URMĂTOR (opțional – poate fi eliminat) ===
        JButton next = new JButton("Următorul pas");
        next.setPreferredSize(new Dimension(200, 40));
        next.setBackground(new Color(255, 120, 160));
        next.setForeground(Color.WHITE);
        next.setFocusPainted(false);
        next.addActionListener(e -> {
            if (Session.selectedCabinaType == null || Session.selectedCabinaType.isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "Vă rugăm să selectați un tip de produs înainte de a continua!",
                        "Atenție", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Logica automată: dacă e batantă → subtipuri, altfel → dimensiuni
            if ("batanta".equals(Session.selectedProductType)) {
                navigator.goToStep(1);
            } else {

                navigator.goToStep(3);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(42, 42, 42));
        bottomPanel.add(new JLabel("Selectați tipul produsului dorit", SwingConstants.CENTER));
        bottomPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        bottomPanel.add(next);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createImageButton(String text, String imgPath, String key) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getClassLoader().getResource(imgPath));
            if (icon.getImageLoadStatus() == MediaTracker.ERRORED) throw new Exception();
            Image img = icon.getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        } catch (Exception ignored) {
            icon = createFallbackIcon();
        }

        JButton btn = new JButton(text, icon);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setPreferredSize(new Dimension(200, 200));
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NORMAL_BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn != selectedButton) {
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(HOVER_BORDER_COLOR, 2),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn != selectedButton) {
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(NORMAL_BORDER_COLOR, 2),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                }
            }
        });

        return btn;
    }

    private void selectButton(JButton button, String key, String text) {
        if (selectedButton != null) {
            selectedButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(NORMAL_BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            selectedButton.setBackground(Color.DARK_GRAY);
        }

        selectedButton = button;
        selectedButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SELECTED_BORDER_COLOR, 3),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        selectedButton.setBackground(new Color(60, 60, 60));

        Session.selectedCabinaType = key;
        Session.selectedProductType = key; // cheia pentru logica dinamică viitoare
        System.out.println("Produs selectat: " + key);
    }

    private ImageIcon createFallbackIcon() {
        BufferedImage img = new BufferedImage(180, 150, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(new Color(70, 70, 70));
        g2d.fillRect(0, 0, 180, 150);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2d.drawString("Imagine indisponibilă", 20, 75);
        g2d.dispose();
        return new ImageIcon(img);
    }

    public void refreshSelection() {
        if (Session.selectedCabinaType != null && !Session.selectedCabinaType.isBlank()) {
            // Poți re-selecta automat butonul dacă vrei (opțional)
        }
    }
}