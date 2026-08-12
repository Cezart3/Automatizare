package gui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Tema UI centralizată pentru aplicația Shower Configurator.
 * Design modern, elegant și profesional.
 */
public final class UITheme {

    private UITheme() {
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CULORI PRINCIPALE - Paleta Midnight Pro (Modern SaaS Dark Mode)
    // ═══════════════════════════════════════════════════════════════════════════

    /** Fundal principal - Deep Slate / Midnight Blue */
    public static final Color BG_PRIMARY = new Color(15, 23, 42); // Slate 900

    /** Fundal secundar - pentru carduri și panouri */
    public static final Color BG_SECONDARY = new Color(30, 41, 59); // Slate 800

    /** Fundal pentru carduri evidențiate (mai deschis decât secondary) */
    public static final Color BG_CARD = new Color(51, 65, 85); // Slate 700

    /** Fundal hover pentru elemente interactive */
    public static final Color BG_HOVER = new Color(71, 85, 105); // Slate 600

    /** Fundal pentru elementul activ/selectat */
    public static final Color BG_ACTIVE = new Color(79, 70, 229, 40); // Indigo cu transparență

    // ═══════════════════════════════════════════════════════════════════════════
    // CULORI ACCENT - Electric Indigo & Cyan
    // ═══════════════════════════════════════════════════════════════════════════

    /** Accent principal - Vibrant Indigo */
    public static final Color ACCENT_PRIMARY = new Color(99, 102, 241); // Indigo 500

    /** Accent hover - Lighter Indigo */
    public static final Color ACCENT_HOVER = new Color(129, 140, 248); // Indigo 400

    /** Accent pressed - Deeper Indigo */
    public static final Color ACCENT_PRESSED = new Color(79, 70, 229); // Indigo 600

    /** Accent secundar - Electric Cyan (pentru detalii tech) */
    public static final Color ACCENT_SECONDARY = new Color(34, 211, 238); // Cyan 400

    /** Success - Emerald Smooth */
    public static final Color SUCCESS = new Color(52, 211, 153); // Emerald 400

    /** Warning - Amber Warm */
    public static final Color WARNING = new Color(251, 191, 36); // Amber 400

    /** Error - Rose Vivid */
    public static final Color ERROR = new Color(244, 63, 94); // Rose 500

    // ═══════════════════════════════════════════════════════════════════════════
    // CULORI TEXT
    // ═══════════════════════════════════════════════════════════════════════════

    /** Text principal - Alb curat (High Contrast) */
    public static final Color TEXT_PRIMARY = new Color(248, 250, 252); // Slate 50

    /** Text secundar - Gri rece */
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184); // Slate 400

    /** Text dezactivat */
    public static final Color TEXT_DISABLED = new Color(100, 116, 139); // Slate 500

    /** Text pentru titluri - Alb strălucitor */
    public static final Color TEXT_TITLE = new Color(255, 255, 255);

    // ═══════════════════════════════════════════════════════════════════════════
    // CULORI BORDER
    // ═══════════════════════════════════════════════════════════════════════════

    /** Border subtil */
    public static final Color BORDER_SUBTLE = new Color(51, 65, 85); // Slate 700

    /** Border normal */
    public static final Color BORDER_NORMAL = new Color(71, 85, 105); // Slate 600

    /** Border evidențiat */
    public static final Color BORDER_HIGHLIGHT = ACCENT_PRIMARY;

    // ═══════════════════════════════════════════════════════════════════════════
    // FONTURI - Modern Stack
    // ═══════════════════════════════════════════════════════════════════════════

    public static final String FONT_FAMILY = "Segoe UI";

    public static final Font FONT_TITLE_LARGE = new Font(FONT_FAMILY, Font.BOLD, 32);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.PLAIN, 18);
    public static final Font FONT_HEADING = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font(FONT_FAMILY, Font.BOLD, 14);

    // ═══════════════════════════════════════════════════════════════════════════
    // DIMENSIUNI ȘI SPAȚIERE
    // ═══════════════════════════════════════════════════════════════════════════

    public static final int CORNER_RADIUS = 16;
    public static final int CORNER_RADIUS_SMALL = 8;
    public static final int CORNER_RADIUS_LARGE = 24;

    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
    public static final int PADDING_XLARGE = 32;

    // ═══════════════════════════════════════════════════════════════════════════
    // METODE UTILITARE PENTRU STILIZARE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Creează un border rotunjit cu culoare specificată.
     */
    public static Border createRoundedBorder(Color color, int thickness) {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(color, thickness, CORNER_RADIUS),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));
    }

    /**
     * Creează un panel cu fundal de card (stil Glassy/Panel).
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Drop Shadow subtil
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fill(new RoundRectangle2D.Float(2, 4, getWidth() - 4, getHeight() - 4, CORNER_RADIUS,
                        CORNER_RADIUS));

                // Background
                g2.setColor(BG_SECONDARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 2, getHeight() - 2, CORNER_RADIUS,
                        CORNER_RADIUS));

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE, PADDING_LARGE));
        return card;
    }

    /**
     * Stilizează un JLabel ca titlu.
     */
    public static void styleAsTitle(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_TITLE);
    }

    /**
     * Stilizează un JLabel ca subtitlu.
     */
    public static void styleAsSubtitle(JLabel label) {
        label.setFont(FONT_SUBTITLE);
        label.setForeground(TEXT_PRIMARY);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COMPONENTE CUSTOM
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Buton modern 'State of the Art'.
     */
    public static class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private Color pressedColor;
        private boolean isPrimary;
        private boolean isHovering = false;

        public ModernButton(String text) {
            this(text, true);
        }

        public ModernButton(String text, boolean isPrimary) {
            super(text);
            this.isPrimary = isPrimary;
            if (isPrimary) {
                this.normalColor = ACCENT_PRIMARY;
                this.hoverColor = ACCENT_HOVER;
                this.pressedColor = ACCENT_PRESSED;
            } else {
                this.normalColor = BG_SECONDARY;
                this.hoverColor = BG_HOVER;
                this.pressedColor = BORDER_NORMAL;
            }
            setupButton();
        }

        public ModernButton(String text, Color normal, Color hover) {
            super(text);
            this.normalColor = normal;
            this.hoverColor = hover;
            this.pressedColor = hover.darker();
            this.isPrimary = true;
            setupButton();
        }

        private void setupButton() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(TEXT_PRIMARY);
            setFont(FONT_BUTTON);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(200, 44)); // Slightly sleeker height

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovering = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bgColor;
            if (getModel().isPressed())
                bgColor = pressedColor;
            else if (getModel().isRollover())
                bgColor = hoverColor;
            else
                bgColor = normalColor;

            // Shadow for Primary Buttons (Glow effect)
            if (isPrimary && isHovering) {
                g2.setColor(new Color(normalColor.getRed(), normalColor.getGreen(), normalColor.getBlue(), 80));
                g2.fillRoundRect(0, 4, getWidth(), getHeight() - 2, CORNER_RADIUS + 4, CORNER_RADIUS + 4);
            }

            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS_SMALL,
                    CORNER_RADIUS_SMALL));

            // Border for secondary
            if (!isPrimary) {
                g2.setColor(BORDER_NORMAL);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS_SMALL, CORNER_RADIUS_SMALL);
            }

            super.paintComponent(g);
            g2.dispose();
        }
    }

    /**
     * ScrollBar modern și minimalist.
     */
    public static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = BG_HOVER;
            this.trackColor = BG_PRIMARY;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled())
                return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isDragging ? ACCENT_PRIMARY : thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }
    }

    /**
     * Checkbox modern cu design custom.
     */
    public static class ModernCheckboxIcon implements Icon {
        private final boolean selected;
        private static final int SIZE = 20;

        public ModernCheckboxIcon(boolean selected) {
            this.selected = selected;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (selected) {
                // Fundal selectat
                g2.setColor(ACCENT_PRIMARY);
                g2.fillRoundRect(x, y, SIZE, SIZE, 6, 6);

                // Bifă
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 5, y + 10, x + 8, y + 14);
                g2.drawLine(x + 8, y + 14, x + 15, y + 6);
            } else {
                // Border
                g2.setColor(BORDER_NORMAL);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x + 1, y + 1, SIZE - 2, SIZE - 2, 6, 6);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE + 4;
        }

        @Override
        public int getIconHeight() {
            return SIZE + 4;
        }
    }

    /**
     * Border rotunjit custom.
     */
    public static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Float(x + thickness / 2f, y + thickness / 2f,
                    width - thickness, height - thickness, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    /**
     * Panel cu gradient de fundal.
     */
    public static class GradientPanel extends JPanel {
        private final Color startColor;
        private final Color endColor;
        private final boolean horizontal;

        public GradientPanel(Color start, Color end, boolean horizontal) {
            this.startColor = start;
            this.endColor = end;
            this.horizontal = horizontal;
            setOpaque(false);
        }

        public GradientPanel() {
            this(BG_PRIMARY, BG_SECONDARY, false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint gp;
            if (horizontal) {
                gp = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
            } else {
                gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
            }

            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g);
        }
    }

    /**
     * ComboBox stilizat.
     */
    public static void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(BG_SECONDARY);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(FONT_BODY);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_SUBTLE, 1, CORNER_RADIUS_SMALL),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel label) {
                    label.setBackground(isSelected ? ACCENT_PRIMARY : BG_CARD);
                    label.setForeground(TEXT_PRIMARY);
                    label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                }
                return c;
            }
        });
    }

    /**
     * TextField stilizat.
     */
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_BODY);
        field.setBackground(BG_SECONDARY);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_SUBTLE, 1, CORNER_RADIUS_SMALL),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        field.setPreferredSize(new Dimension(200, 44));
    }

    /**
     * Creează un JScrollPane stilizat.
     */
    public static JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_PRIMARY);
        scrollPane.getViewport().setBackground(BG_PRIMARY);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
}
