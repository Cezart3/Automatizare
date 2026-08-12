package gui.admin;

import db.ProductDAO;
import model.product.Product;
import model.glass.Glass;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModifyElementsPanel extends JPanel {

    private final ProductDAO dao = new ProductDAO();

    private JComboBox<String> tableComboBox;
    private JComboBox<Object> itemComboBox;
    private JComboBox<String> materialComboBox;
    private JComboBox<String> finishComboBox;
    private JTextField priceField;
    private JButton saveButton;

    // Câmpuri noi pentru sticlă
    private JPanel glassPanel;
    private JPanel hardwarePanel; // Referință directă pentru panoul de feronerie
    private JTextField simplaDebitataField;
    private JTextField securizataCalitaField;
    private JTextField manoperaSlefuireField;
    private JTextField manoperaGaurire4_20Field;
    private JTextField manoperaGaurire21_30Field;
    private JTextField manoperaGaurire31_60CNCField;
    private JTextField adaosFormaProcField;
    private JTextField adaosSablonProcField;
    private JTextField manoperaDecupareFeronField;

    private final Color bgColor = UIManager.getColor("Panel.background");
    private final Color panelColor = new Color(60, 60, 70);
    private final Color btnColor = new Color(255, 87, 34);
    private final Color textColor = Color.WHITE;
    private final Color borderColor = new Color(80, 80, 90);
    private final Font font = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 16);

    private static final Map<String, Integer> MATERIAL_TO_ID = Map.of(
            "Zinc", 1,
            "Aluminiu", 2,
            "Otel Inoxidabil#304", 3,
            "OtelInox +sticla", 4,
            "Nu are material", 5,
            "Nu are materiall", 10
    );

    private static final Map<String, Integer> FINISH_TO_ID = Map.of(
            "Rose-Gold", 6,
            "Alb", 4,
            "Gold", 5,
            "Satin", 1,
            "Negru", 3,
            "Lucios", 2,
            "Nu are finisaj", 10
    );

    public ModifyElementsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(bgColor);

        // Titlu
        JLabel titleLabel = new JLabel("Modificare Elemente Existente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panou principal care va fi scrollabil
        JPanel mainPanel = createMainPanel();

        // Creăm JScrollPane pentru panoul principal
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(panelColor);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Personalizăm scroll bar-ul
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16); // Viteza de scroll
        verticalScrollBar.setBackground(new Color(45, 45, 55));
        verticalScrollBar.setForeground(btnColor);

        add(scrollPane, BorderLayout.CENTER);

        // Listeners
        tableComboBox.addActionListener(e -> loadItems());
        itemComboBox.addActionListener(e -> updateUIForSelectedItem());
        materialComboBox.addActionListener(e -> loadPrice());
        finishComboBox.addActionListener(e -> loadPrice());
        saveButton.addActionListener(e -> saveData());

        // Inițializare
        if (tableComboBox.getItemCount() > 0) {
            tableComboBox.setSelectedIndex(0);
            loadItems();
        }
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(panelColor);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Tabel
        gbc.gridx = 0; gbc.gridy = 0;
        addStyledLabel(mainPanel, "Tabel:", gbc);

        // Filtrăm tabelele pentru a exclude admin_users și alte tabele necorespunzătoare
        List<String> allTables = dao.getAllTableNames();
        List<String> filteredTables = allTables.stream()
                .filter(table -> !table.equalsIgnoreCase("admin_users") &&
                        !table.equalsIgnoreCase("materials") &&
                        !table.equalsIgnoreCase("products") &&
                        !table.equalsIgnoreCase("permissions") &&
                        !table.equalsIgnoreCase("users") &&
                        !table.equalsIgnoreCase("user_roles") &&
                        !table.equalsIgnoreCase("finishes"))
                .collect(Collectors.toList());

        tableComboBox = createStyledComboBox(filteredTables.toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = 0;
        mainPanel.add(tableComboBox, gbc);

        // Produs/Element
        gbc.gridx = 0; gbc.gridy = 1;
        addStyledLabel(mainPanel, "Element:", gbc);
        itemComboBox = createStyledObjectComboBox(new Object[0]);
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(itemComboBox, gbc);

        // Panou pentru feronerie (Material, Finisaj, Preț)
        hardwarePanel = createHardwarePanel();
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(hardwarePanel, gbc);

        // Panou pentru sticlă
        glassPanel = createGlassPanel();
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(glassPanel, gbc);

        // Buton Salvează
        saveButton = createStyledButton("Salvează Modificările");
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(saveButton, gbc);

        return mainPanel;
    }

    private JPanel createHardwarePanel() {
        JPanel hardwarePanel = new JPanel(new GridBagLayout());
        hardwarePanel.setBackground(panelColor);
        hardwarePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), "Detalii Feronerie"
        ));

        GridBagConstraints hgbc = new GridBagConstraints();
        hgbc.insets = new Insets(8, 8, 8, 8);
        hgbc.fill = GridBagConstraints.HORIZONTAL;
        hgbc.weightx = 1.0;

        // Material
        hgbc.gridx = 0; hgbc.gridy = 0;
        addStyledLabel(hardwarePanel, "Material:", hgbc);
        materialComboBox = createStyledComboBox(new String[0]);
        hgbc.gridx = 1; hgbc.gridy = 0;
        hardwarePanel.add(materialComboBox, hgbc);

        // Finisaj
        hgbc.gridx = 0; hgbc.gridy = 1;
        addStyledLabel(hardwarePanel, "Finisaj:", hgbc);
        finishComboBox = createStyledComboBox(new String[0]);
        hgbc.gridx = 1; hgbc.gridy = 1;
        hardwarePanel.add(finishComboBox, hgbc);

        // Preț
        hgbc.gridx = 0; hgbc.gridy = 2;
        addStyledLabel(hardwarePanel, "Preț:", hgbc);
        priceField = createStyledTextField();
        hgbc.gridx = 1; hgbc.gridy = 2;
        hardwarePanel.add(priceField, hgbc);

        return hardwarePanel;
    }

    private JPanel createGlassPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), "Detalii Sticlă"
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Simplă debită
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Simplă debită:", gbc);
        simplaDebitataField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(simplaDebitataField, gbc);

        // Securizată călită
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Securizată călită:", gbc);
        securizataCalitaField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(securizataCalitaField, gbc);

        // Manoperă șlefuire
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Manoperă șlefuire:", gbc);
        manoperaSlefuireField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(manoperaSlefuireField, gbc);

        // Manoperă găurire 4-20mm
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Manoperă găurire 4-20mm:", gbc);
        manoperaGaurire4_20Field = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(manoperaGaurire4_20Field, gbc);

        // Manoperă găurire 21-30mm
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Manoperă găurire 21-30mm:", gbc);
        manoperaGaurire21_30Field = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(manoperaGaurire21_30Field, gbc);

        // Manoperă găurire 31-60mm CNC
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Manoperă găurire 31-60mm CNC:", gbc);
        manoperaGaurire31_60CNCField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(manoperaGaurire31_60CNCField, gbc);

        // Adaos formă proc
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Adaos formă (%):", gbc);
        adaosFormaProcField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(adaosFormaProcField, gbc);

        // Adaos șablon proc
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Adaos șablon (%):", gbc);
        adaosSablonProcField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(adaosSablonProcField, gbc);

        // Manoperă decupare feron
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Manoperă decupare feron:", gbc);
        manoperaDecupareFeronField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(manoperaDecupareFeronField, gbc);

        return panel;
    }

    private boolean isGlassTable() {
        String selectedTable = (String) tableComboBox.getSelectedItem();
        return selectedTable != null && selectedTable.equalsIgnoreCase("sticle");
    }

    private void loadItems() {
        itemComboBox.removeAllItems();
        String tabel = (String) tableComboBox.getSelectedItem();
        if (tabel == null) return;

        if (isGlassTable()) {
            // Încărcăm sticlele
            List<Glass> glasses = dao.getAllGlass();
            for (Glass glass : glasses) {
                itemComboBox.addItem(glass);
            }
        } else {
            // Încărcăm produsele de feronerie
            List<Product> products = dao.getProductsFromTable(tabel);
            for (Product p : products) itemComboBox.addItem(p);
        }
        updateUIForSelectedItem();
    }

    private void updateUIForSelectedItem() {
        boolean isGlass = isGlassTable();

        // Ascundem/afisăm panourile corespunzătoare folosind referințele directe
        hardwarePanel.setVisible(!isGlass);
        glassPanel.setVisible(isGlass);

        if (isGlass) {
            loadGlassData();
        } else {
            loadMaterialsAndFinishes();
        }
    }

    private void loadGlassData() {
        Glass selectedGlass = (Glass) itemComboBox.getSelectedItem();
        if (selectedGlass == null) return;

        simplaDebitataField.setText(String.valueOf(selectedGlass.getSimplaDebitata()));
        securizataCalitaField.setText(String.valueOf(selectedGlass.getSecurizataCalita()));
        manoperaSlefuireField.setText(String.valueOf(selectedGlass.getManoperaSlefuire()));
        manoperaGaurire4_20Field.setText(String.valueOf(selectedGlass.getManoperaGaurire4_20()));
        manoperaGaurire21_30Field.setText(String.valueOf(selectedGlass.getManoperaGaurire21_30()));
        manoperaGaurire31_60CNCField.setText(String.valueOf(selectedGlass.getManoperaGaurire31_60_cnc()));
        adaosFormaProcField.setText(String.valueOf(selectedGlass.getAdaosFormaProc()));
        adaosSablonProcField.setText(String.valueOf(selectedGlass.getAdaosSablonProc()));
        manoperaDecupareFeronField.setText(String.valueOf(selectedGlass.getManoperaDecupeFeron()));
    }

    private void loadMaterialsAndFinishes() {
        Product selected = (Product) itemComboBox.getSelectedItem();
        String tabel = (String) tableComboBox.getSelectedItem();
        if (selected == null || tabel == null) return;

        materialComboBox.removeAllItems();
        finishComboBox.removeAllItems();

        List<String> materialIds = dao.getMaterials(tabel, selected.getCodProdus());
        List<String> finishIds = dao.getFinishes(tabel, selected.getCodProdus());

        for (String mId : materialIds) {
            for (Map.Entry<String, Integer> entry : MATERIAL_TO_ID.entrySet()) {
                if (entry.getValue() == Integer.parseInt(mId)) {
                    materialComboBox.addItem(entry.getKey());
                    break;
                }
            }
        }

        for (String fId : finishIds) {
            for (Map.Entry<String, Integer> entry : FINISH_TO_ID.entrySet()) {
                if (entry.getValue() == Integer.parseInt(fId)) {
                    finishComboBox.addItem(entry.getKey());
                    break;
                }
            }
        }

        loadPrice();
    }

    private void loadPrice() {
        if (isGlassTable()) {
            return; // Prețul pentru sticlă este gestionat separat
        }

        Product selected = (Product) itemComboBox.getSelectedItem();
        String tabel = (String) tableComboBox.getSelectedItem();
        String material = (String) materialComboBox.getSelectedItem();
        String finish = (String) finishComboBox.getSelectedItem();
        if (selected == null || tabel == null || material == null || finish == null) {
            priceField.setText("");
            return;
        }

        int materialId = MATERIAL_TO_ID.getOrDefault(material, 0);
        int finishId = FINISH_TO_ID.getOrDefault(finish, 0);

        double price = dao.getPrice(tabel, selected.getCodProdus(), materialId, finishId);
        priceField.setText(String.valueOf(price));
    }

    private void saveData() {
        if (isGlassTable()) {
            saveGlassData();
        } else {
            savePrice();
        }
    }

    private void saveGlassData() {
        Glass selectedGlass = (Glass) itemComboBox.getSelectedItem();
        if (selectedGlass == null) return;

        try {
            // Actualizăm obiectul glass cu noile valori
            selectedGlass.setSimplaDebitata(Double.parseDouble(simplaDebitataField.getText()));
            selectedGlass.setSecurizataCalita(Double.parseDouble(securizataCalitaField.getText()));
            selectedGlass.setManoperaSlefuire(Double.parseDouble(manoperaSlefuireField.getText()));
            selectedGlass.setManoperaGaurire4_20(Double.parseDouble(manoperaGaurire4_20Field.getText()));
            selectedGlass.setManoperaGaurire21_30(Double.parseDouble(manoperaGaurire21_30Field.getText()));
            selectedGlass.setManoperaGaurire31_60_cnc(Double.parseDouble(manoperaGaurire31_60CNCField.getText()));
            selectedGlass.setAdaosFormaProc(Double.parseDouble(adaosFormaProcField.getText()));
            selectedGlass.setAdaosSablonProc(Double.parseDouble(adaosSablonProcField.getText()));
            selectedGlass.setManoperaDecupeFeron(Double.parseDouble(manoperaDecupareFeronField.getText()));

             boolean success = dao.updateGlass(selectedGlass);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Datele sticlei au fost actualizate cu succes!",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Eroare la actualizarea datelor sticlei!",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Introduceți valori valide!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savePrice() {
        Product selected = (Product) itemComboBox.getSelectedItem();
        String tabel = (String) tableComboBox.getSelectedItem();
        String material = (String) materialComboBox.getSelectedItem();
        String finish = (String) finishComboBox.getSelectedItem();
        if (selected == null || tabel == null || material == null || finish == null) return;

        try {
            double newPrice = Double.parseDouble(priceField.getText());
            int materialId = MATERIAL_TO_ID.getOrDefault(material, 0);
            int finishId = FINISH_TO_ID.getOrDefault(finish, 0);

            boolean success = dao.updatePrice(tabel, selected.getCodProdus(), materialId, finishId, newPrice);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Prețul a fost actualizat cu succes!",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Eroare la actualizarea prețului!",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Introduceți un preț valid!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metode auxiliare pentru styling (rămân aceleași)
    private void addStyledLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(textColor);
        panel.add(label, gbc);
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(new Color(45, 45, 55));
        combo.setForeground(textColor);
        combo.setFont(font);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBackground(isSelected ? btnColor : new Color(45, 45, 55));
                    label.setForeground(isSelected ? Color.WHITE : textColor);
                }
                return c;
            }
        });
        return combo;
    }

    private JComboBox<Object> createStyledObjectComboBox(Object[] items) {
        JComboBox<Object> combo = new JComboBox<>(items);
        combo.setBackground(new Color(45, 45, 55));
        combo.setForeground(textColor);
        combo.setFont(font);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    if (value instanceof Product) {
                        Product product = (Product) value;
                        label.setText(product.getCodProdus() + " - " + product.getDenumire());
                    } else if (value instanceof Glass) {
                        Glass glass = (Glass) value;
                        label.setText(glass.getTipSticla() + " - " + glass.getGrosimeMm() + "mm");
                    }
                    label.setBackground(isSelected ? btnColor : new Color(45, 45, 55));
                    label.setForeground(isSelected ? Color.WHITE : textColor);
                }
                return c;
            }
        });
        return combo;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(45, 45, 55));
        field.setForeground(textColor);
        field.setFont(font);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setCaretColor(textColor);
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(btnColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 70, 30), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 110, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(btnColor);
            }
        });

        return button;
    }
}