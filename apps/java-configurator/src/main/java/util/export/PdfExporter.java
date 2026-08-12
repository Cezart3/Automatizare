package util.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import gui.steps.GlassPanel;
import util.config.Settings;
import util.session.Session;

import java.awt.*;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PdfExporter {

    // ==============================
    //         PUBLIC METHOD
    // ==============================

    public static void exportToPdf(File file, GlassPanel step6Panel,
                                   double pretEuro, double pretFinalEuro, double pretFinalRon, double curs) throws Exception {

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Fonturi generale (itext)
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY);
        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.BLACK);
        com.itextpdf.text.Font priceFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD, new BaseColor(255, 87, 34));

        // Pagina 1: Configurație generală
        addGeneralInfoPage(document, titleFont, headerFont, normalFont);
        if (pretFinalEuro > 0) {
            addPricePage(document, pretEuro, pretFinalEuro, pretFinalRon, curs,
                    titleFont, headerFont, boldFont, normalFont, priceFont);
        }

        // Pagina 2: Desen tehnic
        addDrawingPage(document, writer, titleFont, normalFont);



        document.close();
    }

    // ==============================
    //     PRIVATE HELPER METHODS
    // ==============================

    private static void addGeneralInfoPage(Document document, com.itextpdf.text.Font titleFont, com.itextpdf.text.Font headerFont, com.itextpdf.text.Font normalFont) throws DocumentException {
        Paragraph title = new Paragraph("Oferta tehnica si financiara - Cabina de dus", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        Paragraph dateInfo = new Paragraph("Data emiterii: " +
                new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()), normalFont);
        dateInfo.setSpacingAfter(10);
        document.add(dateInfo);

        document.add(createConfigTable(headerFont, normalFont));
        document.add(createGlassTable(headerFont, normalFont));
        document.add(createHardwareTable(headerFont, normalFont));
    }

    private static PdfPTable createConfigTable(com.itextpdf.text.Font headerFont, com.itextpdf.text.Font normalFont) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(15);

        addTableHeader(table, "Configuratie Cabina", headerFont, 2);
        addConfigRow(table, "Tip cabina:", getCabinaTypeName(Session.selectedCabinaType), normalFont);
        String material = (Session.finisajID == 1 || Session.finisajID == 2) ? "Zinc" : "Otel Inoxidabil";
        addConfigRow(table, "Material:", material, normalFont);
        addConfigRow(table, "Finisaj:", getFinishName(Session.finisajID), normalFont);
        addConfigRow(table, "Dimensiuni:", Session.dimensiuni, normalFont);
        return table;
    }

    private static PdfPTable createGlassTable(com.itextpdf.text.Font headerFont, com.itextpdf.text.Font normalFont) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(15);

        addTableHeader(table, "Specificatii Sticla", headerFont, 2);

        addConfigRow(table, "Nume sticla:", Session.sticlaNume, normalFont);
        addConfigRow(table, "Grosime:", Session.sticlaGrosime + " mm", normalFont);
        addConfigRow(table, "Tip sticla:", Session.sticlaTip, normalFont);
        addConfigRow(table, "Gaurire:", Session.sticlaGaurire, normalFont);
        addConfigRow(table, "Număr găuriri extra:", Session.sticlaNumarGauririExtra, normalFont);
        addConfigRow(table, "Număr decupaje extra:", Session.sticlaNumarDecupajeExtra, normalFont);

        if (Session.sticlaForma != null && !Session.sticlaForma.isEmpty()) {
            addConfigRow(table, "Forma/Sablon:", Session.sticlaForma, normalFont);
        }

        if (!Session.sticlaFormaSablonMap.isEmpty()) {
            for (Map.Entry<Integer, String> entry : Session.sticlaFormaSablonMap.entrySet()) {
                addConfigRow(table, "Sticla " + (entry.getKey() + 1) + ":", entry.getValue(), normalFont);
            }
        }

        return table;
    }

    private static PdfPTable createHardwareTable(com.itextpdf.text.Font headerFont, com.itextpdf.text.Font normalFont) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(15);

        addTableHeader(table, "Feronerie Selectata", headerFont, 2);

        if (Session.hardwareMulti == null || Session.hardwareMulti.isEmpty()) {
            addConfigRow(table, "Feronerie:", "Niciun element selectat", normalFont);
        } else {
            for (Map.Entry<String, List<String>> entry : Session.hardwareMulti.entrySet()) {
                String category = formatLabel(entry.getKey());
                String items = entry.getValue().isEmpty()
                        ? "Niciun element selectat"
                        : String.join(", ", entry.getValue());
                addConfigRow(table, category + ":", items, normalFont);
            }
        }
        return table;
    }

    private static void addDrawingPage(Document document, PdfWriter writer,
                                       com.itextpdf.text.Font titleFont, com.itextpdf.text.Font normalFont) throws DocumentException {
        document.newPage();

        Paragraph drawingTitle = new Paragraph("Desen Tehnic Cabina Dus", titleFont);
        drawingTitle.setAlignment(Element.ALIGN_CENTER);
        drawingTitle.setSpacingAfter(20);
        document.add(drawingTitle);

        try {
            // Obținem content-ul PDF pentru a desena direct
            PdfContentByte canvas = writer.getDirectContent();

            // Dimensiuni pentru zona de desenat
            float pageWidth = document.getPageSize().getWidth();
            float pageHeight = document.getPageSize().getHeight();

            float drawingWidth = pageWidth - 100; // Margini
            float drawingHeight = pageHeight - 200; // Spațiu pentru titlu și descriere

            float startX = 50; // Margine stânga
            float startY = pageHeight - 150; // Începe de sus cu spațiu

            // Creăm un template pentru desen
            PdfTemplate template = canvas.createTemplate(drawingWidth, drawingHeight);
            Graphics2D g2d = template.createGraphics((int)drawingWidth, (int)drawingHeight);

            // Setări de calitate pentru desen
            setupHighQualityGraphics(g2d);

            // Fundal alb
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, (int)drawingWidth, (int)drawingHeight);

            // Desenăm cabina folosind aceleași metode ca în ShowerPreviewPanel
            drawShowerPreviewToPdf(g2d, (int)drawingWidth, (int)drawingHeight);

            g2d.dispose();

            // Adăugăm template-ul în document
            canvas.addTemplate(template, startX, startY - drawingHeight);

            // Descriere
            Paragraph desc = new Paragraph("\n\nPrevizualizare cabina conform configurației selectate", normalFont);
            desc.setAlignment(Element.ALIGN_CENTER);
            document.add(desc);

        } catch (Exception e) {
            Paragraph errorMsg = new Paragraph("Eroare la generarea desenului tehnic: " + e.getMessage(), normalFont);
            document.add(errorMsg);
            e.printStackTrace();
        }
    }

    // ==============================
    //     METODE DE DESENARE CA ÎN SHOWERPREVIEWPANEL
    // ==============================

    private static void drawShowerPreviewToPdf(Graphics2D g2d, int width, int height) {
        DrawingContext context = setupDrawingContext(width, height);
        if (context == null) return;

        switch (Session.selectedCabinaType) {
            case "tipu_1":
            case "tipu_2":
                drawShowerType1_2(g2d, context, true);
                break;
            case "tipu_3":
                drawShowerType3(g2d, context, true);
                break;
            case "tipu_4":
                drawShowerType4(g2d, context, true);
                break;
            case "tipu_5":
            case "tipu_6":
                drawShowerType5_6(g2d, context, true);
                break;
            default:
                drawShowerType1_2(g2d, context, true);
        }
    }

    private static DrawingContext setupDrawingContext(int width, int height) {
        int margin = 40;
        int availableWidth = width - 2 * margin;
        int availableHeight = height - 2 * margin;

        int numGlasses = getNumberOfGlasses();
        double[] dimensions = parseDimensions(numGlasses);
        if (dimensions == null) return null;

        double maxHeight = getMaxHeight(dimensions);
        double totalWidth = getTotalWidth(dimensions);
        double scale = Math.min(availableWidth / totalWidth, availableHeight / maxHeight) * 0.7;

        int startX = margin + (int)((availableWidth - totalWidth * scale) / 2);
        int startY = margin + (int)((availableHeight - maxHeight * scale) / 2);

        return new DrawingContext(dimensions, numGlasses, scale, startX, startY);
    }

    private static void drawShowerType1_2(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
        drawHardwareType1_2(g2d, context, isForPdf);
        drawAllDimensions(g2d, context, isForPdf);
    }

    private static void drawShowerType3(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
        drawHardwareType3(g2d, context, isForPdf);
        drawAllDimensions(g2d, context, isForPdf);
    }

    private static void drawShowerType4(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
        drawHardwareType4(g2d, context, isForPdf);
        drawAllDimensions(g2d, context, isForPdf);
    }

    private static void drawShowerType5_6(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        drawAllGlasses(g2d, context, isForPdf);
        drawHardwareType5_6(g2d, context, isForPdf);
        drawAllDimensions(g2d, context, isForPdf);
    }

    private static void drawAllGlasses(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        for (int i = 0; i < context.numGlasses; i++) {
            GlassPosition pos = getGlassPosition(context, i);
            if (i == 0 && Session.selectedCabinaType.equals("tipu_4")) {
                drawObliqueGlass(g2d, pos, isForPdf);
            } else {
                drawStandardGlass(g2d, pos, i + 1, isForPdf);
            }
        }
    }

    private static void drawObliqueGlass(Graphics2D g2d, GlassPosition pos, boolean isForPdf) {
        Color glassColor = getGlassColorForPdf(isForPdf);

        DrawingContext context = setupDrawingContext(g2d.getClipBounds().width, g2d.getClipBounds().height);
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

            // Desenăm geamul
            g2d.setColor(glassColor);
            g2d.fillPolygon(xPoints, yPoints, 4);

            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawPolygon(xPoints, yPoints, 4);

            // Restaurăm transformarea înainte de a desena textul
            g2d.setTransform(oldTransform);

            drawObliqueGlassDetails(g2d, pos, perspectiveOffset, middleHeight, isForPdf);
        }
    }

    private static void drawObliqueGlassDetails(Graphics2D g2d, GlassPosition pos, int perspectiveOffset, int glassHeight, boolean isForPdf) {
        if (Session.sticlaGrosime != null && !Session.sticlaGrosime.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String thicknessText = "Grosime: " + Session.sticlaGrosime + " mm";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(thicknessText);

            int centerX = pos.x + pos.width/2;
            int centerY = pos.y + glassHeight/2 - perspectiveOffset/2;
            g2d.drawString(thicknessText, centerX - textWidth/2, centerY);
        }

        if (Session.sticlaTip != null && !Session.sticlaTip.isEmpty() &&
                Session.sticlaNume != null && !Session.sticlaNume.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            String nameText = Session.sticlaNume;
            String typeText = getShortGlassType(Session.sticlaTip);
            String fullText = nameText + " - " + typeText;

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(fullText);

            int textX = pos.x + pos.width/2 - textWidth/2;
            int textY = pos.y + glassHeight - perspectiveOffset/2 - 10;
            g2d.drawString(fullText, textX, textY);
        }
    }

    private static void drawStandardGlass(Graphics2D g2d, GlassPosition pos, int glassNumber, boolean isForPdf) {
        Color glassColor = getGlassColorForPdf(isForPdf);

        g2d.setColor(glassColor);
        g2d.fillRect(pos.x, pos.y, pos.width, pos.height);

        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(pos.x, pos.y, pos.width, pos.height);

        drawGlassDetails(g2d, pos, glassNumber, isForPdf);
    }

    private static void drawGlassDetails(Graphics2D g2d, GlassPosition pos, int glassNumber, boolean isForPdf) {
        if (Session.sticlaGrosime != null && !Session.sticlaGrosime.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String thicknessText = "Grosime: " + Session.sticlaGrosime + " mm";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(thicknessText);
            g2d.drawString(thicknessText, pos.x + pos.width/2 - textWidth/2, pos.y + 20);
        }

        if (Session.sticlaTip != null && !Session.sticlaTip.isEmpty() &&
                Session.sticlaNume != null && !Session.sticlaNume.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            String nameText = Session.sticlaNume;
            String typeText = getShortGlassType(Session.sticlaTip);
            String fullText = nameText + " - " + typeText;

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(fullText);
            g2d.drawString(fullText, pos.x + pos.width/2 - textWidth/2, pos.y + pos.height - 10);
        }
    }

    private static void drawHardwareType1_2(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColorForPdf(isForPdf);
        GlassPosition pos1 = getGlassPosition(context, 0);

        drawHinges(g2d, pos1.x, pos1.y, pos1.width, pos1.height, pos1.glassHeight,
                hardwareColor, isForPdf, HingePosition.LEFT);
        drawHandle(g2d, pos1.x, pos1.y, pos1.width, pos1.height, hardwareColor, isForPdf, HandlePosition.RIGHT);
    }

    private static void drawHardwareType3(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColorForPdf(isForPdf);
        GlassPosition pos2 = getGlassPosition(context, 1);
        GlassPosition pos3 = getGlassPosition(context, 2);

        drawHandle(g2d, pos2.x, pos2.y, pos2.width, pos2.height, hardwareColor, isForPdf, HandlePosition.RIGHT);
        drawSplitHinges(g2d, pos2, pos3, pos2.glassHeight, hardwareColor, isForPdf);
    }

    private static void drawHardwareType4(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColorForPdf(isForPdf);
        GlassPosition pos2 = getGlassPosition(context, 1);
        GlassPosition pos3 = getGlassPosition(context, 2);

        drawHandle(g2d, pos2.x, pos2.y, pos2.width, pos2.height, hardwareColor, isForPdf, HandlePosition.RIGHT);
        drawSplitHinges(g2d, pos2, pos3, pos2.glassHeight, hardwareColor, isForPdf);
    }

    private static void drawHardwareType5_6(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        Color hardwareColor = getHardwareColorForPdf(isForPdf);

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

    private static void drawHinges(Graphics2D g2d, int x, int y, int width, int height, double glassHeightMeters,
                                   Color hardwareColor, boolean isForPdf, HingePosition position) {
        g2d.setColor(hardwareColor);

        double offsetRatio = 0.15 / glassHeightMeters;
        int offsetPixels = (int)(height * offsetRatio);
        int hingeWidth = Math.max(5, width / 20);
        int hingeHeight = Math.max(8, height / 40);

        int hingeX = position == HingePosition.LEFT ? x : x + width - hingeWidth;

        int hingeY1 = y + offsetPixels - hingeHeight / 2;
        g2d.fillRect(hingeX, hingeY1, hingeWidth, hingeHeight);

        int hingeY2 = y + height - offsetPixels - hingeHeight / 2;
        g2d.fillRect(hingeX, hingeY2, hingeWidth, hingeHeight);
    }

    private static void drawSplitHinges(Graphics2D g2d, GlassPosition leftGlass, GlassPosition rightGlass,
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

    private static void drawSplitHingesBetweenGlasses(Graphics2D g2d, GlassPosition leftGlass, GlassPosition rightGlass,
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

    private static void drawHandle(Graphics2D g2d, int x, int y, int width, int height,
                                   Color hardwareColor, boolean isForPdf, HandlePosition position) {
        g2d.setColor(hardwareColor);

        int handleSize = Math.min(25, Math.min(width, height) / 4);
        int handleX = position == HandlePosition.LEFT ? x + 5 : x + width - handleSize - 5;
        int handleY = y + (height - handleSize) / 2;

        g2d.fillOval(handleX, handleY, handleSize, handleSize);

        g2d.setColor(hardwareColor.darker());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(handleX, handleY, handleSize, handleSize);
    }

    private static void drawAllDimensions(Graphics2D g2d, DrawingContext context, boolean isForPdf) {
        for (int i = 0; i < context.numGlasses; i++) {
            GlassPosition pos = getGlassPosition(context, i);
            if (i == 0 && Session.selectedCabinaType.equals("tipu_4")) {
                drawObliqueGlassDimensions(g2d, pos, isForPdf);
            } else {
                drawGlassDimensions(g2d, pos, i + 1, context.numGlasses, isForPdf);
            }
        }
    }

    private static void drawObliqueGlassDimensions(Graphics2D g2d, GlassPosition pos, boolean isForPdf) {
        DrawingContext context = setupDrawingContext(g2d.getClipBounds().width, g2d.getClipBounds().height);
        if (context == null || context.numGlasses <= 1) return;

        GlassPosition middleGlass = getGlassPosition(context, 1);
        int glassHeight = middleGlass.height;
        int perspectiveOffset = (int)(glassHeight * 0.3);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        int heightMm = (int)(middleGlass.glassHeight * 1000);
        int widthMm = (int)(pos.glassWidth * 1000);

        FontMetrics fm = g2d.getFontMetrics();

        // Înălțimea (partea stângă)
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

        // Lățimea (partea de jos)
        String widthText = widthMm + " mm";
        int widthTextWidth = fm.stringWidth(widthText);

        int lineY = pos.y + glassHeight + 15;
        g2d.drawLine(pos.x, lineY, pos.x + pos.width, lineY);
        g2d.drawLine(pos.x, lineY - 3, pos.x, lineY + 3);
        g2d.drawLine(pos.x + pos.width, lineY - 3, pos.x + pos.width, lineY + 3);
        g2d.drawString(widthText, pos.x + pos.width/2 - widthTextWidth/2, lineY + 15);
    }

    private static void drawGlassDimensions(Graphics2D g2d, GlassPosition pos, int glassNumber,
                                            int totalGlasses, boolean isForPdf) {
        g2d.setColor(Color.BLACK);
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

    private static void drawHeightDimension(Graphics2D g2d, int lineX, int y, int height,
                                            String text, int textWidth, int textOffset) {
        g2d.drawLine(lineX, y, lineX, y + height);
        g2d.drawLine(lineX - 3, y, lineX + 3, y);
        g2d.drawLine(lineX - 3, y + height, lineX + 3, y + height);

        AffineTransform old = g2d.getTransform();
        g2d.rotate(-Math.PI/2, lineX + textOffset, y + height/2);
        g2d.drawString(text, lineX + textOffset - textWidth/2, y + height/2);
        g2d.setTransform(old);
    }

    private static void drawWidthDimension(Graphics2D g2d, int x, int lineY, int width, int widthMm, FontMetrics fm) {
        String widthText = widthMm + " mm";
        int textWidth2 = fm.stringWidth(widthText);

        g2d.drawLine(x, lineY, x + width, lineY);
        g2d.drawLine(x, lineY - 3, x, lineY + 3);
        g2d.drawLine(x + width, lineY - 3, x + width, lineY + 3);
        g2d.drawString(widthText, x + width/2 - textWidth2/2, lineY + 15);
    }

    // ==============================
    //     METODE HELPER PENTRU DESENARE
    // ==============================

    private static GlassPosition getGlassPosition(DrawingContext context, int index) {
        double glassWidth = context.dimensions[index * 2 + 1];
        double glassHeight = context.dimensions[index * 2];
        int drawWidth = (int)(glassWidth * context.scale);
        int drawHeight = (int)(glassHeight * context.scale);
        int x = context.startX + (int)(getCumulativeWidth(context.dimensions, index) * context.scale);

        return new GlassPosition(x, context.startY, drawWidth, drawHeight, glassHeight, glassWidth, context.scale);
    }

    private static void setupHighQualityGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private static int getNumberOfGlasses() {
        if (Session.selectedCabinaType == null) return 2;
        return switch (Session.selectedCabinaType) {
            case "tipu_1", "tipu_2" -> 2;
            case "tipu_3", "tipu_4" -> 3;
            case "tipu_5", "tipu_6" -> 4;
            default -> 2;
        };
    }

    private static double[] parseDimensions(int numGlasses) {
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

    private static double getMaxHeight(double[] dimensions) {
        double max = 0;
        for (int i = 0; i < dimensions.length; i += 2) {
            max = Math.max(max, dimensions[i]);
        }
        return max;
    }

    private static double getTotalWidth(double[] dimensions) {
        double total = 0;
        for (int i = 1; i < dimensions.length; i += 2) {
            total += dimensions[i];
        }
        return total;
    }

    private static double getCumulativeWidth(double[] dimensions, int index) {
        double cumulative = 0;
        for (int i = 0; i < index; i++) {
            cumulative += dimensions[i * 2 + 1];
        }
        return cumulative;
    }

    private static String getShortGlassType(String fullType) {
        if (fullType == null) return "";
        if (fullType.contains("Securizata")) return "Securiz.";
        if (fullType.contains("Simpla")) return "Simpla";
        return fullType.length() > 8 ? fullType.substring(0, 8) : fullType;
    }

    private static Color getGlassColorForPdf(boolean isForPdf) {
        if (Session.sticlaNume != null) {
            switch (Session.sticlaNume.toLowerCase()) {
                case "dark grey":
                    return new Color(60, 60, 80);
                case "bronze":
                    return new Color(185, 107, 30);
                case "clear":
                    return new Color(220, 220, 240);
                default:
                    return new Color(200, 200, 220);
            }
        }
        return new Color(200, 200, 220);
    }

    private static Color getHardwareColorForPdf(boolean isForPdf) {
        switch (Session.finisajID) {
            case 1: return new Color(150, 150, 150);
            case 2: return new Color(180, 180, 180);
            case 3: return Color.BLACK;
            case 4: return new Color(200, 200, 200);
            case 5: return new Color(205, 175, 0);
            case 6: return new Color(205, 152, 163);
            default: return new Color(150, 150, 150);
        }
    }

    // ==============================
    //     CLASE INTERNE PENTRU DESENARE
    // ==============================

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

    // ==============================
    //     RESTUL METODELOR (RĂMÂN NESCHIMBATE)
    // ==============================

    private static void addPricePage(Document document, double pretEuro, double pretFinalEuro, double pretFinalRon, double curs,
                                     com.itextpdf.text.Font titleFont, com.itextpdf.text.Font headerFont,
                                     com.itextpdf.text.Font boldFont, com.itextpdf.text.Font normalFont,
                                     com.itextpdf.text.Font priceFont) throws DocumentException {
       // document.newPage();

        Paragraph priceTitle = new Paragraph("Detalii Pret", titleFont);
        priceTitle.setAlignment(Element.ALIGN_CENTER);
        priceTitle.setSpacingAfter(20);
        document.add(priceTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setSpacingAfter(10);

        addTableHeader(table, "Detalii Pret (curs EUR/RON: " + String.format("%.4f", curs) + ")", headerFont, 2);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        addPriceRow(table, "Pret productie (EUR):", nf.format(pretEuro) + " EUR", boldFont);
        addPriceRow(table, "Adaos comercial (" + Settings.getAdaosComercial() + "%):",
                nf.format(pretEuro * (Settings.getAdaosComercial() / 100)) + " EUR", normalFont);
        addPriceRow(table, "TVA (" + Settings.getTVA() + "%):",
                nf.format(pretFinalEuro - (pretEuro * (1 + Settings.getAdaosComercial() / 100))) + " EUR", normalFont);

        addFinalPriceRows(table, pretFinalEuro, pretFinalRon, nf, priceFont);
        document.add(table);

    }

    private static void addFinalPriceRows(PdfPTable table, double pretFinalEuro, double pretFinalRon, NumberFormat nf, com.itextpdf.text.Font priceFont) {
        PdfPCell totalEuroLabel = new PdfPCell(new Phrase("PRET FINAL (EUR):", priceFont));
        totalEuroLabel.setBorder(PdfPCell.NO_BORDER);
        table.addCell(totalEuroLabel);

        PdfPCell totalEuroValue = new PdfPCell(new Phrase(nf.format(pretFinalEuro) + " EUR", priceFont));
        totalEuroValue.setBorder(PdfPCell.NO_BORDER);
        totalEuroValue.setBackgroundColor(new BaseColor(255, 235, 238));
        table.addCell(totalEuroValue);

        PdfPCell totalRonLabel = new PdfPCell(new Phrase("PRET FINAL (RON):", priceFont));
        totalRonLabel.setBorder(PdfPCell.NO_BORDER);
        table.addCell(totalRonLabel);

        PdfPCell totalRonValue = new PdfPCell(new Phrase(nf.format(pretFinalRon) + " RON", priceFont));
        totalRonValue.setBorder(PdfPCell.NO_BORDER);
        totalRonValue.setBackgroundColor(new BaseColor(255, 235, 238));
        table.addCell(totalRonValue);
    }



    private static void addTableHeader(PdfPTable table, String title, com.itextpdf.text.Font font, int colspan) {
        PdfPCell header = new PdfPCell(new Phrase(title, font));
        header.setBackgroundColor(new BaseColor(33, 150, 243));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setColspan(colspan);
        header.setPadding(8);
        table.addCell(header);
    }

    private static void addConfigRow(PdfPTable table, String label, String value, com.itextpdf.text.Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBackgroundColor(new BaseColor(245, 245, 245));
        labelCell.setPadding(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", font));
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }

    private static void addPriceRow(PdfPTable table, String label, String value, com.itextpdf.text.Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(PdfPCell.NO_BORDER);
        labelCell.setPadding(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBorder(PdfPCell.NO_BORDER);
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }

    private static String getCabinaTypeName(String type) {
        if (type == null) return "Neselectat";
        return switch (type) {
            case "tipu_1" -> "Tip 1 - Cabina simplă (2 sticle)";
            case "tipu_2" -> "Tip 2 - Cabina cu ușă (2 sticle)";
            case "tipu_3" -> "Tip 3 - Cabina colț (3 sticle)";
            case "tipu_4" -> "Tip 4 - Cabina colț cu ușă (3 sticle)";
            case "tipu_5" -> "Tip 5 - Cabina walk-in (4 sticle)";
            case "tipu_6" -> "Tip 6 - Cabina walk-in cu ușă (4 sticle)";
            default -> type;
        };
    }

    private static String getFinishName(int finishId) {
        return switch (finishId) {
            case 1 -> "Satin";
            case 2 -> "Lucios";
            case 3 -> "Negru";
            case 4 -> "Alb";
            case 5 -> "Gold";
            case 6 -> "Rose Gold";
            default -> "Neselectat";
        };
    }

    private static String formatLabel(String key) {
        if (key == null) return "";
        return switch (key) {
            case "manere_buton" -> "Mânere Buton";
            case "manere_diverse" -> "Mânere Diverse";
            case "profile_rigidizare_si_conectori" -> "Profile Rigidizare și Conectori";
            case "balamale" -> "Balamale";
            case "garnituri" -> "Garnituri";
            case "profile" -> "Profile";
            default -> Character.toUpperCase(key.charAt(0)) + key.substring(1).replace('_', ' ');
        };
    }
}