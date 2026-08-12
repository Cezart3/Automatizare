package gui.steps;

import gui.navigation.StepNavigator;
import util.session.Session;
import util.config.ProductType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DimensiuniPanel extends JPanel {

    // === CULORI LUXURY DARK ===
    private static final Color BG_COLOR = new Color(12, 14, 24);
    private static final Color CARD_BG_COLOR = new Color(22, 26, 42);
    private static final Color ACCENT_COLOR = new Color(99, 102, 241);
    private static final Color ACCENT_HOVER_COLOR = new Color(129, 140, 248);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private static final Color BORDER_COLOR = new Color(51, 65, 85);
    private static final Color FIELD_BG = new Color(30, 35, 52);

    private JPanel cardsContainer;
    private JTextField[] fields;
    private JCheckBox[] formaCheckboxes;
    private JCheckBox[] sablonCheckboxes;
    private final StepNavigator navigator;

    public DimensiuniPanel(StepNavigator navigator) {
        this.navigator = navigator;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(25, 20, 10, 20));

        JLabel title = new JLabel("Introduceți Dimensiunile", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        header.add(title, BorderLayout.CENTER);

        JLabel subtitle = new JLabel(getSubtitleText(), SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        header.add(subtitle, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // === CARDURI ===
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(BG_COLOR);

        JScrollPane scroll = new JScrollPane(cardsContainer);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(BG_COLOR);
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 40, 10, 40);
        centerWrapper.add(scroll, gbc);
        add(centerWrapper, BorderLayout.CENTER);

        // === BUTON URMĂTOR ===
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(BG_COLOR);
        bottom.setBorder(new EmptyBorder(20, 0, 40, 0));

        ModernButton nextBtn = new ModernButton("Următorul pas", ACCENT_COLOR, ACCENT_HOVER_COLOR);
        nextBtn.setPreferredSize(new Dimension(260, 52));
        nextBtn.addActionListener(e -> saveDimensionsAndNext());
        bottom.add(nextBtn);
        add(bottom, BorderLayout.SOUTH);

        refreshFromCabinaType();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) { adjustCardHeights(); }
            @Override public void componentShown(java.awt.event.ComponentEvent e) { adjustCardHeights(); }
        });
    }

    private String getSubtitleText() {
        ProductType type = ProductType.fromSession();
        if (type != null) {
            return switch (type) {
                case PANOU -> "Completați dimensiunile panoului fix";
                case BALUSTRADA -> "Completați dimensiunile balustradei";
                case CULISANTA -> "Completați dimensiunile cabinei culisante";
                default -> "Completați înălțimea și lățimea fiecărei sticle";
            };
        }
        return "Completați înălțimea și lățimea fiecărei sticle";
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            refreshFromCabinaType();
            SwingUtilities.invokeLater(this::adjustCardHeights);
        }
    }

    private void refreshFromCabinaType() {
        cardsContainer.removeAll();

        int nrSticle = getNumarSticle();
        fields = new JTextField[nrSticle * 2];
        formaCheckboxes = new JCheckBox[nrSticle];
        sablonCheckboxes = new JCheckBox[nrSticle];

        String[] saved = Session.dimensiuni != null && !Session.dimensiuni.isBlank()
                ? Session.dimensiuni.split("x") : new String[0];

        for (int i = 0; i < nrSticle; i++) {
            JPanel card = createGlassCard(i, saved);
            cardsContainer.add(card);
            cardsContainer.add(Box.createVerticalStrut(15));
        }

        cardsContainer.add(Box.createVerticalGlue());
        revalidate();
        repaint();
        SwingUtilities.invokeLater(this::adjustCardHeights);
    }

    // === CALCULEAZĂ NUMĂRUL DE STICLE BAZAT PE TIP ȘI SUBTIP ===
    private int getNumarSticle() {
        ProductType type = ProductType.fromSession();

        // Pentru BATANTA, trebuie să verificăm subtipul pentru a determina nr. sticle
        if (type == ProductType.BATANTA) {
            String subtip = Session.selectedCabinaType;
            if (subtip != null && !subtip.isBlank()) {
                String num = subtip.replaceAll("[^0-9]", "");
                if (!num.isEmpty()) {
                    return switch (Integer.parseInt(num)) {
                        case 1, 2 -> 2;  // tipu_1, tipu_2 = 2 sticle
                        case 3, 4 -> 3;  // tipu_3, tipu_4 = 3 sticle
                        case 5, 6 -> 4;  // tipu_5, tipu_6 = 4 sticle
                        default -> 2;
                    };
                }
            }
            return 2; // default pentru batanta fără subtip
        }

        // Pentru alte tipuri (PANOU, CULISANTA, BALUSTRADA)
        if (type != null) {
            return type.getMinGlasses();
        }

        // Fallback final
        return 2;
    }

    private JPanel createGlassCard(int index, String[] saved) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // Titlu dinamic
        String titleText = getCardTitle(index);
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(ACCENT_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 0, 20));
        card.add(title, BorderLayout.WEST);

        // Layout principal
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);

        JPanel fieldsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        fieldsPanel.setOpaque(false);
        fieldsPanel.add(createDimensionField("Înălțime (mm)", index * 2, saved));
        fieldsPanel.add(createDimensionField("Lățime (mm)", index * 2 + 1, saved));

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        content.add(fieldsPanel, gbc);

        // Checkbox-uri doar dacă produsul suportă formă/sablon
        if (shouldShowFormaSablon()) {
            JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            checkboxPanel.setOpaque(false);

            JCheckBox formaCb = new JCheckBox("Formă");
            JCheckBox sablonCb = new JCheckBox("Sablon");
            styleCheckbox(formaCb);
            styleCheckbox(sablonCb);

            formaCheckboxes[index] = formaCb;
            sablonCheckboxes[index] = sablonCb;

            int i = index;
            formaCb.addActionListener(e -> {
                if (formaCb.isSelected()) {
                    sablonCb.setSelected(false);
                    Session.sticlaFormaSablonMap.put(i, "Formă");
                } else Session.sticlaFormaSablonMap.remove(i);
                Session.save();
            });
            sablonCb.addActionListener(e -> {
                if (sablonCb.isSelected()) {
                    formaCb.setSelected(false);
                    Session.sticlaFormaSablonMap.put(i, "Sablon");
                } else Session.sticlaFormaSablonMap.remove(i);
                Session.save();
            });

            String savedOption = Session.sticlaFormaSablonMap.get(i);
            if ("Formă".equals(savedOption)) formaCb.setSelected(true);
            if ("Sablon".equals(savedOption)) sablonCb.setSelected(true);

            checkboxPanel.add(formaCb);
            checkboxPanel.add(sablonCb);

            gbc.gridx = 1;
            gbc.weightx = 0.0;
            gbc.insets = new Insets(0, 20, 0, 0);
            content.add(checkboxPanel, gbc);
        }

        card.add(content, BorderLayout.CENTER);

        // Ajustare dinamică
        card.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = card.getWidth();
                if (width < 1000) {
                    formaCheckboxes[index].setFont(new Font("Segoe UI", 500, 13));
                    sablonCheckboxes[index].setFont(new Font("Segoe UI", 500, 13));
                } else {
                    formaCheckboxes[index].setFont(new Font("Segoe UI", 500, 15));
                    sablonCheckboxes[index].setFont(new Font("Segoe UI", 500, 15));
                }
            }
        });

        return card;
    }

    private String getCardTitle(int index) {
        ProductType type = ProductType.fromSession();
        if (type == ProductType.PANOU || type == ProductType.BALUSTRADA) {
            return "Panou / Balustradă";
        }
        return "Sticla " + (index + 1);
    }

    private boolean shouldShowFormaSablon() {
        ProductType type = ProductType.fromSession();
        return type == null || type == ProductType.BATANTA || type == ProductType.PANOU;
    }

    private JPanel createDimensionField(String label, int fieldIndex, String[] saved) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField field = new JTextField(10);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setPreferredSize(new Dimension(160, 44));
        field.setMaximumSize(new Dimension(160, 44));

        // Focus effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT_COLOR, 2),
                        new EmptyBorder(10, 12, 10, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(10, 12, 10, 12)
                ));
            }
        });

        if (saved.length > fieldIndex && !saved[fieldIndex].isEmpty()) {
            field.setText(saved[fieldIndex]);
        }
        fields[fieldIndex] = field;

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(field);
        return panel;
    }

    private void styleCheckbox(JCheckBox cb) {
        cb.setFont(new Font("Segoe UI", 500, 15));
        cb.setForeground(TEXT_PRIMARY);
        cb.setBackground(CARD_BG_COLOR);
        cb.setFocusPainted(false);
        cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cb.setIcon(new ModernCheckboxIcon(false));
        cb.setSelectedIcon(new ModernCheckboxIcon(true));
    }

    private static class ModernCheckboxIcon implements Icon {
        private final boolean selected;

        public ModernCheckboxIcon(boolean selected) { this.selected = selected; }

        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (selected) {
                g2.setColor(ACCENT_COLOR);
                g2.fillRoundRect(x + 1, y + 1, 18, 18, 6, 6);
                // Draw checkmark
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 5, y + 10, x + 8, y + 14);
                g2.drawLine(x + 8, y + 14, x + 15, y + 5);
            } else {
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x + 1, y + 1, 18, 18, 6, 6);
            }
            g2.dispose();
        }
        @Override public int getIconWidth() { return 22; }
        @Override public int getIconHeight() { return 22; }
    }

    private void adjustCardHeights() {
        int available = getHeight() - 250;
        int cardCount = cardsContainer.getComponentCount();
        if (cardCount <= 1 || available <= 0) return;

        int targetHeight = Math.max(100, available / (cardCount - 1) - 15);

        for (Component c : cardsContainer.getComponents()) {
            if (c instanceof JPanel card && card.getMaximumSize().height > 50) {
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, targetHeight));
                card.setPreferredSize(new Dimension(card.getWidth(), targetHeight));
            }
        }
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private void saveDimensionsAndNext() {
        StringBuilder sb = new StringBuilder();
        for (JTextField f : fields) {
            String t = f.getText().trim();
            sb.append(t.isEmpty() ? "0" : t).append("x");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        Session.dimensiuni = sb.toString();
        Session.save();
        navigator.goToStep(5);
    }

    // === BUTON MODERN ===
    private static class ModernButton extends JButton {
        private final Color normal;
        private final Color hover;

        public ModernButton(String text, Color normal, Color hover) {
            super(text);
            this.normal = normal;
            this.hover = hover;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { setBackground(hover); repaint(); }
                @Override public void mouseExited(MouseEvent e)  { setBackground(normal); repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? hover.darker() : (getModel().isRollover() ? hover : normal));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // === SCROLLBAR MINIMALIST ===
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = new Color(80, 80, 80);
            trackColor = new Color(40, 40, 40);
        }
        @Override protected JButton createDecreaseButton(int o) { return createZeroButton(); }
        @Override protected JButton createIncreaseButton(int o) { return createZeroButton(); }
        private JButton createZeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0,0));
            b.setMinimumSize(new Dimension(0,0));
            b.setMaximumSize(new Dimension(0,0));
            return b;
        }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty() || !scrollbar.isEnabled()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
            g2.dispose();
        }
    }
}