package gui.steps;

import gui.navigation.StepNavigator;
import util.session.Session;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BatantaSubtipPanel extends JPanel {
    private JButton selectedButton = null;
    private final Color SELECTED_BORDER_COLOR = new Color(255, 120, 160);
    private final Color NORMAL_BORDER_COLOR = new Color(100, 100, 100);
    private final Color HOVER_BORDER_COLOR = new Color(150, 150, 150);

    public BatantaSubtipPanel(StepNavigator navigator) {
        setLayout(new BorderLayout());
        setBackground(new Color(42,42,42));

        JLabel title = new JLabel("Tipologie Cabină", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 30, 30));
        grid.setBackground(new Color(42,42,42));
        grid.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Butoane tip cabine
        JButton t1 = createImageButton("Tipul 1", "Pictures/tipu_1.jpg", "tipu_1");
        JButton t2 = createImageButton("Tipul 2", "Pictures/tipu_2.jpg", "tipu_2");
        JButton t3 = createImageButton("Tipul 3", "Pictures/tipu_3.jpg", "tipu_3");
        JButton t4 = createImageButton("Tipul 4", "Pictures/tipu_4.jpg", "tipu_4");
        JButton t5 = createImageButton("Tipul 5", "Pictures/tipu_5.jpg", "tipu_5");
        JButton t6 = createImageButton("Tipul 6", "Pictures/tipu_5.jpg", "tipu_6");

        grid.add(t1); grid.add(t2); grid.add(t3);
        grid.add(t4); grid.add(t5); grid.add(t6);

        add(grid, BorderLayout.CENTER);

        // Buton Next
        JButton next = new JButton("Următorul pas");
        next.setPreferredSize(new Dimension(200,40));
        next.setBackground(new Color(255, 120, 160));
        next.setForeground(Color.WHITE);
        next.setFocusPainted(false);
        next.setFont(new Font("Segoe UI", Font.BOLD, 14));

        next.addActionListener(e -> {
            if (Session.selectedCabinaType == null || Session.selectedCabinaType.isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "Vă rugăm să selectați un tip de cabină înainte de a continua!",
                        "Atenție", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Salvează sesiunea și navighează către FERONERIE (Step 3)
            Session.save();
            System.out.println("Navigating to Step 3 (Feronerie) with cabina type: " + Session.selectedCabinaType);
            navigator.goToStep(3);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(42,42,42));

        // Adăugăm un label informativ
        JLabel infoLabel = new JLabel("Selectați un tip de cabină făcând click pe imagine");
        infoLabel.setForeground(Color.LIGHT_GRAY);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        bottomPanel.add(infoLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Spațiu
        bottomPanel.add(next);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createImageButton(String text, String imgPath, String key) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getClassLoader().getResource(imgPath));
            Image img = icon.getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        } catch (Exception ignored) {
            System.err.println("Eroare la încărcarea imaginii: " + imgPath);
            // Fallback: creează un icon colorat dacă imaginea nu există
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

        // Tooltip pentru informații suplimentare
        btn.setToolTipText("Click pentru a selecta " + text);

        // Efect de hover
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

        btn.addActionListener(e -> selectButton(btn, key, text));
        return btn;
    }

    private void selectButton(JButton button, String key, String text) {
        // Resetează border-ul butonului anterior selectat
        if (selectedButton != null) {
            selectedButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(NORMAL_BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }

        // Setează noul buton selectat
        selectedButton = button;
        selectedButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SELECTED_BORDER_COLOR, 3),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Actualizează sesiunea
        Session.selectedCabinaType = key;
        System.out.println("Tip cabina selectat: " + key + " - " + text);

        // Feedback vizual suplimentar
        button.setBackground(new Color(60, 60, 60));

        // Schimbă temporar culoarea textului pentru feedback
        Timer timer = new Timer(300, event -> {
            button.setForeground(Color.WHITE);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private ImageIcon createFallbackIcon() {
        // Creează un icon de rezervă colorat
        BufferedImage image = new BufferedImage(180, 150, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Gradient de fundal
        GradientPaint gradient = new GradientPaint(0, 0, new Color(70, 70, 70), 0, 150, new Color(40, 40, 40));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 180, 150);

        // Border
        g2d.setColor(NORMAL_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(1, 1, 177, 147);

        // Text "Imagine indisponibilă"
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String text = "Imagine indisponibilă";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, (180 - textWidth) / 2, 75);

        g2d.dispose();
        return new ImageIcon(image);
    }

    // Metodă pentru a pre-selecta un buton dacă există deja o selecție în sesiune
    public void refreshSelection() {
        if (Session.selectedCabinaType != null && !Session.selectedCabinaType.isEmpty()) {
            Component[] components = ((JPanel)((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER))
                    .getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    Component[] buttons = ((JPanel) comp).getComponents();
                    for (Component btnComp : buttons) {
                        if (btnComp instanceof JButton) {
                            JButton btn = (JButton) btnComp;
                            String btnText = btn.getText();
                            String expectedKey = getKeyFromButtonText(btnText);
                            if (Session.selectedCabinaType.equals(expectedKey)) {
                                selectButton(btn, Session.selectedCabinaType, btnText);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private String getKeyFromButtonText(String buttonText) {
        return switch (buttonText) {
            case "Tipul 1" -> "tipu_1";
            case "Tipul 2" -> "tipu_2";
            case "Tipul 3" -> "tipu_3";
            case "Tipul 4" -> "tipu_4";
            case "Tipul 5" -> "tipu_5";
            case "Tipul 6" -> "tipu_6";
            default -> "";
        };
    }
}