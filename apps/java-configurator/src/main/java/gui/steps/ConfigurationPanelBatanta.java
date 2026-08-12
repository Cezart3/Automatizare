package gui.steps;

import db.ProductDAO;
import model.glass.Glass;
import util.api.ExchangeRateAPI;
import util.calculator.PriceCalculator;
import util.session.Session;
import util.config.Settings;
import util.export.PdfExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfigurationPanelBatanta extends JPanel {
    // Colors
    private static final Color BG_COLOR = new Color(30, 30, 30);
    private static final Color CARD_BG_COLOR = new Color(45, 45, 45);
    private static final Color ACCENT_COLOR = new Color(255, 120, 160); // Orange
    private static final Color ACCENT_HOVER_COLOR = new Color(255, 120, 160);
    private static final Color SECONDARY_COLOR = new Color(33, 150, 243); // Blue
    private static final Color SECONDARY_HOVER_COLOR = new Color(25, 118, 210);
    private static final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private static final Color TEXT_SECONDARY = new Color(180, 180, 180);

    private final JTextArea summary = new JTextArea();
    private final JLabel priceLabel = new JLabel("", SwingConstants.CENTER);
    private final List<Glass> sticle;
    private final ProductDAO productDAO;
    private GlassPanel step6Panel;
    private double pretFinalEuro = 0.0;
    private double pretFinalRon = 0.0;
    private double curs = 0.0;
    private double pretEuro = 0.0;

    // Constructor original
    public ConfigurationPanelBatanta(ConfigPanel navigator, List<Glass> sticle, ProductDAO productDAO) {
        this.sticle = sticle;
        this.productDAO = productDAO;
        initializeUI();
    }

    // Constructor nou
    public ConfigurationPanelBatanta(ConfigPanel navigator, List<Glass> sticle, ProductDAO productDAO, GlassPanel step6Panel) {
        this.sticle = sticle;
        this.productDAO = productDAO;
        this.step6Panel = step6Panel;
        initializeUI();
    }

    public void setStep6Panel(GlassPanel step6Panel) {
        this.step6Panel = step6Panel;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // --- Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setBorder(new EmptyBorder(25, 20, 10, 20));

        JLabel title = new JLabel("Rezumat Configurație", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        headerPanel.add(title, BorderLayout.CENTER);

        JLabel subtitle = new JLabel("Verifică detaliile și calculează oferta finală", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content (Split: Summary Left, Price/Actions Right) ---
        // Using a GridBagLayout for centering and responsiveness
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 1. Summary Card (Left or Top)
        JPanel summaryCard = createCardPanel();
        summaryCard.setLayout(new BorderLayout());

        JLabel summaryTitle = new JLabel(" Detalii Tehnice");
        summaryTitle.setIcon(UIManager.getIcon("FileView.fileIcon")); // Fallback icon if available
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryTitle.setForeground(ACCENT_COLOR);
        summaryTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        summaryCard.add(summaryTitle, BorderLayout.NORTH);

        summary.setEditable(false);
        summary.setFont(new Font("Consolas", Font.PLAIN, 14)); // Monospaced for alignment
        summary.setBackground(CARD_BG_COLOR);
        summary.setForeground(TEXT_SECONDARY);
        summary.setLineWrap(true);
        summary.setWrapStyleWord(true);
        summary.setBorder(null);

        JScrollPane scroll = new JScrollPane(summary);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CARD_BG_COLOR);
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        summaryCard.add(scroll, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        contentPanel.add(summaryCard, gbc);

        // 2. Price & Actions Panel (Right or Bottom)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setBackground(BG_COLOR);

        // Price Card
        JPanel priceCard = createCardPanel();
        priceCard.setLayout(new BorderLayout());

        JLabel priceTitle = new JLabel(" Ofertă Preț", SwingConstants.CENTER);
        priceTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceTitle.setForeground(TEXT_PRIMARY);
        priceTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        priceCard.add(priceTitle, BorderLayout.NORTH);

        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceLabel.setForeground(TEXT_PRIMARY);
        // Initial placeholder text
        priceLabel.setText("<html><div style='text-align:center; color:#808080;'>Apăsați 'Calculează Preț'<br>pentru a genera oferta.</div></html>");
        priceCard.add(priceLabel, BorderLayout.CENTER);

        rightPanel.add(priceCard, BorderLayout.CENTER);

        // Buttons Container
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        buttonsPanel.setBackground(BG_COLOR);

        ModernButton calcBtn = new ModernButton("Calculează Preț", ACCENT_COLOR, ACCENT_HOVER_COLOR);
        calcBtn.addActionListener(e -> calculatePrice());

        ModernButton exportPdfBtn = new ModernButton("Export PDF", SECONDARY_COLOR, SECONDARY_HOVER_COLOR);
        exportPdfBtn.addActionListener(e -> exportToPdf());

        buttonsPanel.add(calcBtn);
        buttonsPanel.add(exportPdfBtn);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        contentPanel.add(rightPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // --- Listeners ---
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                summary.setText(buildSummary());
                summary.setCaretPosition(0);
            }
        });
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    // --- Logic Methods (Kept mostly original but cleaned up) ---

    private void calculatePrice() {
        Session.save();

        // Show loading state
        priceLabel.setText("<html><div style='text-align:center; color:#FF5722;'>Se calculează...</div></html>");

        // Run on worker thread
        new SwingWorker<Void, Void>() {
            private double adaosComercialEuro = 0.0;
            private double pretCuAdaosEuro = 0.0;
            private double tvaEuro = 0.0;

            @Override
            protected Void doInBackground() {
                // Heavy lifting
                pretEuro = PriceCalculator.calculeaza(sticle, productDAO);
                curs = ExchangeRateAPI.getEuroToRon();

                double pretRon = pretEuro * curs;
                double adaosPercent = Settings.getAdaosComercial();
                double tvaPercent = Settings.getTVA();

                adaosComercialEuro = pretEuro * (adaosPercent / 100);
                pretCuAdaosEuro = pretEuro + adaosComercialEuro;
                tvaEuro = pretCuAdaosEuro * (tvaPercent / 100);
                pretFinalEuro = pretCuAdaosEuro + tvaEuro;

                double adaosComercialRon = pretRon * (adaosPercent / 100);
                double pretCuAdaosRon = pretRon + adaosComercialRon;
                double tvaRon = pretCuAdaosRon * (tvaPercent / 100);
                pretFinalRon = pretCuAdaosRon + tvaRon;

                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    updatePriceLabel();
                } catch (Exception e) {
                    priceLabel.setText("<html><div style='text-align:center; color:red;'>Eroare calcul!</div></html>");
                    e.printStackTrace();
                }
            }

            private void updatePriceLabel() {
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);

                String css = "<style>" +
                        "body { font-family: 'Segoe UI', sans-serif; }" +
                        ".label { color: #aaaaaa; font-size: 10px; }" +
                        ".value { color: #ffffff; font-weight: bold; }" +
                        ".highlight { color: #FF5722; font-size: 14px; }" +
                        ".total-ron { color: #FF5722; font-size: 22px; font-weight: bold; }" +
                        ".divider { border-top: 1px solid #555555; margin: 5px 0; }" +
                        "</style>";

                String content = String.format(
                        "<html>%s<div style='text-align: center; width: 100%%;'>" +
                                "<span class='label'>Curs Valutar: %.4f RON/EUR</span><br><br>" +

                                "<table width='100%%'>" +
                                "<tr><td align='left' class='label'>Preț Producție:</td><td align='right' class='value'>%s €</td></tr>" +
                                "<tr><td align='left' class='label'>Adaos (%s%%):</td><td align='right' class='value'>%s €</td></tr>" +
                                "<tr><td align='left' class='label'>TVA (%s%%):</td><td align='right' class='value'>%s €</td></tr>" +
                                "</table>" +

                                "<div class='divider'></div>" +
                                "<span class='highlight'>TOTAL: %s EUR</span><br><br>" +

                                "<span class='label'>PREȚ FINAL (RON)</span><br>" +
                                "<span class='total-ron'>%s RON</span>" +
                                "</div></html>",
                        css,
                        curs,
                        nf.format(pretEuro),
                        nf.format(Settings.getAdaosComercial()), nf.format(adaosComercialEuro),
                        nf.format(Settings.getTVA()), nf.format(tvaEuro),
                        nf.format(pretFinalEuro),
                        nf.format(pretFinalRon)
                );

                priceLabel.setText(content);
            }
        }.execute();
    }

    private void exportToPdf() {
        if (pretFinalEuro == 0) {
            JOptionPane.showMessageDialog(this,
                    "Vă rugăm să calculați prețul înainte de export.",
                    "Atenție", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvează Oferta PDF");
        fileChooser.setSelectedFile(new File("Oferta_Cabina_" +
                new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }

            File finalFile = file;
            // Show progress
            JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Exporting...", true);
            progressDialog.setSize(200, 100);
            progressDialog.setLocationRelativeTo(this);
            JLabel lbl = new JLabel("Generare PDF...", SwingConstants.CENTER);
            progressDialog.add(lbl);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    PdfExporter.exportToPdf(finalFile, step6Panel, pretEuro, pretFinalEuro, pretFinalRon, curs);
                    return null;
                }

                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        get();
                        JOptionPane.showMessageDialog(ConfigurationPanelBatanta.this,
                                "PDF salvat cu succes:\n" + finalFile.getName(),
                                "Succes", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(ConfigurationPanelBatanta.this,
                                "Eroare: " + e.getMessage(),
                                "Eroare Export", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
            // Note: In a real app, don't block EDT with modal dialog like this if possible,
            // but for simple Swing apps it's acceptable to ensure user waits.
        }
    }

    private String buildSummary() {
        StringBuilder sb = new StringBuilder();

        // Helper for sections
        Runnable addSeparator = () -> sb.append("\n----------------------------------------\n");

        sb.append("CONFIGURAȚIE CABINĂ DE DUȘ\n");
        addSeparator.run();

        sb.append(String.format("%-20s %s\n", "Tip:", getCabinaTypeName(Session.selectedCabinaType)));
        sb.append(String.format("%-20s %s\n", "Dimensiuni:", Session.dimensiuni));
        sb.append(String.format("%-20s %s\n", "Material:", (Session.finisajID == 1 || Session.finisajID == 2) ? "Zinc" : "Oțel Inoxidabil"));
        sb.append(String.format("%-20s %s\n", "Finisaj:", getFinishName(Session.finisajID)));

        addSeparator.run();
        sb.append("SPECIFICAȚII STICLĂ\n\n");
        sb.append(String.format("• %-18s %s\n", "Tip:", Session.sticlaNume != null ? Session.sticlaNume : "-"));
        sb.append(String.format("• %-18s %s mm\n", "Grosime:", Session.sticlaGrosime != null ? Session.sticlaGrosime : "-"));
        sb.append(String.format("• %-18s %s\n", "Prelucrare:", Session.sticlaTip != null ? Session.sticlaTip : "-"));
        sb.append(String.format("• %-18s %s\n", "Găurire:", Session.sticlaGaurire != null ? Session.sticlaGaurire : "-"));

        if (Session.sticlaNumarGauririExtra != null && !Session.sticlaNumarGauririExtra.equals("0")) {
            sb.append(String.format("• %-18s %s\n", "Găuri Extra:", Session.sticlaNumarGauririExtra));
        }
        if (Session.sticlaNumarDecupajeExtra != null && !Session.sticlaNumarDecupajeExtra.equals("0")) {
            sb.append(String.format("• %-18s %s\n", "Decupaje Extra:", Session.sticlaNumarDecupajeExtra));
        }

        if (Session.sticlaFormaSablonMap != null && !Session.sticlaFormaSablonMap.isEmpty()) {
            sb.append("\nForme Speciale:\n");
            Session.sticlaFormaSablonMap.forEach((k, v) ->
                    sb.append(String.format("  Sticla %d: %s\n", k + 1, v)));
        }

        addSeparator.run();
        sb.append("FERONERIE ȘI ACCESORII\n\n");
        if (Session.hardwareMulti != null && !Session.hardwareMulti.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : Session.hardwareMulti.entrySet()) {
                List<String> items = entry.getValue();
                if (items != null && !items.isEmpty()) {
                    sb.append(formatLabel(entry.getKey())).append(":\n");
                    for (String item : items) {
                        sb.append("  + ").append(item).append("\n");
                    }
                    sb.append("\n");
                }
            }
        } else {
            sb.append("Nicio feronerie selectată.\n");
        }

        return sb.toString();
    }

    // --- Helpers ---

    private String getCabinaTypeName(String type) {
        if (type == null) return "Neselectat";
        return switch (type) {
            case "tipu_1" -> "Tip 1 (2 sticle)";
            case "tipu_2" -> "Tip 2 (Ușă + Fix)";
            case "tipu_3" -> "Tip 3 (Colț)";
            case "tipu_4" -> "Tip 4 (Colț + Ușă)";
            case "tipu_5" -> "Tip 5 (Walk-in)";
            case "tipu_6" -> "Tip 6 (Walk-in + Ușă)";
            default -> type;
        };
    }

    private String getFinishName(int finishId) {
        return switch (finishId) {
            case 1 -> "Satin";
            case 2 -> "Lucios";
            case 3 -> "Negru Mat";
            case 4 -> "Alb";
            case 5 -> "Auriu (Gold)";
            case 6 -> "Rose Gold";
            default -> "Standard";
        };
    }

    private String formatLabel(String key) {
        if (key == null) return "";
        String formatted = key.replace('_', ' ');
        return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
    }

    // --- Custom UI Components ---

    /**
     * Modern Button with rounded corners and hover effects
     */
    private static class ModernButton extends JButton {
        private final Color normalColor;
        private final Color hoverColor;

        public ModernButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(200, 45));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(hoverColor.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(normalColor);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    /**
     * Minimalist ScrollBar UI
     */
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(80, 80, 80);
            this.trackColor = new Color(40, 40, 40);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            return jbutton;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);
            g2.dispose();
        }
    }
}