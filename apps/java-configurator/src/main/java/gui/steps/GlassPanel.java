package gui.steps;

import db.GlassDAO;
import gui.preview.ShowerPreviewPanel;
import gui.navigation.StepNavigator;
import model.glass.Glass;
import util.session.Session;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GlassPanel extends JPanel {

    private ShowerPreviewPanel previewPanel;

    public GlassPanel(StepNavigator navigator) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(42, 42, 42));

        JLabel title = new JLabel("Selectare Tip Sticlă", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        // Panel principal
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setBackground(new Color(42, 42, 42));

        // Previzualizare custom în partea stângă - acum folosim clasa separată
        previewPanel = new ShowerPreviewPanel();
        JPanel previewContainer = createPreviewContainer();
        mainContent.add(previewContainer, BorderLayout.CENTER);

        // Opțiuni în partea dreaptă
        JPanel optionsPanel = createOptionsPanel(navigator);
        mainContent.add(optionsPanel, BorderLayout.EAST);

        add(mainContent, BorderLayout.CENTER);
    }

    public BufferedImage getPreviewImage() {
        return previewPanel.getPreviewImage();
    }

    public BufferedImage getHighQualityPreviewImage() {
        return previewPanel.getHighQualityPreviewImage(800, 1000); // Dimensiuni mai mari pentru calitate
    }

    private JPanel createPreviewContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(42, 42, 42));

        JLabel previewTitle = new JLabel("Previzualizare Cabina", SwingConstants.CENTER);
        previewTitle.setForeground(Color.WHITE);
        previewTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        container.add(previewTitle, BorderLayout.NORTH);

        JPanel canvasPanel = new JPanel(new BorderLayout());
        canvasPanel.setBackground(new Color(42, 42, 42));
        canvasPanel.setBorder(BorderFactory.createLineBorder(new Color(85, 85, 85), 2));
        canvasPanel.add(previewPanel, BorderLayout.CENTER);

        container.add(canvasPanel, BorderLayout.CENTER);
        container.setPreferredSize(new Dimension(450, 550));

        return container;
    }

    private JPanel createOptionsPanel(StepNavigator navigator) {
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBackground(new Color(42, 42, 42));
        optionsPanel.setPreferredSize(new Dimension(300, 550));

        GlassDAO dao = new GlassDAO();
        List<Glass> allGlasses = dao.getAllGlasses();

        Set<String> sticlaTipSet = allGlasses.stream()
                .map(Glass::getTipSticla)
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> grosimeSet = allGlasses.stream()
                .map(Glass::getGrosimeMm)
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        String[] sticlaArray = sticlaTipSet.toArray(new String[0]);
        String[] grosimeArray = grosimeSet.stream()
                .map(Integer::parseInt)
                .sorted()
                .map(String::valueOf)
                .toArray(String[]::new);

        // Componente
        JLabel nameLabel = new JLabel("Nume sticlă:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> nameCombo = new JComboBox<>(sticlaArray);
        styleComboBox(nameCombo);

        JLabel thicknessLabel = new JLabel("Grosime (mm):");
        thicknessLabel.setForeground(Color.WHITE);
        thicknessLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> thicknessCombo = new JComboBox<>(grosimeArray);
        styleComboBox(thicknessCombo);

        JLabel typeLabel = new JLabel("Tip sticlă:");
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Securizata calita", "Simplă debitata"});
        styleComboBox(typeCombo);

        JLabel drillingLabel = new JLabel("Găurire extra:");
        drillingLabel.setForeground(Color.WHITE);
        drillingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> drillingCombo = new JComboBox<>(new String[]{"Fara", "4-20", "21-30", "31-60"});
        styleComboBox(drillingCombo);

        JLabel numarGauririLabel = new JLabel("Număr găuriri extra:");
        numarGauririLabel.setForeground(Color.WHITE);
        numarGauririLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> numarGauririCombo = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        styleComboBox(numarGauririCombo);

        JLabel numarDecupajeLabel = new JLabel("Număr decupaje extra:");
        numarDecupajeLabel.setForeground(Color.WHITE);
        numarDecupajeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> numarDecupajeCombo = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5"});
        styleComboBox(numarDecupajeCombo);

        // Listener pentru actualizare în timp real
        ItemListener previewUpdater = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateSessionAndPreview(nameCombo, thicknessCombo, typeCombo, drillingCombo,
                        numarGauririCombo, numarDecupajeCombo);
                previewPanel.repaint();
            }
        };

        nameCombo.addItemListener(previewUpdater);
        thicknessCombo.addItemListener(previewUpdater);
        typeCombo.addItemListener(previewUpdater);
        drillingCombo.addItemListener(previewUpdater);
        numarGauririCombo.addItemListener(previewUpdater);
        numarDecupajeCombo.addItemListener(previewUpdater);

        // Layout pentru opțiuni
        JPanel optionsGrid = new JPanel(new GridLayout(6, 1, 10, 15));
        optionsGrid.setOpaque(false);

        optionsGrid.add(createOptionPanel(nameLabel, nameCombo));
        optionsGrid.add(createOptionPanel(thicknessLabel, thicknessCombo));
        optionsGrid.add(createOptionPanel(typeLabel, typeCombo));
        optionsGrid.add(createOptionPanel(drillingLabel, drillingCombo));
        optionsGrid.add(createOptionPanel(numarGauririLabel, numarGauririCombo));
        optionsGrid.add(createOptionPanel(numarDecupajeLabel, numarDecupajeCombo));

        // Buton următorul pas
        JButton nextBtn = new JButton("Următorul pas →");
        nextBtn.setPreferredSize(new Dimension(250, 45));
        nextBtn.setBackground(new Color(255, 120, 160));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextBtn.setFocusPainted(false);
        nextBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        nextBtn.addActionListener(e -> {
            updateSessionAndPreview(nameCombo, thicknessCombo, typeCombo, drillingCombo,
                    numarGauririCombo, numarDecupajeCombo);
            Session.save();
            navigator.goToStep(6);
        });

        // Container pentru opțiuni
        JPanel optionsContainer = new JPanel(new BorderLayout(30, 30));
        optionsContainer.setBackground(new Color(42, 42, 42));
        optionsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        optionsContainer.add(optionsGrid, BorderLayout.CENTER);
        optionsContainer.add(nextBtn, BorderLayout.SOUTH);

        optionsPanel.add(optionsContainer, new GridBagConstraints() {{
            gridx = 0; gridy = 0;
            weightx = 1.0; weighty = 1.0;
            anchor = GridBagConstraints.CENTER;
        }});

        // Selecții implicite
        if (sticlaArray.length > 0) nameCombo.setSelectedIndex(0);
        if (grosimeArray.length > 0) thicknessCombo.setSelectedIndex(0);
        typeCombo.setSelectedIndex(0);
        drillingCombo.setSelectedIndex(0);
        numarGauririCombo.setSelectedIndex(0);
        numarDecupajeCombo.setSelectedIndex(0);

        // Actualizare inițială
        updateSessionAndPreview(nameCombo, thicknessCombo, typeCombo, drillingCombo,
                numarGauririCombo, numarDecupajeCombo);

        return optionsPanel;
    }

    private JPanel createOptionPanel(JLabel label, JComboBox<String> combo) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setOpaque(false);
        panel.add(label, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);
        return panel;
    }

    private void updateSessionAndPreview(JComboBox<String> nameCombo, JComboBox<String> thicknessCombo,
                                         JComboBox<String> typeCombo, JComboBox<String> drillingCombo,
                                         JComboBox<String> numarGauririCombo, JComboBox<String> numarDecupajeCombo) {
        Session.sticlaNume = (String) nameCombo.getSelectedItem();
        Session.sticlaGrosime = (String) thicknessCombo.getSelectedItem();
        Session.sticlaTip = (String) typeCombo.getSelectedItem();
        Session.sticlaGaurire = (String) drillingCombo.getSelectedItem();
        Session.sticlaNumarGauririExtra = (String) numarGauririCombo.getSelectedItem();
        Session.sticlaNumarDecupajeExtra = (String) numarDecupajeCombo.getSelectedItem();
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setBackground(new Color(60, 60, 60));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBackground(isSelected ? new Color(255, 87, 34) : new Color(60, 60, 60));
                    label.setForeground(Color.WHITE);
                    label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                return c;
            }
        });
    }
}