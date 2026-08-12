package gui.preview;

import util.session.Session;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ShowerPreviewPanel extends JPanel {

    public ShowerPreviewPanel() {
        setBackground(new Color(60, 60, 60));
        setPreferredSize(new Dimension(400, 500));
    }

    //! ==============================
    //! PUBLIC METHODS FOR PDF EXPORT
    //! ==============================

    public BufferedImage getPreviewImage() {
        return getPreviewImage(500, 600);
    }

    public BufferedImage getPreviewImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        setupGraphicsQuality(g2);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        drawShowerPreview(g2, width, height, true);

        g2.dispose();
        return image;
    }

    public BufferedImage getHighQualityPreviewImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        setupHighQualityGraphics(g2);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        drawShowerPreview(g2, width, height, true);

        g2.dispose();
        return image;
    }

    //! ==============================
    //! MAIN PAINTING METHOD
    //! ==============================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setupGraphicsQuality(g2d);

        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        drawShowerPreview(g2d, getWidth(), getHeight(), false);
    }

    private void drawShowerPreview(Graphics2D g2d, int width, int height, boolean isForPdf) {
        DrawingContext context = setupDrawingContext(width, height);
        if (context == null) return;

        String productType = Session.selectedProductType != null ? Session.selectedProductType : "";

        switch (productType) {
            case "panou":
                drawPanouFix(g2d, context, isForPdf);
                break;
            case "tipu_1":
            case "tipu_2":
                drawShowerType1_2(g2d, context, isForPdf);
                break;
            case "tipu_3":
                drawShowerType3(g2d, context, isForPdf);
                break;
            case "tipu_4":
                drawShowerType4(g2d, context, isForPdf);
                break;
            case "tipu_5":
            case "tipu_6":
                drawShowerType5_6(g2d, context, isForPdf);
                break;
            default:
                drawShowerType1_2(g2d, context, isForPdf);
        }
    }

    //! ==============================
    //! DRAWING CONTEXT SETUP
    //! ==============================

    private DrawingContext setupDrawingContext(int width, int height) {
        int margin = 40;
        int availableWidth = width - 2 * margin;
        int availableHeight = height - 2 * margin;

        int numGlasses = getNumberOfGlasses();
        double[] dimensions = parseDimensions(numGlasses);
        if (dimensions == null) return null;

        double maxHeight = getMaxHeight(dimensions);
        double totalWidth = getTotalWidth(dimensions);
        //! Scală redusă pentru a încăpea în pagină
        double scale = Math.min(availableWidth / totalWidth, availableHeight / maxHeight) * 0.7;

        int startX = margin + (int)((availableWidth - totalWidth * scale) / 2);
        int startY = margin + (int)((availableHeight - maxHeight * scale) / 2);

        return new DrawingContext(dimensions, numGlasses, scale, startX, startY);
    }

    //! ==============================
    //! SHOWER TYPE SPECIFIC DRAWING METHODS
    //! ==============================

    private void drawShowerType1_2(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
        drawHardwareType1_2(g2d, context, isForPdf);
        drawAllDimensions(g2d, context, isForPdf);
    }

    private void drawShowerType3(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
        drawHardwareType3(g2d, context, isForPdf);
        drawAllDimensions(g2d, context, isForPdf);
    }

    private void drawShowerType4(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawGlassType4(g2d, context, isForPdf);
        drawHardwareType4(g2d, context, isForPdf);
        drawDimensionsType4(g2d, context, isForPdf);
    }

    private void drawShowerType5_6(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
        drawHardwareType5_6(g2d, context, isForPdf);
        drawAllDimensions(g2d, context, isForPdf);
    }

    //! ==============================
    //! GLASS DRAWING METHODS
    //! ==============================

    private void drawAllGlasses(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        for (int i = 0; i < context.numGlasses; i++) {
            GlassPosition pos = getGlassPosition(context, i);
            if (i == 0 && Session.selectedCabinaType.equals("tipu_4")) {
                drawObliqueGlass(g2d, pos, isForPdf);
            } else {
                drawStandardGlass(g2d, pos, i + 1, isForPdf);
            }
        }
    }

    private void drawGlassType4(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
    }

    private void drawObliqueGlass(Graphics2D g2d, GlassPosition pos, boolean isForPdf) {
        Color glassColor = getGlassColor(isForPdf);

        DrawingContext context = setupDrawingContext(getWidth(), getHeight());
        if (context != null && context.numGlasses > 1) {
            GlassPosition middleGlass = getGlassPosition(context, 1);

            int middleHeight = middleGlass.height;
            int perspectiveOffset = (int)(middleHeight * 0.3);

            int[] xPoints = new int[4];
            int[] yPoints = new int[4];

            xPoints[0] = pos.x;
            yPoints[0] = pos.y;

            xPoints[1] = pos.x;
            yPoints[1] = pos.y + middleHeight;

            xPoints[2] = pos.x + pos.width;
            yPoints[2] = pos.y + middleHeight - perspectiveOffset;

            xPoints[3] = pos.x + pos.width;
            yPoints[3] = pos.y - perspectiveOffset;

            AffineTransform oldTransform = g2d.getTransform();
            int centerX = pos.x + pos.width / 2;
            g2d.translate(centerX, 0);
            g2d.scale(-1, 1);
            g2d.translate(-centerX, 0);

            if (isForPdf) {
                g2d.setColor(glassColor);
            } else {
                g2d.setColor(new Color(glassColor.getRed(), glassColor.getGreen(), glassColor.getBlue(), 180));
            }
            g2d.fillPolygon(xPoints, yPoints, 4);

            g2d.setColor(isForPdf ? Color.DARK_GRAY : glassColor.darker());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawPolygon(xPoints, yPoints, 4);

            //! ✳️ revenim la transformarea normală (textul nu va fi oglindit)
            g2d.setTransform(oldTransform);

            //! ✳️ abia acum desenăm textul și detaliile
            drawObliqueGlassDetails(g2d, pos, perspectiveOffset, middleHeight, isForPdf);
        }
    }


    private void drawObliqueGlassDetails(Graphics2D g2d, GlassPosition pos, int perspectiveOffset, int glassHeight, boolean isForPdf) {
        if (Session.sticlaGrosime != null && !Session.sticlaGrosime.isEmpty()) {
            g2d.setColor(isForPdf ? Color.BLACK : Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String thicknessText = "Grosime: " + Session.sticlaGrosime + " mm";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(thicknessText);

            int centerX = pos.x + pos.width/2;
            int centerY = pos.y + glassHeight/2 + perspectiveOffset/2;
            g2d.drawString(thicknessText, centerX - textWidth/2, centerY);
        }

        if (Session.sticlaTip != null && !Session.sticlaTip.isEmpty() &&
                Session.sticlaNume != null && !Session.sticlaNume.isEmpty()) {
            g2d.setColor(isForPdf ? Color.BLACK : Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            String nameText = Session.sticlaNume;
            String typeText = getShortGlassType(Session.sticlaTip);
            String fullText = nameText + " - " + typeText;

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(fullText);

            int textX = pos.x + pos.width/2 - textWidth/2;
            int textY = pos.y + glassHeight/2 + perspectiveOffset/2 + 15;
            g2d.drawString(fullText, textX, textY);
        }
    }

    private void drawStandardGlass(Graphics2D g2d, GlassPosition pos, int glassNumber, boolean isForPdf) {
        Color glassColor = getGlassColor(isForPdf);

        if (isForPdf) {
            g2d.setColor(glassColor);
            g2d.fillRect(pos.x, pos.y, pos.width, pos.height);
        } else {
            g2d.setColor(new Color(glassColor.getRed(), glassColor.getGreen(), glassColor.getBlue(), 180));
            g2d.fillRect(pos.x, pos.y, pos.width, pos.height);
        }

        g2d.setColor(isForPdf ? Color.DARK_GRAY : glassColor.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(pos.x, pos.y, pos.width, pos.height);

        drawGlassDetails(g2d, pos, glassNumber, isForPdf);
    }

    private void drawGlassDetails(Graphics2D g2d, GlassPosition pos, int glassNumber, boolean isForPdf) {
        if (Session.sticlaGrosime != null && !Session.sticlaGrosime.isEmpty()) {
            g2d.setColor(isForPdf ? Color.BLACK : Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String thicknessText = "Grosime: " + Session.sticlaGrosime + " mm";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(thicknessText);
            g2d.drawString(thicknessText, pos.x + pos.width/2 - textWidth/2, pos.y + 20);
        }

        if (Session.sticlaTip != null && !Session.sticlaTip.isEmpty() &&
                Session.sticlaNume != null && !Session.sticlaNume.isEmpty()) {
            g2d.setColor(isForPdf ? Color.BLACK : Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            String nameText = Session.sticlaNume;
            String typeText = getShortGlassType(Session.sticlaTip);
            String fullText = nameText + " - " + typeText;

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(fullText);
            g2d.drawString(fullText, pos.x + pos.width/2 - textWidth/2, pos.y + pos.height - 10);
        }
    }

    //! ==============================
    //! HARDWARE DRAWING METHODS
    //! ==============================

    private void drawHardwareType1_2(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColor(isForPdf);
        GlassPosition pos1 = getGlassPosition(context, 0);

        drawHinges(g2d, pos1.x, pos1.y, pos1.width, pos1.height, pos1.glassHeight,
                hardwareColor, isForPdf, HingePosition.LEFT);
        drawHandle(g2d, pos1.x, pos1.y, pos1.width, pos1.height, hardwareColor, isForPdf, HandlePosition.RIGHT);
    }

    private void drawHardwareType3(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColor(isForPdf);
        GlassPosition pos2 = getGlassPosition(context, 1);
        GlassPosition pos3 = getGlassPosition(context, 2);

        drawHandle(g2d, pos2.x, pos2.y, pos2.width, pos2.height, hardwareColor, isForPdf, HandlePosition.RIGHT);
        drawSplitHinges(g2d, pos2, pos3, pos2.glassHeight, hardwareColor, isForPdf);
    }

    private void drawHardwareType4(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColor(isForPdf);
        GlassPosition pos2 = getGlassPosition(context, 1);
        GlassPosition pos3 = getGlassPosition(context, 2);

        drawHandle(g2d, pos2.x, pos2.y, pos2.width, pos2.height, hardwareColor, isForPdf, HandlePosition.RIGHT);
        drawSplitHinges(g2d, pos2, pos3, pos2.glassHeight, hardwareColor, isForPdf);
    }

    private void drawHardwareType5_6(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColor(isForPdf);

        GlassPosition pos2 = getGlassPosition(context, 1);
        drawHandle(g2d, pos2.x, pos2.y, pos2.width, pos2.height, hardwareColor, isForPdf, HandlePosition.RIGHT);

        GlassPosition pos1 = getGlassPosition(context, 0);
        drawSplitHingesBetweenGlasses(g2d, pos1, pos2, pos1.glassHeight, hardwareColor, isForPdf);

        if (context.numGlasses >= 4) {
            GlassPosition pos3 = getGlassPosition(context, 2);
            GlassPosition pos4 = getGlassPosition(context, 3);
            drawSplitHingesBetweenGlasses(g2d, pos3, pos4, pos3.glassHeight, hardwareColor, isForPdf);
        }
    }

    //! ==============================
    //! HARDWARE COMPONENT METHODS
    //! ==============================

    private void drawHinges(Graphics2D g2d, int x, int y, int width, int height, double glassHeightMeters,
                            Color hardwareColor, boolean isForPdf, HingePosition position) {
        g2d.setColor(hardwareColor);

        double offsetRatio = 0.15 / glassHeightMeters;
        int offsetPixels = (int)(height * offsetRatio);
        int hingeWidth = Math.max(5, width / 10);
        int hingeHeight = Math.max(8, height / 30);

        int hingeX = position == HingePosition.LEFT ? x : x + width - hingeWidth;

        int hingeY1 = y + offsetPixels - hingeHeight / 2;
        g2d.fillRect(hingeX, hingeY1, hingeWidth, hingeHeight);

        int hingeY2 = y + height - offsetPixels - hingeHeight / 2;
        g2d.fillRect(hingeX, hingeY2, hingeWidth, hingeHeight);
    }

    private void drawSplitHinges(Graphics2D g2d, GlassPosition leftGlass, GlassPosition rightGlass,
                                 double glassHeightMeters, Color hardwareColor, boolean isForPdf) {
        g2d.setColor(hardwareColor);

        double offsetRatio = 0.15 / glassHeightMeters;
        int offsetPixels = (int)(leftGlass.height * offsetRatio);
        int hingeWidth = Math.max(5, leftGlass.width / 20);
        int hingeHeight = Math.max(8, leftGlass.height / 40);

        int hingeY1 = leftGlass.y + offsetPixels - hingeHeight / 2;
        int hingeY2 = leftGlass.y + leftGlass.height - offsetPixels - hingeHeight / 2;

        g2d.fillRect(leftGlass.x + leftGlass.width - hingeWidth/2, hingeY1, hingeWidth/2, hingeHeight);
        g2d.fillRect(leftGlass.x + leftGlass.width - hingeWidth/2, hingeY2, hingeWidth/2, hingeHeight);

        g2d.fillRect(rightGlass.x, hingeY1, hingeWidth/2, hingeHeight);
        g2d.fillRect(rightGlass.x, hingeY2, hingeWidth/2, hingeHeight);
    }

    private void drawSplitHingesBetweenGlasses(Graphics2D g2d, GlassPosition leftGlass, GlassPosition rightGlass,
                                               double glassHeightMeters, Color hardwareColor, boolean isForPdf) {
        g2d.setColor(hardwareColor);

        double offsetRatio = 0.15 / glassHeightMeters;
        int offsetPixels = (int)(leftGlass.height * offsetRatio);
        int hingeWidth = Math.max(5, leftGlass.width / 20);
        int hingeHeight = Math.max(8, leftGlass.height / 40);

        int hingeY1 = leftGlass.y + offsetPixels - hingeHeight / 2;
        int hingeY2 = leftGlass.y + leftGlass.height - offsetPixels - hingeHeight / 2;

        g2d.fillRect(leftGlass.x + leftGlass.width - hingeWidth/2, hingeY1, hingeWidth/2, hingeHeight);
        g2d.fillRect(leftGlass.x + leftGlass.width - hingeWidth/2, hingeY2, hingeWidth/2, hingeHeight);

        g2d.fillRect(rightGlass.x, hingeY1, hingeWidth/2, hingeHeight);
        g2d.fillRect(rightGlass.x, hingeY2, hingeWidth/2, hingeHeight);
    }

    private void drawHandle(Graphics2D g2d, int x, int y, int width, int height,
                            Color hardwareColor, boolean isForPdf, HandlePosition position) {
        g2d.setColor(hardwareColor);

        int handleSize = Math.min(25, Math.min(width, height) / 14);
        int handleX = position == HandlePosition.LEFT ? x + 5 : x + width - handleSize - 9;
        int handleY = y + (height - handleSize) / 2;

        g2d.fillOval(handleX, handleY, handleSize, handleSize);

        g2d.setColor(isForPdf ? hardwareColor.darker() : hardwareColor.brighter());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(handleX, handleY, handleSize, handleSize);
    }

    //! ==============================
    //! DIMENSION DRAWING METHODS
    //! ==============================

    private void drawAllDimensions(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        for (int i = 0; i < context.numGlasses; i++) {
            GlassPosition pos = getGlassPosition(context, i);
            if (i == 0 && Session.selectedCabinaType.equals("tipu_4")) {
                drawObliqueGlassDimensions(g2d, pos, isForPdf);
            } else {
                drawGlassDimensions(g2d, pos, i + 1, context.numGlasses, isForPdf);
            }
        }
    }

    private void drawDimensionsType4(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllDimensions(g2d, context, isForPdf);
    }

    private void drawObliqueGlassDimensions(Graphics2D g2d, GlassPosition pos, boolean isForPdf) {
        DrawingContext context = setupDrawingContext(getWidth(), getHeight());
        if (context == null || context.numGlasses <= 1) return;

        GlassPosition middleGlass = getGlassPosition(context, 1);
        int glassHeight = middleGlass.height;
        int perspectiveOffset = (int)(glassHeight * 0.3);

        g2d.setColor(isForPdf ? Color.BLACK : Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        //! Dimensiuni corecte pentru paralelogram
        int heightMm = (int)(middleGlass.glassHeight * 1000);
        int widthMm = (int)(pos.glassWidth * 1000);
        int depthMm = (int)((perspectiveOffset / pos.scale) * 1000);

        FontMetrics fm = g2d.getFontMetrics();

        //! Înălțimea (partea stângă)
        String heightText = heightMm + " mm";
        int heightTextWidth = fm.stringWidth(heightText);

        int lineX = pos.x - 15;
        g2d.drawLine(lineX, pos.y, lineX, pos.y + glassHeight);
        g2d.drawLine(lineX - 3, pos.y, lineX + 3, pos.y);
        g2d.drawLine(lineX - 3, pos.y + glassHeight, lineX + 3, pos.y + glassHeight);

        AffineTransform old = g2d.getTransform();
        g2d.rotate(-Math.PI/2, lineX - 10, pos.y + glassHeight/2);
        g2d.drawString(heightText, lineX - 10 - heightTextWidth/2, pos.y + glassHeight/2);
        g2d.setTransform(old);

        //! Lățimea (partea de jos) - dimensiunea reală a sticlei
        String widthText = widthMm + " mm";
        int widthTextWidth = fm.stringWidth(widthText);

        int lineY = pos.y + glassHeight + 15;
        g2d.drawLine(pos.x, lineY, pos.x + pos.width, lineY);
        g2d.drawLine(pos.x, lineY - 3, pos.x, lineY + 3);
        g2d.drawLine(pos.x + pos.width, lineY - 3, pos.x + pos.width, lineY + 3);
        g2d.drawString(widthText, pos.x + pos.width/2 - widthTextWidth/2, lineY + 15);

        //! Adâncimea (offset-ul pentru perspectivă)

    }

    private void drawGlassDimensions(Graphics2D g2d, GlassPosition pos, int glassNumber,
                                     int totalGlasses, boolean isForPdf) {
        g2d.setColor(isForPdf ? Color.BLACK : Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        int heightMm = (int)(pos.glassHeight * 1000);
        int widthMm = (int)(pos.glassWidth * 1000);

        FontMetrics fm = g2d.getFontMetrics();

        String heightText = heightMm + " mm";
        int textWidth = fm.stringWidth(heightText);

        if (glassNumber == 1) {
            drawHeightDimension(g2d, pos.x - 15, pos.y, pos.height, heightText, textWidth, -10);
        } else if (glassNumber == totalGlasses) {
            drawHeightDimension(g2d, pos.x + pos.width + 15, pos.y, pos.height, heightText, textWidth, 10);
        }

        drawWidthDimension(g2d, pos.x, pos.y + pos.height, pos.width, widthMm, fm);
    }

    private void drawHeightDimension(Graphics2D g2d, int lineX, int y, int height,
                                     String text, int textWidth, int textOffset) {
        g2d.drawLine(lineX, y, lineX, y + height);
        g2d.drawLine(lineX - 3, y, lineX + 3, y);
        g2d.drawLine(lineX - 3, y + height, lineX + 3, y + height);

        AffineTransform old = g2d.getTransform();
        g2d.rotate(-Math.PI/2, lineX + textOffset, y + height/2);
        g2d.drawString(text, lineX + textOffset - textWidth/2, y + height/2);
        g2d.setTransform(old);
    }
    private void drawPanouFix(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        // Desenează singura sticlă
        GlassPosition pos = getGlassPosition(context, 0);
        drawStandardGlass(g2d, pos, 1, isForPdf);

        // Balamale doar pe stânga (fixare în perete)
        Color hardwareColor = getHardwareColor(isForPdf);
        drawHinges(g2d, pos.x, pos.y, pos.width, pos.height, pos.glassHeight,
                hardwareColor, isForPdf, HingePosition.LEFT);

        // Dimensiuni clare pe ambele laturi
        drawGlassDimensions(g2d, pos, 1, 1, isForPdf);
    }

    private void drawWidthDimension(Graphics2D g2d, int x, int lineY, int width, int widthMm, FontMetrics fm) {
        String widthText = widthMm + " mm";
        int textWidth2 = fm.stringWidth(widthText);

        g2d.drawLine(x, lineY, x + width, lineY);
        g2d.drawLine(x, lineY - 3, x, lineY + 3);
        g2d.drawLine(x + width, lineY - 3, x + width, lineY + 3);
        g2d.drawString(widthText, x + width/2 - textWidth2/2, lineY + 15);
    }

    //! ==============================
    //! HELPER METHODS
    //! ==============================

    private GlassPosition getGlassPosition(DrawingContext context, int index) {
        double glassWidth = context.dimensions[index * 2 + 1];
        double glassHeight = context.dimensions[index * 2];
        int drawWidth = (int)(glassWidth * context.scale);
        int drawHeight = (int)(glassHeight * context.scale);
        int x = context.startX + (int)(getCumulativeWidth(context.dimensions, index) * context.scale);

        return new GlassPosition(x, context.startY, drawWidth, drawHeight, glassHeight, glassWidth, context.scale);
    }

    private void setupGraphicsQuality(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private void setupHighQualityGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    //! ==============================
    //! DIMENSION CALCULATION METHODS
    //! ==============================

    private int getNumberOfGlasses() {
        String productType = Session.selectedProductType;
        if ("panou".equals(productType)) {
            return 1;
        }

        if (Session.selectedCabinaType == null) return 2;
        return switch (Session.selectedCabinaType) {
            case "tipu_1", "tipu_2" -> 2;
            case "tipu_3", "tipu_4" -> 3;
            case "tipu_5", "tipu_6" -> 4;
            default -> 2;
        };
    }

    private double[] parseDimensions(int numGlasses) {
        if (Session.dimensiuni == null || Session.dimensiuni.isEmpty()) {
            double[] defaultDimensions = new double[numGlasses * 2];
            for (int i = 0; i < numGlasses; i++) {
                defaultDimensions[i * 2] = 2.0;
                defaultDimensions[i * 2 + 1] = 0.9;
            }
            return defaultDimensions;
        }

        try {
            String[] parts = Session.dimensiuni.split("x");
            double[] dimensions = new double[numGlasses * 2];
            for (int i = 0; i < Math.min(parts.length, numGlasses * 2); i++) {
                dimensions[i] = Double.parseDouble(parts[i].trim()) / 1000.0;
            }
            for (int i = parts.length; i < numGlasses * 2; i++) {
                dimensions[i] = (i % 2 == 0) ? 2.0 : 0.9;
            }
            return dimensions;
        } catch (Exception e) {
            System.err.println("Eroare la parsarea dimensiunilor: " + Session.dimensiuni);
            double[] defaultDimensions = new double[numGlasses * 2];
            for (int i = 0; i < numGlasses; i++) {
                defaultDimensions[i * 2] = 2.0;
                defaultDimensions[i * 2 + 1] = 0.9;
            }
            return defaultDimensions;
        }
    }

    private double getMaxHeight(double[] dimensions) {
        double max = 0;
        for (int i = 0; i < dimensions.length; i += 2) {
            max = Math.max(max, dimensions[i]);
        }
        return max;
    }

    private double getTotalWidth(double[] dimensions) {
        double total = 0;
        for (int i = 1; i < dimensions.length; i += 2) {
            total += dimensions[i];
        }
        return total;
    }

    private double getCumulativeWidth(double[] dimensions, int index) {
        double cumulative = 0;
        for (int i = 0; i < index; i++) {
            cumulative += dimensions[i * 2 + 1];
        }
        return cumulative;
    }

    private String getShortGlassType(String fullType) {
        if (fullType == null) return "";
        if (fullType.contains("Securizata")) return "Securiz.";
        if (fullType.contains("Simpla")) return "Simpla";
        return fullType.length() > 8 ? fullType.substring(0, 8) : fullType;
    }

    private Color getGlassColor(boolean isForPdf) {
        if (Session.sticlaNume != null) {
            switch (Session.sticlaNume.toLowerCase()) {
                case "dark grey":
                    return isForPdf ? new Color(60, 60, 80) : new Color(80, 80, 100);
                case "bronze":
                    return isForPdf ? new Color(185, 107, 30) : new Color(205, 127, 50);
                case "clear":
                    return isForPdf ? new Color(220, 220, 240) : new Color(240, 240, 255, 180);
                default:
                    return isForPdf ? new Color(200, 200, 220) : new Color(220, 220, 255, 160);
            }
        }
        return isForPdf ? new Color(200, 200, 220) : new Color(220, 220, 255, 160);
    }

    private Color getHardwareColor(boolean isForPdf) {
        switch (Session.finisajID) {
            case 1: return isForPdf ? new Color(150, 150, 150) : new Color(200, 200, 200);
            case 2: return isForPdf ? new Color(180, 180, 180) : new Color(230, 230, 230);
            case 3: return Color.BLACK;
            case 4: return isForPdf ? new Color(200, 200, 200) : Color.WHITE;
            case 5: return isForPdf ? new Color(205, 175, 0) : new Color(255, 215, 0);
            case 6: return isForPdf ? new Color(205, 152, 163) : new Color(255, 192, 203);
            default: return isForPdf ? new Color(150, 150, 150) : new Color(200, 200, 200);
        }
    }

    //! ==============================
    //! HELPER CLASSES
    //! ==============================

    private static class DrawingContext {
        final double[] dimensions;
        final int numGlasses;
        final double scale;
        final int startX;
        final int startY;

        DrawingContext(double[] dimensions, int numGlasses, double scale, int startX, int startY) {
            this.dimensions = dimensions;
            this.numGlasses = numGlasses;
            this.scale = scale;
            this.startX = startX;
            this.startY = startY;
        }
    }

    private static class GlassPosition {
        final int x;
        final int y;
        final int width;
        final int height;
        final double glassHeight;
        final double glassWidth;
        final double scale;

        GlassPosition(int x, int y, int width, int height, double glassHeight, double glassWidth, double scale) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.glassHeight = glassHeight;
            this.glassWidth = glassWidth;
            this.scale = scale;
        }
    }

    private enum HingePosition {
        LEFT, RIGHT
    }

    private enum HandlePosition {
        LEFT, RIGHT
    }
}