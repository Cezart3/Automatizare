package gui.navigation;

import gui.layout.ConfiguratorFrame;
import gui.steps.ConfigPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StepSideNavPanel extends JPanel {

    private final List<StepItem> stepItems;
    private final ConfigPanel navigator;
    private final ConfiguratorFrame frame;
    private int activeStep = 0;

    // Set care ține minte pașii efectiv vizitați
    private final Set<Integer> visitedSteps = new HashSet<>();

    // === PALETA LUXURY DARK 2025 ===
    private static final Color BG_PRIMARY = new Color(12, 14, 24);
    private static final Color BG_SECONDARY = new Color(18, 21, 32);
    private static final Color BG_CARD = new Color(24, 28, 44);
    private static final Color BG_HOVER = new Color(35, 42, 62);
    private static final Color BG_ACTIVE = new Color(99, 102, 241, 20);

    private static final Color ACCENT_MAIN = new Color(99, 102, 241);
    private static final Color ACCENT_HOVER = new Color(129, 140, 248);
    private static final Color ACCENT_GLOW = new Color(99, 102, 241, 60);

    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);

    private static final Color BORDER_SUBTLE = new Color(51, 65, 85);
    private static final Color SUCCESS_COLOR = new Color(52, 211, 153);

    public StepSideNavPanel(List<String> steps, ConfigPanel navigator, ConfiguratorFrame frame) {
        this.navigator = navigator;
        this.frame = frame;
        this.stepItems = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(BG_PRIMARY);
        setPreferredSize(new Dimension(280, 0));

        // Header cu gradient subtil
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Panel cu pași
        JPanel stepsPanel = createStepsPanel(steps);
        add(stepsPanel, BorderLayout.CENTER);

        // Footer cu buton Acasă
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);

        // Primul pas este vizitat implicit la deschidere
        visitedSteps.add(0);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Subtle gradient line
                g2.setColor(ACCENT_MAIN);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);

                g2.dispose();
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));

        JLabel title = new JLabel("CONFIGURATOR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Pași de configurare");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        return header;
    }

    private JPanel createStepsPanel(List<String> steps) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(BG_PRIMARY);
        container.setBorder(BorderFactory.createEmptyBorder(15, 16, 15, 16));

        for (int i = 0; i < steps.size(); i++) {
            StepItem item = new StepItem(i, steps.get(i));
            stepItems.add(item);

            container.add(item);

            // Linie de conectare între pași (nu după ultimul)
            if (i < steps.size() - 1) {
                container.add(createConnector());
            }
        }

        container.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_PRIMARY);
        scrollPane.getViewport().setBackground(BG_PRIMARY);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_PRIMARY);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createConnector() {
        JPanel connector = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Linie verticală punctată
                g2.setColor(BORDER_SUBTLE);
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        0, new float[] { 4, 4 }, 0));
                int x = 28; // Aliniat cu centrul cercului
                g2.drawLine(x, 0, x, getHeight());

                g2.dispose();
            }
        };
        connector.setOpaque(false);
        connector.setPreferredSize(new Dimension(248, 20));
        connector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return connector;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_PRIMARY);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 16, 25, 16));

        // Separator
        JPanel separator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BORDER_SUBTLE);
                g.fillRect(20, 0, getWidth() - 40, 1);
            }
        };
        separator.setPreferredSize(new Dimension(0, 1));
        separator.setOpaque(false);
        footer.add(separator, BorderLayout.NORTH);

        // Buton Acasă
        JButton homeButton = new JButton("Acasă") {
            private boolean hovering = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();

                // Fundal
                if (hovering) {
                    g2.setColor(ACCENT_MAIN);
                } else {
                    g2.setColor(BG_CARD);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));

                // Border când nu e hover
                if (!hovering) {
                    g2.setColor(BORDER_SUBTLE);
                    g2.setStroke(new BasicStroke(1));
                    g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 12, 12));
                }

                // Icon săgeată + text
                g2.setColor(hovering ? Color.WHITE : TEXT_SECONDARY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String text = "← " + getText();
                int textX = (w - fm.stringWidth(text)) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, textX, textY);

                g2.dispose();
            }
        };
        homeButton.setPreferredSize(new Dimension(248, 48));
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> frame.showHome());

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(homeButton);
        footer.add(buttonWrapper, BorderLayout.CENTER);

        return footer;
    }

    public void setActiveStep(int stepIdx) {
        this.activeStep = stepIdx;

        // Marcăm pasul curent ca vizitat
        visitedSteps.add(stepIdx);

        for (int i = 0; i < stepItems.size(); i++) {
            stepItems.get(i).setActive(i == stepIdx);
            // Un pas este "completed" doar dacă a fost vizitat ȘI nu este pasul curent
            stepItems.get(i).setCompleted(visitedSteps.contains(i) && i != stepIdx);
        }
        repaint();
    }

    // === CLASA INTERNĂ PENTRU UN PAS ===
    private class StepItem extends JPanel {
        private final int index;
        private final String name;
        private boolean active = false;
        private boolean completed = false;
        private boolean hovering = false;

        public StepItem(int index, String name) {
            this.index = index;
            this.name = name;

            setOpaque(false);
            setPreferredSize(new Dimension(248, 56));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    navigator.showStep(index);
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

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int w = getWidth(), h = getHeight();

            // Fundal cu hover/active
            if (active) {
                g2.setColor(BG_ACTIVE);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));

                // Border accent pentru activ
                g2.setColor(ACCENT_MAIN);
                g2.setStroke(new BasicStroke(2));
                g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, 12, 12));
            } else if (hovering) {
                g2.setColor(BG_HOVER);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));
            }

            // Cercul cu număr
            int circleX = 12;
            int circleY = (h - 32) / 2;
            int circleSize = 32;

            if (active) {
                // Glow pentru activ
                g2.setColor(ACCENT_GLOW);
                g2.fillOval(circleX - 3, circleY - 3, circleSize + 6, circleSize + 6);

                g2.setColor(ACCENT_MAIN);
                g2.fillOval(circleX, circleY, circleSize, circleSize);

                g2.setColor(Color.WHITE);
            } else if (completed) {
                g2.setColor(SUCCESS_COLOR);
                g2.fillOval(circleX, circleY, circleSize, circleSize);

                // Checkmark
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(circleX + 9, circleY + 16, circleX + 14, circleY + 21);
                g2.drawLine(circleX + 14, circleY + 21, circleX + 23, circleY + 11);
            } else {
                g2.setColor(BG_CARD);
                g2.fillOval(circleX, circleY, circleSize, circleSize);

                g2.setColor(BORDER_SUBTLE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(circleX, circleY, circleSize, circleSize);

                g2.setColor(TEXT_MUTED);
            }

            // Numărul (doar dacă nu e completed)
            if (!completed) {
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String num = String.valueOf(index + 1);
                int numX = circleX + (circleSize - fm.stringWidth(num)) / 2;
                int numY = circleY + (circleSize + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(num, numX, numY);
            }

            // Textul pasului
            int textX = circleX + circleSize + 14;
            g2.setColor(active ? TEXT_PRIMARY : (hovering ? TEXT_PRIMARY : TEXT_SECONDARY));
            g2.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 14));
            FontMetrics fm = g2.getFontMetrics();
            int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(name, textX, textY);

            g2.dispose();
        }
    }
}
