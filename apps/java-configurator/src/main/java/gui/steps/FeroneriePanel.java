package gui.steps;

import db.ProductDAO;
import gui.navigation.StepNavigator;
import model.product.Product;
import model.cabin.CabinTypeInfo;
import model.cabin.CabinTypes;
import util.session.Session;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class FeroneriePanel extends JPanel {

    private final String[] tables = {
            "balamale",
            "manere_buton",
            "manere_diverse",
            "profile",
            "garnituri",
            "profile_rigidizare_si_conectori"
    };
    private final Map<String, List<JCheckBox>> categoryCheckBoxes = new HashMap<>();
    private final Map<String, JLabel> summaryLabels = new HashMap<>();
    private final JLabel previewLabel = new JLabel();
    private final StepNavigator navigator;
    private final JTextArea recipeInfoTextArea = new JTextArea();
    private final Map<String, JPanel> categoryContentPanels = new HashMap<>();
    private String openCategory = null; // ține minte ce categorie e deschisă
    // Culori constante
    private static final Color BACKGROUND_COLOR = new Color(42, 42, 42);
    private static final Color PANEL_BACKGROUND = new Color(60, 60, 60);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
    private static final Color ACCENT_COLOR    = new Color(255, 120, 160);
    // roz-coral dezaturat (înlocuiește portocaliul agresiv)
    private static final Color HIGHLIGHT_COLOR = new Color(100, 180, 255);  // albastru foarte soft, modern
    public FeroneriePanel(StepNavigator navigator) {
        this.navigator = navigator;
        initializePanel();
        setupUIComponents();
        testResourceLoading();
        System.out.println("FeroneriePanel initialized. Waiting for activation...");
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
    }

    private void setupUIComponents() {
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        updateRecipeInfo();
    }

    private JPanel createTitlePanel() {
        JLabel title = new JLabel("Selectare Feronerie", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_COLOR);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.add(title, BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BACKGROUND_COLOR);

        center.add(createWestPanel(), BorderLayout.WEST);
        center.add(createPreviewPanel(), BorderLayout.CENTER);
        center.add(createEastPanel(), BorderLayout.EAST);

        return center;
    }

    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(BACKGROUND_COLOR);

        westPanel.add(createSummaryPanel(), BorderLayout.CENTER);
        westPanel.add(createRecipePanel(), BorderLayout.SOUTH);

        return westPanel;
    }

    private JPanel createSummaryPanel() {
        JPanel sumContainer = new JPanel(new GridBagLayout());
        sumContainer.setBackground(BACKGROUND_COLOR);
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setOpaque(false);

        for (String key : tables) {
            JLabel label = createSummaryLabel(key);
            summaryLabels.put(key, label);
            summaryPanel.add(label);
            summaryPanel.add(Box.createVerticalStrut(5));
        }
        sumContainer.add(summaryPanel);
        return sumContainer;
    }

    private JLabel createSummaryLabel(String key) {
        JLabel label = new JLabel(formatLabel(key) + ": –");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(LIGHT_GRAY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onSummaryLabelClicked(key, label);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label.setForeground(ACCENT_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateSummaryLabelColor(key, label);
            }
        });

        return label;
    }

    private JPanel createRecipePanel() {
        JPanel recipePanel = new JPanel(new BorderLayout());
        recipePanel.setBackground(new Color(50, 50, 50));
        recipePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Rețetă Cabina Selectată",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 12),
                TEXT_COLOR
        ));
        recipePanel.setPreferredSize(new Dimension(250, 220));

        // Configurare JTextArea pentru scroll
        recipeInfoTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        recipeInfoTextArea.setForeground(LIGHT_GRAY);
        recipeInfoTextArea.setBackground(new Color(50, 50, 50));
        recipeInfoTextArea.setEditable(false);
        recipeInfoTextArea.setLineWrap(true);
        recipeInfoTextArea.setWrapStyleWord(true);
        recipeInfoTextArea.setText("Selectați un tip de cabina pentru a vedea rețeta");

        // Adăugare scroll pane
        JScrollPane scrollPane = new JScrollPane(recipeInfoTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton modifyRecipeButton = createModifyRecipeButton();

        recipePanel.add(scrollPane, BorderLayout.CENTER);
        recipePanel.add(modifyRecipeButton, BorderLayout.SOUTH);

        return recipePanel;
    }

    private JButton createModifyRecipeButton() {
        JButton button = new JButton("Modifică Cantități");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(TEXT_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 25));
        button.addActionListener(e -> showRecipeModificationDialog());
        return button;
    }

    private JPanel createPreviewPanel() {
        JPanel previewPanel = new JPanel(new GridBagLayout());
        previewPanel.setBackground(BACKGROUND_COLOR);
        previewLabel.setPreferredSize(new Dimension(250, 250));
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setVerticalAlignment(SwingConstants.CENTER);
        previewPanel.add(previewLabel);
        return previewPanel;
    }

    private JScrollPane createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBackground(BACKGROUND_COLOR);

        ProductDAO dao = new ProductDAO();
        for (String table : tables) {
            JPanel categoryPanel = createCategoryPanel(table, dao);
            eastPanel.add(categoryPanel);
            eastPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(eastPanel);
        scroll.setPreferredSize(new Dimension(350, 0));
        scroll.getViewport().setBackground(BACKGROUND_COLOR);
        return scroll;
    }

    private JPanel createCategoryPanel(String table, ProductDAO dao) {
        JPanel catPanel = new JPanel();
        catPanel.setLayout(new BoxLayout(catPanel, BoxLayout.Y_AXIS));
        catPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                formatLabel(table),
                0, 0,
                new Font("Segoe UI", Font.BOLD, 12),
                TEXT_COLOR
        ));
        catPanel.setBackground(PANEL_BACKGROUND);

        List<JCheckBox> checkBoxes = new ArrayList<>();
        List<Product> products = getProductsForCategory(table, dao);

        if (products.isEmpty()) {
            addNoProductsMessage(catPanel);
        } else {
            addProductCheckboxes(catPanel, checkBoxes, products, table);
        }

        categoryCheckBoxes.put(table, checkBoxes);
        return catPanel;
    }

    private List<Product> getProductsForCategory(String table, ProductDAO dao) {
        List<Product> products = dao.getProductsFromTable(table);

        if ("profile".equalsIgnoreCase(table) && Session.selectedCabinaType != null) {
            addMissingProfiles(products);
        }

        return products;
    }

    private void addMissingProfiles(List<Product> products) {
        CabinTypeInfo info = CabinTypes.get(Session.selectedCabinaType);
        if (info != null) {
            for (String profil : info.getProfileLength().keySet()) {
                boolean exists = products.stream().anyMatch(p -> p.getCodProdus().equals(profil));
                if (!exists) {
                    products.add(new Product(profil, profil));
                }
            }
        }
    }

    private void addNoProductsMessage(JPanel panel) {
        JLabel noData = new JLabel("Niciun produs disponibil");
        noData.setForeground(LIGHT_GRAY);
        panel.add(noData);
    }

    private void addProductCheckboxes(JPanel panel, List<JCheckBox> checkBoxes, List<Product> products, String table) {
        for (Product product : products) {
            JPanel checkboxPanel = createCheckboxWithQuantityButton(product, table);
            checkBoxes.add((JCheckBox) checkboxPanel.getComponent(0));
            panel.add(checkboxPanel);
        }
    }

    private JPanel createCheckboxWithQuantityButton(Product product, String table) {
        JCheckBox checkBox = createCheckbox(product, table);
        JButton quantityButton = createQuantityButton(product, table);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.add(checkBox, BorderLayout.CENTER);
        panel.add(quantityButton, BorderLayout.EAST);

        return panel;
    }

    private JCheckBox createCheckbox(Product product, String table) {
        JCheckBox checkBox = new JCheckBox(product.getCodProdus() + " – " + product.getDenumire());
        checkBox.setForeground(TEXT_COLOR);
        checkBox.setBackground(PANEL_BACKGROUND);

        checkBox.addActionListener(e -> handleCheckboxSelection(checkBox, table));
        return checkBox;
    }

    private JButton createQuantityButton(Product product, String table) {
        JButton button = new JButton("Qty");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        button.setPreferredSize(new Dimension(45, 20));
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);

        button.addActionListener(e ->
                showQuantityDialog(table, product.getCodProdus(), product.getDenumire())
        );

        return button;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JButton backButton = createBackButton();
        JButton nextButton = createNextButton();

        bottomPanel.add(backButton);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(nextButton);

        return bottomPanel;
    }

    private JButton createBackButton() {
        JButton button = new JButton("Înapoi la tipul cabinei");
        button.setPreferredSize(new Dimension(180, 35));
        button.setBackground(new Color(100, 100, 100));
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.addActionListener(e -> navigator.goToStep(1));
        return button;
    }

    private JButton createNextButton() {
        JButton button = new JButton("Următorul pas");
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.addActionListener(e -> handleNextButton());
        return button;
    }

    // ==========================
    // EVENT HANDLING METHODS
    // ==========================

    private void handleCheckboxSelection(JCheckBox checkBox, String table) {
        List<String> selectedList = Session.hardwareMulti.getOrDefault(table, new ArrayList<>());
        String productCode = extractProductCode(checkBox.getText());

        if (checkBox.isSelected()) {
            addProductToSelection(selectedList, productCode, table);
        } else {
            removeProductFromSelection(selectedList, productCode, table); // MODIFICAT: Adăugat parametrul table
        }

        Session.hardwareMulti.put(table, selectedList);
        Session.save();
        updateSummary(table);
        updatePreview();
        updateRecipeInfo();
    }

    private String extractProductCode(String checkboxText) {
        return checkboxText.split(" – ")[0].trim();
    }

    private void addProductToSelection(List<String> selectedList, String productCode, String table) {
        if (!selectedList.contains(productCode)) {
            selectedList.add(productCode);
            initializeProductQuantity(table, productCode);
        }
    }

    private void initializeProductQuantity(String table, String productCode) {
        if (!Session.hardwareQuantities.containsKey(table)) {
            Session.hardwareQuantities.put(table, new HashMap<>());
        }
        // Setează cantitatea la valoarea implicită din rețetă dacă există
        int defaultQuantity = getDefaultQuantity(table, productCode);
        Session.hardwareQuantities.get(table).put(productCode, defaultQuantity);
    }

    // MODIFICAT: Adăugat parametrul table pentru a șterge cantitatea
    private void removeProductFromSelection(List<String> selectedList, String productCode, String table) {
        selectedList.remove(productCode);
        // Șterge și cantitatea din sesiune
        if (Session.hardwareQuantities.containsKey(table)) {
            Session.hardwareQuantities.get(table).remove(productCode);
        }
    }

    private void handleNextButton() {
        saveAllSelections();
        Session.save();
        navigator.goToStep(2);
    }

    private void saveAllSelections() {
        for (String table : tables) {
            List<String> selected = new ArrayList<>();
            for (JCheckBox checkBox : categoryCheckBoxes.get(table)) {
                if (checkBox.isSelected()) {
                    selected.add(extractProductCode(checkBox.getText()));
                }
            }
            Session.hardwareMulti.put(table, selected);
        }
    }

    // ==========================
    // QUANTITY MANAGEMENT METHODS
    // ==========================

    private void showQuantityDialog(String category, String productCode, String productName) {
        int currentQuantity = getCurrentQuantity(category, productCode);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Modificare Cantitate", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        contentPanel.setBackground(PANEL_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Informații produs
        JLabel productLabel = new JLabel(productCode + " - " + productName);
        productLabel.setForeground(TEXT_COLOR);
        productLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        contentPanel.add(productLabel);

        // Control cantitate
        JPanel spinnerPanel = new JPanel(new FlowLayout());
        spinnerPanel.setBackground(PANEL_BACKGROUND);

        JLabel quantityLabel = new JLabel("Cantitate:");
        quantityLabel.setForeground(TEXT_COLOR);
        spinnerPanel.add(quantityLabel);

        JSpinner spinner = createQuantitySpinner(currentQuantity);
        spinnerPanel.add(spinner);

        contentPanel.add(spinnerPanel);

        // Butoane
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PANEL_BACKGROUND);

        JButton okButton = new JButton("OK");
        okButton.setBackground(ACCENT_COLOR);
        okButton.setForeground(TEXT_COLOR);
        okButton.addActionListener(e -> {
            int newQuantity = (Integer) spinner.getValue();
            updateProductQuantity(category, productCode, newQuantity);
            dialog.dispose();
            showQuantityUpdateSuccess(productCode, newQuantity);
        });

        JButton cancelButton = new JButton("Anulează");
        cancelButton.setBackground(LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private int getCurrentQuantity(String category, String productCode) {
        return Session.hardwareQuantities
                .getOrDefault(category, new HashMap<>())
                .getOrDefault(productCode, getDefaultQuantity(category, productCode));
    }

    private int getDefaultQuantity(String category, String productCode) {
        if (Session.selectedCabinaType == null) {
            return 1;
        }

        CabinTypeInfo info = CabinTypes.get(Session.selectedCabinaType);
        if (info == null) {
            return 1;
        }

        // Caută în feronerie
        if (info.getFeronerieByCategory().containsKey(category)) {
            Map<String, Integer> products = info.getFeronerieByCategory().get(category);
            if (products.containsKey(productCode)) {
                return products.get(productCode);
            }
        }

        // Caută în profile
        if ("profile".equals(category)) {
            Map<String, Integer> profiles = info.getProfileLength();
            if (profiles.containsKey(productCode)) {
                return profiles.get(productCode);
            }
        }

        return 1;
    }

    private JSpinner createQuantitySpinner(int currentQuantity) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(currentQuantity, 1, 100, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(60, 25));
        return spinner;
    }

    private void updateProductQuantity(String category, String productCode, int newQuantity) {
        initializeCategoryQuantities(category);
        Session.hardwareQuantities.get(category).put(productCode, newQuantity);
        Session.save();

        updateSummary(category);
        updateRecipeInfo();
    }

    private void initializeCategoryQuantities(String category) {
        if (!Session.hardwareQuantities.containsKey(category)) {
            Session.hardwareQuantities.put(category, new HashMap<>());
        }
    }

    private void showQuantityUpdateSuccess(String productCode, int newQuantity) {
        JOptionPane.showMessageDialog(this,
                "Cantitatea pentru " + productCode + " a fost setată la: " + newQuantity,
                "Cantitate Actualizată",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================
    // RECIPE MANAGEMENT METHODS
    // ==========================

    private void showRecipeModificationDialog() {
        if (!isCabinaTypeSelected()) {
            showNoCabinaTypeWarning();
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Modificare Cantități Rețetă", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(520, 460);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PANEL_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getViewport().setBackground(PANEL_BACKGROUND);
        dialog.add(scrollPane, BorderLayout.CENTER);

        CabinTypeInfo info = CabinTypes.get(Session.selectedCabinaType);
        Map<String, JSpinner> allSpinners = new HashMap<>();

        JLabel instruction = new JLabel("Modificați cantitățile pentru produsele selectate:");
        instruction.setForeground(TEXT_COLOR);
        instruction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        instruction.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(instruction);
        mainPanel.add(Box.createVerticalStrut(15));

        boolean hasProducts = false;

        // === ITERĂM PRIN FIECARE CATEGORIE CA ÎN PANOUL PRINCIPAL ===
        for (String table : tables) {
            List<String> selectedCodes = Session.hardwareMulti.getOrDefault(table, new ArrayList<>());
            if (selectedCodes.isEmpty()) continue;

            hasProducts = true;

            // Titlu categorie
            JLabel catTitle = new JLabel(formatLabel(table) + ":");
            catTitle.setForeground(HIGHLIGHT_COLOR);
            catTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
            catTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(catTitle);
            mainPanel.add(Box.createVerticalStrut(8));

            // Produsele din acea categorie
            for (String code : selectedCodes) {
                int defaultQty = getDefaultQuantity(table, code);
                int currentQty = getCurrentQuantity(table, code);

                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
                row.setBackground(PANEL_BACKGROUND);
                row.setMaximumSize(new Dimension(480, 36));

                JLabel codeLabel = new JLabel(code);
                codeLabel.setForeground(TEXT_COLOR);
                codeLabel.setPreferredSize(new Dimension(100, 25));
                row.add(codeLabel);

                JLabel defLabel = new JLabel("(implicit: " + defaultQty + ")");
                defLabel.setForeground(LIGHT_GRAY);
                defLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                row.add(defLabel);

                JSpinner spinner = new JSpinner(new SpinnerNumberModel(currentQty, 1, 1000, 1));
                spinner.setPreferredSize(new Dimension(70, 28));
                row.add(spinner);

                allSpinners.put(table + ":" + code, spinner);
                mainPanel.add(row);
            }
            mainPanel.add(Box.createVerticalStrut(12));
        }

        if (!hasProducts) {
            JLabel empty = new JLabel("Niciun produs selectat pentru modificare.");
            empty.setForeground(LIGHT_GRAY);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(empty);
        }

        // === BUTOANE JOS ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BACKGROUND_COLOR);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton saveBtn = new JButton("Salvează Modificări");
        saveBtn.setBackground(ACCENT_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            saveQuantitiesFromSpinners(allSpinners);
            dialog.dispose();
            updateAllSummaries();
            updateRecipeInfo();
            showRecipeUpdateSuccess();
        });

        JButton cancelBtn = new JButton("Anulează");
        cancelBtn.setBackground(new Color(90, 90, 90));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(saveBtn);

        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // METODĂ NOUĂ: Obține doar feroneria selectată
    private Map<String, Map<String, Integer>> getSelectedFeronerie(CabinTypeInfo info) {
        Map<String, Map<String, Integer>> selectedFeronerie = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> categoryEntry : info.getFeronerieByCategory().entrySet()) {
            String category = categoryEntry.getKey();
            Map<String, Integer> products = categoryEntry.getValue();
            Map<String, Integer> selectedProducts = new HashMap<>();

            for (Map.Entry<String, Integer> productEntry : products.entrySet()) {
                String productCode = productEntry.getKey();
                // Verifică dacă produsul este selectat
                if (isProductSelected(category, productCode)) {
                    selectedProducts.put(productCode, productEntry.getValue());
                }
            }

            if (!selectedProducts.isEmpty()) {
                selectedFeronerie.put(category, selectedProducts);
            }
        }

        return selectedFeronerie;
    }

    // METODĂ NOUĂ: Obține doar profilele selectate
    private Map<String, Integer> getSelectedProfileLength(CabinTypeInfo info) {
        Map<String, Integer> selectedProfileLength = new HashMap<>();

        for (Map.Entry<String, Integer> profileEntry : info.getProfileLength().entrySet()) {
            String profileCode = profileEntry.getKey();
            // Verifică dacă profilul este selectat
            if (isProductSelected("profile", profileCode)) {
                selectedProfileLength.put(profileCode, profileEntry.getValue());
            }
        }

        return selectedProfileLength;
    }

    // METODĂ NOUĂ: Verifică dacă un produs este selectat
    private boolean isProductSelected(String category, String productCode) {
        return Session.hardwareMulti.getOrDefault(category, new ArrayList<>()).contains(productCode);
    }

    private void addCategoryControls(JPanel parent, String categoryName,
                                     Map<String, Map<String, Integer>> categories,
                                     Map<String, JSpinner> allSpinners) {

        JLabel categoryLabel = new JLabel(categoryName + ":");
        categoryLabel.setForeground(HIGHLIGHT_COLOR);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(categoryLabel);
        parent.add(Box.createVerticalStrut(5));

        for (Map.Entry<String, Map<String, Integer>> categoryEntry : categories.entrySet()) {
            String table = categoryEntry.getKey();
            Map<String, Integer> products = categoryEntry.getValue();

            for (Map.Entry<String, Integer> productEntry : products.entrySet()) {
                String productCode = productEntry.getKey();
                int defaultQuantity = productEntry.getValue();

                JPanel productPanel = createProductQuantityPanel(table, productCode, defaultQuantity, allSpinners);
                parent.add(productPanel);
                parent.add(Box.createVerticalStrut(3));
            }
        }
        parent.add(Box.createVerticalStrut(10));
    }

    private JPanel createProductQuantityPanel(String table, String productCode, int defaultQuantity,
                                              Map<String, JSpinner> allSpinners) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(PANEL_BACKGROUND);
        panel.setMaximumSize(new Dimension(450, 30));

        // Etichetă produs
        JLabel productLabel = new JLabel(productCode);
        productLabel.setForeground(TEXT_COLOR);
        productLabel.setPreferredSize(new Dimension(120, 25));
        panel.add(productLabel);

        // Etichetă cantitate implicită
        JLabel defaultLabel = new JLabel("(implicit: " + defaultQuantity + ")");
        defaultLabel.setForeground(LIGHT_GRAY);
        defaultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        defaultLabel.setPreferredSize(new Dimension(80, 25));
        panel.add(defaultLabel);

        // Spinner pentru cantitate - folosește cantitatea curentă sau cea implicită
        int currentQuantity = getCurrentQuantity(table, productCode);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(currentQuantity, 1, 1000, 1));
        spinner.setPreferredSize(new Dimension(60, 25));
        panel.add(spinner);

        // Stocare spinner pentru salvare
        String key = table + ":" + productCode;
        allSpinners.put(key, spinner);

        return panel;
    }

    private void saveQuantitiesFromSpinners(Map<String, JSpinner> allSpinners) {
        for (Map.Entry<String, JSpinner> entry : allSpinners.entrySet()) {
            String[] parts = entry.getKey().split(":");
            String table = parts[0];
            String productCode = parts[1];
            int newQuantity = (Integer) entry.getValue().getValue();

            updateProductQuantity(table, productCode, newQuantity);
        }
    }

    private boolean isCabinaTypeSelected() {
        return Session.selectedCabinaType != null && CabinTypes.exists(Session.selectedCabinaType);
    }

    private void showNoCabinaTypeWarning() {
        JOptionPane.showMessageDialog(this,
                "Vă rugăm să selectați mai întâi un tip de cabină!",
                "Lipsă Tip Cabină",
                JOptionPane.WARNING_MESSAGE);
    }

    private void showRecipeUpdateSuccess() {
        JOptionPane.showMessageDialog(this,
                "Toate cantitățile au fost actualizate!",
                "Rețetă Actualizată",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================
    // SUMMARY AND PREVIEW METHODS
    // ==========================

    private void updateSummary(String table) {
        List<String> selectedItems = getSelectedItemsWithQuantities(table);
        String summaryText = buildSummaryText(table, selectedItems);

        summaryLabels.get(table).setText(summaryText);
        updateSummaryLabelColor(table, summaryLabels.get(table));
    }

    private List<String> getSelectedItemsWithQuantities(String table) {
        List<String> selectedItems = new ArrayList<>();
        Map<String, Integer> quantities = Session.hardwareQuantities.getOrDefault(table, new HashMap<>());

        for (JCheckBox checkBox : categoryCheckBoxes.get(table)) {
            if (checkBox.isSelected()) {
                String productCode = extractProductCode(checkBox.getText());
                int quantity = quantities.getOrDefault(productCode, getDefaultQuantity(table, productCode));
                selectedItems.add(productCode + " (x" + quantity + ")");
            }
        }

        return selectedItems;
    }

    private String buildSummaryText(String table, List<String> selectedItems) {
        return formatLabel(table) + ": " +
                (selectedItems.isEmpty() ? "–" : String.join(", ", selectedItems));
    }

    private void updateSummaryLabelColor(String table, JLabel label) {
        List<String> selected = Session.hardwareMulti.getOrDefault(table, new ArrayList<>());
        label.setForeground(selected.isEmpty() ? LIGHT_GRAY : HIGHLIGHT_COLOR);
    }

    private void updatePreview() {
        String lastSelectedProduct = findLastSelectedProduct();

        if (lastSelectedProduct != null) {
            displayProductImage(lastSelectedProduct);
        } else {
            showNoImageSelected();
        }
    }

    private String findLastSelectedProduct() {
        for (String table : tables) {
            List<JCheckBox> checkBoxes = categoryCheckBoxes.get(table);
            for (int i = checkBoxes.size() - 1; i >= 0; i--) {
                if (checkBoxes.get(i).isSelected()) {
                    return extractProductCode(checkBoxes.get(i).getText());
                }
            }
        }
        return null;
    }

    private void showNoImageSelected() {
        previewLabel.setIcon(null);
        previewLabel.setText("Nicio imagine selectată");
    }

    // ==========================
    // RECIPE INFO METHODS
    // ==========================

    private void updateRecipeInfo() {
        if (Session.selectedCabinaType == null || !CabinTypes.exists(Session.selectedCabinaType)) {
            recipeInfoTextArea.setText("Selectați un tip de cabina pentru a vedea rețeta");
            return;
        }

        CabinTypeInfo info = CabinTypes.get(Session.selectedCabinaType);
        StringBuilder recipeText = new StringBuilder();

        recipeText.append("Rețetă pentru ").append(Session.selectedCabinaType).append("\n\n");

        // MODIFICARE: Folosim doar produsele selectate
        Map<String, Map<String, Integer>> selectedFeronerie = getSelectedFeronerie(info);
        Map<String, Integer> selectedProfileLength = getSelectedProfileLength(info);

        // Adăugare feronerie
        for (Map.Entry<String, Map<String, Integer>> cat : selectedFeronerie.entrySet()) {
            recipeText.append(formatLabel(cat.getKey())).append(":\n");
            for (Map.Entry<String, Integer> prod : cat.getValue().entrySet()) {
                int currentQty = getCurrentQuantity(cat.getKey(), prod.getKey());
                recipeText.append("  • ").append(prod.getKey())
                        .append(" - ").append(currentQty)
                        .append(" buc. (implicit: ").append(prod.getValue()).append(")\n");
            }
            recipeText.append("\n");
        }

        // Adăugare profile
        if (!selectedProfileLength.isEmpty()) {
            recipeText.append("Profile:\n");
            for (Map.Entry<String, Integer> prof : selectedProfileLength.entrySet()) {
                int currentQty = getCurrentQuantity("profile", prof.getKey());
                recipeText.append("  • ").append(prof.getKey())
                        .append(" - ").append(currentQty)
                        .append(" buc. (implicit: ").append(prof.getValue()).append(" m)\n");
            }
        }

        recipeInfoTextArea.setText(recipeText.toString());
        recipeInfoTextArea.setCaretPosition(0); // Scroll la început
    }

    private void updateAllSummaries() {
        for (String table : tables) {
            updateSummary(table);
        }
    }

    // ==========================
    // IMAGE HANDLING METHODS
    // ==========================

    private void onSummaryLabelClicked(String table, JLabel label) {
        List<String> selectedItems = Session.hardwareMulti.get(table);
        if (selectedItems == null || selectedItems.isEmpty()) {
            return;
        }

        if (selectedItems.size() == 1) {
            displayProductImage(selectedItems.get(0));
        } else {
            showProductSelectionDialog(table, selectedItems);
        }
    }

    private void showProductSelectionDialog(String table, List<String> products) {
        String[] options = products.toArray(new String[0]);

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Alegeți un produs pentru a vedea imaginea:",
                "Selectare Produs - " + formatLabel(table),
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selected != null) {
            displayProductImage(selected);
        }
    }

    private void displayProductImage(String productCode) {
        try {
            ImageIcon imageIcon = loadProductImage(productCode);

            if (imageIcon != null) {
                Image scaledImage = scaleImageToPreview(imageIcon);
                previewLabel.setIcon(new ImageIcon(scaledImage));
                previewLabel.setText("");
            } else {
                showImageNotFound(productCode);
            }
        } catch (Exception e) {
            showImageLoadError(productCode, e);
        }
    }

    private Image scaleImageToPreview(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        return image.getScaledInstance(
                previewLabel.getWidth(),
                previewLabel.getHeight(),
                Image.SCALE_SMOOTH
        );
    }

    private void showImageNotFound(String productCode) {
        previewLabel.setIcon(null);
        previewLabel.setText("Imagine indisponibilă: " + productCode);
    }

    private void showImageLoadError(String productCode, Exception e) {
        previewLabel.setIcon(null);
        previewLabel.setText("Eroare la încărcare: " + productCode);
        System.err.println("Eroare la încărcarea imaginii pentru " + productCode + ": " + e.getMessage());
    }

    // ==========================
    // RESOURCE LOADING METHODS
    // ==========================

    private void testResourceLoading() {
        System.out.println("=== TEST RESURSE DISPONIBILE ===");

        try {
            java.net.URL resourcesUrl = getClass().getResource("/");
            if (resourcesUrl != null) {
                System.out.println("Calea resurselor: " + resourcesUrl);
                java.nio.file.Path resourcesPath = java.nio.file.Paths.get(resourcesUrl.toURI());
                java.nio.file.Path imagesPath = resourcesPath.resolve("images/products");

                if (java.nio.file.Files.exists(imagesPath)) {
                    System.out.println("Directorul images/products există!");
                    java.nio.file.Files.list(imagesPath)
                            .limit(10)
                            .forEach(path -> System.out.println("  - " + path.getFileName()));
                } else {
                    System.out.println("Directorul images/products NU există!");
                }
            } else {
                System.out.println("Nu s-au putut găsi resursele!");
            }
        } catch (Exception e) {
            System.err.println("Eroare la testare resurse: " + e.getMessage());
        }
    }

    private ImageIcon loadProductImage(String productCode) {
        System.out.println("=== ÎNCĂRCARE IMAGINE PENTRU: " + productCode + " ===");

        // Try to find image_url from database first
        ImageIcon iconFromDatabase = loadImageFromDatabase(productCode);
        if (iconFromDatabase != null) {
            return iconFromDatabase;
        }

        // Try alternative methods
        return loadImageByProductCode(productCode);
    }

    private ImageIcon loadImageFromDatabase(String productCode) {
        for (String table : tables) {
            String imageUrl = new ProductDAO().getProductImage(table, productCode);
            System.out.println("Căutare în tabel " + table + ": image_url = '" + imageUrl + "'");

            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                String cleanImageUrl = imageUrl.trim();
                System.out.println("Am găsit image_url: " + cleanImageUrl);

                ImageIcon icon = tryImagePaths(cleanImageUrl);
                if (icon != null) {
                    return icon;
                }
            }
        }
        return null;
    }

    private ImageIcon tryImagePaths(String imageUrl) {
        String[] possiblePaths = {
                "/images/products/" + imageUrl,
                "/pictures/" + imageUrl,
                "/" + imageUrl,
                "src/main/resources/images/products/" + imageUrl,
                "images/products/" + imageUrl
        };

        for (String path : possiblePaths) {
            try {
                System.out.println("Încerc path: " + path);
                ImageIcon icon = loadImageFromPath(path);
                if (icon != null) {
                    return icon;
                }
            } catch (Exception e) {
                System.err.println("Eroare la path " + path + ": " + e.getMessage());
            }
        }
        return null;
    }

    private ImageIcon loadImageFromPath(String path) {
        if (path.startsWith("/")) {
            URL imageResourceUrl = getClass().getResource(path);
            if (imageResourceUrl != null) {
                System.out.println("SUCCES - Imagine găsită în resurse: " + imageResourceUrl);
                ImageIcon icon = new ImageIcon(imageResourceUrl);
                if (icon.getIconWidth() > 0) {
                    return icon;
                }
            }
        } else {
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                System.out.println("SUCCES - Fișier există: " + file.getAbsolutePath());
                return new ImageIcon(file.getAbsolutePath());
            }
        }
        return null;
    }

    private ImageIcon loadImageByProductCode(String productCode) {
        System.out.println("Încerc metode alternative pentru: " + productCode);

        String[] extensions = {".jpg", ".jpeg", ".png", ".gif"};
        String[] basePaths = {
                "/images/products/",
                "/pictures/",
                "/",
                "src/main/resources/images/products/",
                "images/products/"
        };

        for (String basePath : basePaths) {
            for (String ext : extensions) {
                String fullPath = basePath + productCode + ext;
                ImageIcon icon = loadImageFromPath(fullPath);
                if (icon != null) {
                    return icon;
                }
            }
        }

        System.out.println("ECHEC - Nicio imagine găsită pentru: " + productCode);
        return null;
    }

    // ==========================
    // INITIALIZATION METHODS
    // ==========================

    public void refreshSelections() {
        prepopulateFromCabinType();
        updateRecipeInfo();
    }

    private void prepopulateFromCabinType() {
        if (Session.selectedCabinaType == null || !CabinTypes.exists(Session.selectedCabinaType)) return;

        CabinTypeInfo info = CabinTypes.get(Session.selectedCabinaType);
        if (info == null) return;

        clearAllSelections();
        populateSelectionsFromCabinType(info);
        updateAllSummariesAndPreview();
    }

    private void clearAllSelections() {
        categoryCheckBoxes.values().forEach(list -> list.forEach(cb -> cb.setSelected(false)));
        Session.hardwareMulti.clear();
        Session.hardwareQuantities.clear(); // MODIFICAT: Curățăm și cantitățile
    }

    private void populateSelectionsFromCabinType(CabinTypeInfo info) {
        Map<String, List<String>> itemsPerCategory = new HashMap<>();

        populateFeronerieSelections(info, itemsPerCategory);
        populateProfileSelections(info, itemsPerCategory);

        Session.hardwareMulti.putAll(itemsPerCategory);
    }

    private void populateFeronerieSelections(CabinTypeInfo info, Map<String, List<String>> itemsPerCategory) {
        for (Map.Entry<String, Map<String, Integer>> entry : info.getFeronerieByCategory().entrySet()) {
            String category = entry.getKey();
            Map<String, Integer> items = entry.getValue();

            List<JCheckBox> checkBoxes = categoryCheckBoxes.get(category);
            if (checkBoxes == null) continue;

            for (String productCode : items.keySet()) {
                selectCheckboxByProductCode(checkBoxes, productCode, category, itemsPerCategory);
            }
        }
    }

    private void populateProfileSelections(CabinTypeInfo info, Map<String, List<String>> itemsPerCategory) {
        for (String profile : info.getProfileLength().keySet()) {
            List<JCheckBox> checkBoxes = categoryCheckBoxes.get("profile");
            if (checkBoxes == null) continue;

            selectCheckboxByProductCode(checkBoxes, profile, "profile", itemsPerCategory);
        }
    }

    private void selectCheckboxByProductCode(List<JCheckBox> checkBoxes, String productCode,
                                             String category, Map<String, List<String>> itemsPerCategory) {
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.getText().startsWith(productCode)) {
                checkBox.setSelected(true);
                itemsPerCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(productCode);

                // Inițializează cantitatea cu valoarea din rețetă
                initializeProductQuantity(category, productCode);
                break;
            }
        }
    }

    private void updateAllSummariesAndPreview() {
        categoryCheckBoxes.keySet().forEach(this::updateSummary);
        updatePreview();
    }

    // ==========================
    // UTILITY METHODS
    // ==========================

    private String formatLabel(String table) {
        return table.substring(0, 1).toUpperCase() + table.substring(1).replace("_", " ");
    }
}