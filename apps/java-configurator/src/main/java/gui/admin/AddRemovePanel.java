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

public class AddRemovePanel extends JPanel {

    private final ProductDAO dao = new ProductDAO();

    // ComboBox-uri separate pentru secțiunile de ștergere și adăugare
    private JComboBox<String> deleteTableComboBox;
    private JComboBox<String> addTableComboBox;

    private JTextField productCodeField;
    private JTextField productNameField;
    private JTextField productPriceField; // Câmp nou pentru preț
    private JComboBox<String> materialComboBox;
    private JComboBox<String> finishComboBox;
    private JButton addButton;
    private JButton deleteButton;
    private JComboBox<Object> deleteItemComboBox;
    private JComboBox<String> deleteMaterialComboBox;
    private JComboBox<String> deleteFinishComboBox;

    // Câmpuri noi pentru sticlă - ADĂUGARE
    private JPanel glassAddPanel;
    private JTextField tipSticlaField;
    private JTextField grosimeMmField;
    private JTextField simplaDebitataField;
    private JTextField securizataCalitaField;
    private JTextField manoperaSlefuireField;
    private JTextField manoperaGaurire4_20Field;
    private JTextField manoperaGaurire21_30Field;
    private JTextField manoperaGaurire31_60CNCField;
    private JTextField adaosFormaProcField;
    private JTextField adaosSablonProcField;
    private JTextField manoperaDecupareFeronField;

    // Panouri pentru ștergere
    private JPanel glassDeletePanel;
    private JPanel hardwareDeletePanel;
    private JPanel hardwareAddPanel;

    private final Color bgColor = UIManager.getColor("Panel.background");
    private final Color panelColor = new Color(60, 60, 70);
    private final Color btnColor = new Color(255, 87, 34);
    private final Color deleteBtnColor = new Color(220, 53, 69);
    private final Color textColor = Color.WHITE;
    private final Color borderColor = new Color(80, 80, 90);
    private final Font font = new Font("Segoe UI", Font.PLAIN, 14);

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

    public AddRemovePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(bgColor);

        // Titlu
        JLabel titleLabel = new JLabel("Adăugare / Ștergere Elemente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panou principal scrollable
        JPanel mainPanel = createMainPanel();

        // Creăm JScrollPane pentru panoul principal
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(panelColor);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Personalizăm scroll bar-ul
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBackground(new Color(45, 45, 55));
        verticalScrollBar.setForeground(btnColor);

        add(scrollPane, BorderLayout.CENTER);

        // Listeners SEPARATE pentru fiecare secțiune
        deleteTableComboBox.addActionListener(e -> loadDeleteItems());
        deleteItemComboBox.addActionListener(e -> updateDeleteUIForSelectedItem());
        addTableComboBox.addActionListener(e -> updateAddUIForSelectedTable());

        addButton.addActionListener(e -> addItem());
        deleteButton.addActionListener(e -> deleteItem());

        // Inițializare
        if (deleteTableComboBox.getItemCount() > 0) {
            deleteTableComboBox.setSelectedIndex(0);
            loadDeleteItems();
        }
        if (addTableComboBox.getItemCount() > 0) {
            addTableComboBox.setSelectedIndex(0);
            updateAddUIForSelectedTable();
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

        // === SECȚIUNEA DE ȘTERGERE ===
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel deleteSectionLabel = new JLabel("Ștergere Element", SwingConstants.LEFT);
        deleteSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        deleteSectionLabel.setForeground(textColor);
        mainPanel.add(deleteSectionLabel, gbc);

        // Tabel pentru ȘTERGERE
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        addStyledLabel(mainPanel, "Tabel:", gbc);

        List<String> filteredTables = getFilteredTables();
        deleteTableComboBox = createStyledComboBox(filteredTables.toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(deleteTableComboBox, gbc);

        // Element existent pentru ștergere
        gbc.gridx = 0; gbc.gridy = 2;
        addStyledLabel(mainPanel, "Element:", gbc);
        deleteItemComboBox = createStyledObjectComboBox(new Object[0]);
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(deleteItemComboBox, gbc);

        // Panou pentru ștergere feronerie
        hardwareDeletePanel = createHardwareDeletePanel();
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(hardwareDeletePanel, gbc);

        // Panou pentru ștergere sticlă
        glassDeletePanel = createGlassDeletePanel();
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(glassDeletePanel, gbc);

        // Buton Șterge
        deleteButton = createStyledDeleteButton("Șterge Element");
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(deleteButton, gbc);

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(borderColor);
        separator.setBackground(borderColor);
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(separator, gbc);

        // === SECȚIUNEA DE ADAUGARE ===
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 8, 8, 8);
        JLabel addSectionLabel = new JLabel("Adăugare Element Nou", SwingConstants.LEFT);
        addSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addSectionLabel.setForeground(textColor);
        mainPanel.add(addSectionLabel, gbc);

        // Tabel pentru ADAUGARE (SEPARAT)
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 1;
        addStyledLabel(mainPanel, "Tabel:", gbc);
        addTableComboBox = createStyledComboBox(filteredTables.toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = 8;
        mainPanel.add(addTableComboBox, gbc);

        // Panou pentru adăugare feronerie
        hardwareAddPanel = createHardwareAddPanel();
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        mainPanel.add(hardwareAddPanel, gbc);

        // Panou pentru adăugare sticlă
        glassAddPanel = createGlassAddPanel();
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        mainPanel.add(glassAddPanel, gbc);

        // Buton Adaugă
        addButton = createStyledButton("Adaugă Element");
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(addButton, gbc);

        return mainPanel;
    }

    private List<String> getFilteredTables() {
        List<String> allTables = dao.getAllTableNames();
        return allTables.stream()
                .filter(table -> !table.equalsIgnoreCase("admin_users") &&
                        !table.equalsIgnoreCase("materials") &&
                        !table.equalsIgnoreCase("finishes"))
                .collect(Collectors.toList());
    }

    private JPanel createHardwareDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), "Detalii Feronerie pentru Ștergere"
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Material pentru ștergere
        gbc.gridx = 0; gbc.gridy = 0;
        addStyledLabel(panel, "Material:", gbc);
        deleteMaterialComboBox = createStyledComboBox(new String[0]);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(deleteMaterialComboBox, gbc);

        // Finisaj pentru ștergere
        gbc.gridx = 0; gbc.gridy = 1;
        addStyledLabel(panel, "Finisaj:", gbc);
        deleteFinishComboBox = createStyledComboBox(new String[0]);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(deleteFinishComboBox, gbc);

        return panel;
    }

    private JPanel createGlassDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), "Detalii Sticlă pentru Ștergere"
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Mesaj informativ pentru sticlă
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("Sticla va fi ștearsă complet din sistem", SwingConstants.CENTER);
        infoLabel.setFont(font);
        infoLabel.setForeground(textColor);
        panel.add(infoLabel, gbc);

        return panel;
    }

    private JPanel createHardwareAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), "Detalii Feronerie pentru Adăugare"
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Cod produs nou
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Cod produs:", gbc);
        productCodeField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(productCodeField, gbc);

        // Nume produs nou
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Nume produs:", gbc);
        productNameField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(productNameField, gbc);

        // Preț produs nou
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Preț:", gbc);
        productPriceField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(productPriceField, gbc);

        // Material pentru adăugare
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Material:", gbc);
        materialComboBox = createStyledComboBox(MATERIAL_TO_ID.keySet().toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(materialComboBox, gbc);

        // Finisaj pentru adăugare
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Finisaj:", gbc);
        finishComboBox = createStyledComboBox(FINISH_TO_ID.keySet().toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(finishComboBox, gbc);

        return panel;
    }

    private JPanel createGlassAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor), "Detalii Sticlă pentru Adăugare"
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Tip sticlă
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Tip sticlă:", gbc);
        tipSticlaField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(tipSticlaField, gbc);

        // Grosime mm
        gbc.gridx = 0; gbc.gridy = row;
        addStyledLabel(panel, "Grosime (mm):", gbc);
        grosimeMmField = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(grosimeMmField, gbc);

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

    private boolean isGlassTable(JComboBox<String> tableComboBox) {
        String selectedTable = (String) tableComboBox.getSelectedItem();
        return selectedTable != null && selectedTable.equalsIgnoreCase("sticle");
    }

    private void loadDeleteItems() {
        deleteItemComboBox.removeAllItems();
        String tabel = (String) deleteTableComboBox.getSelectedItem();
        if (tabel == null) return;

        if (isGlassTable(deleteTableComboBox)) {
            // Încărcăm sticlele
            List<Glass> glasses = dao.getAllGlass();
            for (Glass glass : glasses) {
                deleteItemComboBox.addItem(glass);
            }
        } else {
            // Încărcăm produsele de feronerie
            List<Product> products = dao.getProductsFromTable(tabel);
            for (Product p : products) deleteItemComboBox.addItem(p);
        }
        updateDeleteUIForSelectedItem();
    }

    private void updateDeleteUIForSelectedItem() {
        boolean isGlass = isGlassTable(deleteTableComboBox);

        // Ascundem/afisăm panourile corespunzătoare pentru SECȚIUNEA DE ȘTERGERE
        hardwareDeletePanel.setVisible(!isGlass);
        glassDeletePanel.setVisible(isGlass);

        if (!isGlass) {
            loadMaterialsAndFinishesForDelete();
        }
    }

    private void updateAddUIForSelectedTable() {
        boolean isGlass = isGlassTable(addTableComboBox);

        // Ascundem/afisăm panourile corespunzătoare pentru SECȚIUNEA DE ADAUGARE
        hardwareAddPanel.setVisible(!isGlass);
        glassAddPanel.setVisible(isGlass);
    }

    private void loadMaterialsAndFinishesForDelete() {
        Object selected = deleteItemComboBox.getSelectedItem();
        String tabel = (String) deleteTableComboBox.getSelectedItem();

        if (!(selected instanceof Product) || tabel == null) {
            return;
        }

        Product product = (Product) selected;
        deleteMaterialComboBox.removeAllItems();
        deleteFinishComboBox.removeAllItems();

        System.out.println("Se încarcă materialele și finisajele pentru produsul: " + product.getCodProdus() + " din tabelul: " + tabel);

        // Obținem ID-urile disponibile pentru produs
        List<String> materialIds = dao.getMaterials(tabel, product.getCodProdus());
        List<String> finishIds = dao.getFinishes(tabel, product.getCodProdus());

        System.out.println("Material IDs found: " + materialIds);
        System.out.println("Finish IDs found: " + finishIds);

        // Pentru fiecare ID, adăugăm denumirea corespunzătoare din map
        for (String mId : materialIds) {
            for (Map.Entry<String, Integer> entry : MATERIAL_TO_ID.entrySet()) {
                if (entry.getValue() == Integer.parseInt(mId)) {
                    deleteMaterialComboBox.addItem(entry.getKey());
                    break;
                }
            }
        }

        for (String fId : finishIds) {
            for (Map.Entry<String, Integer> entry : FINISH_TO_ID.entrySet()) {
                if (entry.getValue() == Integer.parseInt(fId)) {
                    deleteFinishComboBox.addItem(entry.getKey());
                    break;
                }
            }
        }

        System.out.println("Materiale în combobox: " + deleteMaterialComboBox.getItemCount());
        System.out.println("Finisaje în combobox: " + deleteFinishComboBox.getItemCount());
    }

    private void addItem() {
        if (isGlassTable(addTableComboBox)) {
            addGlass();
        } else {
            addProduct();
        }
    }

    private void refreshDeleteListForTable(String tableName) {
        // Verifică dacă tabelul selectat în secțiunea de ștergere este același cu cel în care am adăugat
        String currentDeleteTable = (String) deleteTableComboBox.getSelectedItem();

        if (currentDeleteTable != null && currentDeleteTable.equals(tableName)) {
            // Dacă da, reîncarcă lista pentru acest tabel
            loadDeleteItems();
        } else {
            // Dacă nu, oferă utilizatorului opțiunea de a schimba tabelul
            int option = JOptionPane.showConfirmDialog(this,
                    "Produsul a fost adăugat în tabelul '" + tableName + "'. Doriți să schimbați tabelul în secțiunea de ștergere la '" + tableName + "' pentru a vedea noul produs?",
                    "Refresh listă",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // Setează tabelul în secțiunea de ștergere și reîncarcă
                deleteTableComboBox.setSelectedItem(tableName);
                loadDeleteItems();
            }
        }
    }

    private void addProduct() {
        String cod = productCodeField.getText().trim();
        String nume = productNameField.getText().trim();
        String priceText = productPriceField.getText().trim();
        String material = (String) materialComboBox.getSelectedItem();
        String finish = (String) finishComboBox.getSelectedItem();
        String tabel = (String) addTableComboBox.getSelectedItem();

        if (cod.isEmpty() || nume.isEmpty() || priceText.isEmpty() || tabel == null || material == null || finish == null) {
            JOptionPane.showMessageDialog(this,
                    "Completați toate câmpurile!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Prețul trebuie să fie un număr valid!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obținem ID-urile pentru material și finisaj
        int materialId = MATERIAL_TO_ID.getOrDefault(material, 0);
        int finishId = FINISH_TO_ID.getOrDefault(finish, 0);

        if (materialId == 0 || finishId == 0) {
            JOptionPane.showMessageDialog(this,
                    "Material sau finisaj invalid!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = dao.insertProduct(tabel, cod, nume, materialId, finishId, price);

        if (success) {
            // Resetează câmpurile
            productCodeField.setText("");
            productNameField.setText("");
            productPriceField.setText("");

            // REFRESH DINAMIC - reîncarcă lista de ștergere
            refreshDeleteListForTable(tabel);

            JOptionPane.showMessageDialog(this,
                    "Produs adăugat cu succes în tabelul " + tabel + "!",
                    "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Eroare la adăugarea produsului!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addGlass() {
        String tipSticla = tipSticlaField.getText().trim();
        String grosimeMm = grosimeMmField.getText().trim();

        if (tipSticla.isEmpty() || grosimeMm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Completați tipul sticlei și grosimea!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Obținem următorul ID disponibil
            int nextId = getNextGlassId();

            // Creăm un obiect Glass cu valorile din câmpuri
            Glass newGlass = new Glass(
                    nextId,
                    tipSticla,
                    grosimeMm,
                    Double.parseDouble(simplaDebitataField.getText().isEmpty() ? "0" : simplaDebitataField.getText()),
                    Double.parseDouble(securizataCalitaField.getText().isEmpty() ? "0" : securizataCalitaField.getText()),
                    Double.parseDouble(manoperaSlefuireField.getText().isEmpty() ? "0" : manoperaSlefuireField.getText()),
                    Double.parseDouble(manoperaGaurire4_20Field.getText().isEmpty() ? "0" : manoperaGaurire4_20Field.getText()),
                    Double.parseDouble(manoperaGaurire21_30Field.getText().isEmpty() ? "0" : manoperaGaurire21_30Field.getText()),
                    Double.parseDouble(manoperaGaurire31_60CNCField.getText().isEmpty() ? "0" : manoperaGaurire31_60CNCField.getText()),
                    Double.parseDouble(adaosFormaProcField.getText().isEmpty() ? "0" : adaosFormaProcField.getText()),
                    Double.parseDouble(adaosSablonProcField.getText().isEmpty() ? "0" : adaosSablonProcField.getText()),
                    Double.parseDouble(manoperaDecupareFeronField.getText().isEmpty() ? "0" : manoperaDecupareFeronField.getText())
            );

            boolean success = insertGlassInDatabase(newGlass);

            if (success) {
                // Resetează toate câmpurile
                resetGlassFields();

                // REFRESH DINAMIC - reîncarcă lista de ștergere pentru sticle
                refreshDeleteListForTable("sticle");

                JOptionPane.showMessageDialog(this,
                        "Sticlă adăugată cu succes cu ID-ul: " + nextId + "!",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Eroare la adăugarea sticlei!",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Introduceți valori numerice valide în toate câmpurile!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodă pentru a obține următorul ID disponibil pentru sticlă
    private int getNextGlassId() {
        List<Glass> glasses = dao.getAllGlass();
        int maxId = 0;
        for (Glass glass : glasses) {
            if (glass.getId() > maxId) {
                maxId = glass.getId();
            }
        }
        return maxId + 1;
    }

    // Metodă temporară pentru inserarea sticlei - trebuie implementată în DAO
    private boolean insertGlassInDatabase(Glass glass) {
        // Implementare temporară - în practică, această metodă ar trebui să fie în ProductDAO
        try {
            // SQL pentru inserarea unei sticle noi
            String sql = "INSERT INTO sticle (id, tip_sticla, grosime_mm, simpla_debitata, securizata_calita, " +
                    "manopera_slefuire, manopera_gaurire_4_20, manopera_gaurire_21_30, manopera_gaurire_31_60_cnc, " +
                    "adaos_forma_proc, adaos_sablon_proc, manopera_decupe_feron) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Aici ar trebui să folosim o conexiune la baza de date
            // Connection conn = DatabaseManager.getConnection();
            // PreparedStatement stmt = conn.prepareStatement(sql);
            // ... execută inserarea

            System.out.println("Sticlă inserată: " + glass.getTipSticla() + " - " + glass.getGrosimeMm() + "mm, ID: " + glass.getId());
            return true; // Temporar - returnăm true pentru testare
        } catch (Exception e) {
            System.err.println("Eroare la inserarea sticlei: " + e.getMessage());
            return false;
        }
    }

    private void resetGlassFields() {
        tipSticlaField.setText("");
        grosimeMmField.setText("");
        simplaDebitataField.setText("");
        securizataCalitaField.setText("");
        manoperaSlefuireField.setText("");
        manoperaGaurire4_20Field.setText("");
        manoperaGaurire21_30Field.setText("");
        manoperaGaurire31_60CNCField.setText("");
        adaosFormaProcField.setText("");
        adaosSablonProcField.setText("");
        manoperaDecupareFeronField.setText("");
    }

    private void deleteItem() {
        Object selected = deleteItemComboBox.getSelectedItem();
        String tabel = (String) deleteTableComboBox.getSelectedItem();

        if (selected == null || tabel == null) {
            JOptionPane.showMessageDialog(this,
                    "Selectați un element pentru ștergere!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isGlassTable(deleteTableComboBox)) {
            deleteGlass((Glass) selected);
        } else {
            deleteProduct((Product) selected);
        }
    }

    private void deleteProduct(Product selected) {
        String material = (String) deleteMaterialComboBox.getSelectedItem();
        String finish = (String) deleteFinishComboBox.getSelectedItem();
        String tabel = (String) deleteTableComboBox.getSelectedItem();

        if (material == null || finish == null) {
            JOptionPane.showMessageDialog(this,
                    "Selectați un material și finisaj pentru ștergere!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (deleteMaterialComboBox.getItemCount() == 0 || deleteFinishComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Nu există materiale sau finisaje disponibile pentru acest produs!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sigur doriți să ștergeți produsul: " + selected.getCodProdus() +
                        " cu material: " + material + " și finisaj: " + finish + "?",
                "Confirmare ștergere",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int materialId = MATERIAL_TO_ID.getOrDefault(material, 0);
            int finishId = FINISH_TO_ID.getOrDefault(finish, 0);

            boolean success = dao.deleteProduct(tabel, selected.getCodProdus(), materialId, finishId);

            if (success) {
                // AUTO-REFRESH după ștergere
                loadDeleteItems();

                JOptionPane.showMessageDialog(this,
                        "Produs șters cu succes!",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Eroare la ștergerea produsului!",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteGlass(Glass selected) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Sigur doriți să ștergeți sticla: " + selected.getTipSticla() +
                        " - " + selected.getGrosimeMm() + "mm?",
                "Confirmare ștergere",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = deleteGlassFromDatabase(selected.getId());

            if (success) {
                // AUTO-REFRESH după ștergere
                loadDeleteItems();

                JOptionPane.showMessageDialog(this,
                        "Sticlă ștearsă cu succes!",
                        "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Eroare la ștergerea sticlei!",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Metodă temporară pentru ștergerea sticlei - trebuie implementată în DAO
    private boolean deleteGlassFromDatabase(int glassId) {
        // Implementare temporară
        try {
            // SQL pentru ștergerea sticlei
            String sql = "DELETE FROM sticle WHERE id = ?";

            // Aici ar trebui să folosim o conexiune la baza de date
            // Connection conn = DatabaseManager.getConnection();
            // PreparedStatement stmt = conn.prepareStatement(sql);
            // stmt.setInt(1, glassId);
            // ... execută ștergerea

            System.out.println("Sticlă ștearsă cu ID-ul: " + glassId);
            return true; // Temporar - returnăm true pentru testare
        } catch (Exception e) {
            System.err.println("Eroare la ștergerea sticlei: " + e.getMessage());
            return false;
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

    private JButton createStyledDeleteButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(deleteBtnColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 35, 35), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(225, 75, 75));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(deleteBtnColor);
            }
        });

        return button;
    }
}