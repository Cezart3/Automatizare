package gui.steps;

import gui.navigation.StepNavigator;
import util.session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorPanel extends JPanel {

    private static final String[] COLORS = {
            "Satin", "Lucios", "Negru", "Alb", "Gold", "Rose-Gold"
    };

    private static final Map<String, Integer> COLOR_TO_ID = Map.of(
            "Satin", 1,
            "Lucios", 2,
            "Negru", 3,
            "Alb", 4,
            "Gold", 5,
            "Rose-Gold", 6
    );

    private static final Map<String, Color> COLOR_SWATCH = Map.of(
            "Alb", new Color(0xE6E6E6),
            "Negru", new Color(0x1A1A1A),
            "Satin", new Color(0xC4C4C4),
            "Lucios", new Color(0xD8D8D8),
            "Gold", new Color(0xD4AF37),
            "Rose-Gold", new Color(0xC97A86)
    );

    // Descrieri pentru fiecare finisaj
    private static final Map<String, String> COLOR_DESC = Map.of(
            "Alb", "Finisaj alb mat, elegant",
            "Negru", "Finisaj negru sofisticat",
            "Satin", "Finisaj satinat mat",
            "Lucios", "Finisaj cromat lucios",
            "Gold", "Finisaj auriu premium",
            "Rose-Gold", "Finisaj roz-auriu modern"
    );

    // Paletă luxury dark
    private static final Color BG_DARK = new Color(12, 14, 24);
    private static final Color BG_CARD = new Color(22, 26, 42);
    private static final Color BG_CARD_HOVER = new Color(32, 38, 58);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color ACCENT_GLOW = new Color(99, 102, 241, 40);
    private static final Color TEXT_WHITE = new Color(248, 250, 252);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);
    private static final Color BORDER_SUBTLE = new Color(51, 65, 85);
    private static final Color SUCCESS = new Color(52, 211, 153);

    private final List<FinishCard> cards = new ArrayList<>();
    private String selectedColor = COLORS[0];
    private final StepNavigator navigator;

    public ColorPanel(StepNavigator navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(BG_DARK);

        // Header
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // Grid cu carduri
        JPanel cardsPanel = createCardsPanel();
        add(cardsPanel, BorderLayout.CENTER);

        // Footer cu buton
        JPanel footer = createFooter();
        add(footer, BorderLayout.SOUTH);

        // Setare implicită
        selectColor(COLORS[0]);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JLabel title = new JLabel("Selectează Finisajul");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Alege culoarea profilelor și accesoriilor");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        return header;
    }

    private JPanel createCardsPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);

        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setBackground(BG_DARK);
        grid.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        for (String colorName : COLORS) {
            FinishCard card = new FinishCard(colorName);
            cards.add(card);
            grid.add(card);
        }

        wrapper.add(grid);
        return wrapper;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(BG_DARK);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 0, 35, 0));

        JButton nextBtn = new JButton("Următorul pas") {
            private boolean hovering = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();

                // Umbră
                if (!getModel().isPressed()) {
                    g2.setColor(new Color(0, 0, 0, 40));
                    g2.fill(new RoundRectangle2D.Float(2, 3, w - 2, h - 2, 12, 12));
                }

                // Fundal
                Color bg = getModel().isPressed() ? ACCENT.darker() : (hovering ? ACCENT.brighter() : ACCENT);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, getModel().isPressed() ? 1 : 0, w - 2, h - 2, 12, 12));

                // Text
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int textX = (w - fm.stringWidth(text)) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2 - (getModel().isPressed() ? 0 : 1);
                g2.drawString(text, textX, textY);

                g2.dispose();
            }
        };
        nextBtn.setPreferredSize(new Dimension(200, 50));
        nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nextBtn.setContentAreaFilled(false);
        nextBtn.setBorderPainted(false);
        nextBtn.setFocusPainted(false);
        nextBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextBtn.addActionListener(e -> navigator.goToStep(4));

        footer.add(nextBtn);
        return footer;
    }

    private void selectColor(String colorName) {
        selectedColor = colorName;
        Session.material = colorName;
        Session.finisajID = COLOR_TO_ID.getOrDefault(colorName, 0);

        for (FinishCard card : cards) {
            card.setSelected(card.colorName.equals(colorName));
        }
    }

    // === CARD PENTRU UN FINISAJ ===
    private class FinishCard extends JPanel {
        private final String colorName;
        private final Color swatchColor;
        private boolean selected = false;
        private boolean hovering = false;

        public FinishCard(String colorName) {
            this.colorName = colorName;
            this.swatchColor = COLOR_SWATCH.getOrDefault(colorName, Color.GRAY);

            setOpaque(false);
            setPreferredSize(new Dimension(180, 160));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectColor(colorName);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovering = false;
                    repaint();
                }
            });
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int w = getWidth(), h = getHeight();
            int radius = 16;

            // Glow pentru selectat
            if (selected) {
                g2.setColor(ACCENT_GLOW);
                g2.fill(new RoundRectangle2D.Float(-4, -4, w + 8, h + 8, radius + 4, radius + 4));
            }

            // Fundal card
            Color bgColor = selected ? new Color(30, 35, 55) : (hovering ? BG_CARD_HOVER : BG_CARD);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, radius, radius));

            // Border
            if (selected) {
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(2.5f));
            } else {
                g2.setColor(hovering ? new Color(71, 85, 105) : BORDER_SUBTLE);
                g2.setStroke(new BasicStroke(1f));
            }
            g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, radius, radius));

            // Cercul cu culoarea
            int circleSize = 56;
            int circleX = (w - circleSize) / 2;
            int circleY = 24;

            // Umbră pentru cerc
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillOval(circleX + 2, circleY + 3, circleSize, circleSize);

            // Cercul principal
            g2.setColor(swatchColor);
            g2.fillOval(circleX, circleY, circleSize, circleSize);

            // Efect glossy pentru "Lucios"
            if (colorName.equals("Lucios")) {
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillOval(circleX + 8, circleY + 6, circleSize - 16, circleSize / 2 - 4);
            }

            // Efect metalic pentru Gold și Rose-Gold
            if (colorName.equals("Gold") || colorName.equals("Rose-Gold")) {
                GradientPaint metallic = new GradientPaint(
                        circleX, circleY, new Color(255, 255, 255, 60),
                        circleX + circleSize, circleY + circleSize, new Color(0, 0, 0, 30)
                );
                g2.setPaint(metallic);
                g2.fillOval(circleX, circleY, circleSize, circleSize);
            }

            // Border cerc
            g2.setColor(new Color(0, 0, 0, 40));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(circleX, circleY, circleSize, circleSize);

            // Checkmark pentru selectat
            if (selected) {
                int checkSize = 22;
                int checkX = circleX + circleSize - checkSize + 4;
                int checkY = circleY + circleSize - checkSize + 4;

                // Fundal check
                g2.setColor(SUCCESS);
                g2.fillOval(checkX, checkY, checkSize, checkSize);

                // Checkmark
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(checkX + 5, checkY + 11, checkX + 9, checkY + 15);
                g2.drawLine(checkX + 9, checkY + 15, checkX + 16, checkY + 7);
            }

            // Numele finisajului
            g2.setColor(selected ? TEXT_WHITE : (hovering ? TEXT_WHITE : TEXT_MUTED));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            int textY = circleY + circleSize + 28;
            g2.drawString(colorName, (w - fm.stringWidth(colorName)) / 2, textY);

            // Descrierea
            String desc = COLOR_DESC.getOrDefault(colorName, "");
            g2.setColor(new Color(100, 116, 139));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            fm = g2.getFontMetrics();
            int descY = textY + 18;
            g2.drawString(desc, (w - fm.stringWidth(desc)) / 2, descY);

            g2.dispose();
        }
    }
}
