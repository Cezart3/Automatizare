package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import db.DatabaseManager;
import db.GlassDAO;
import db.ProductDAO;
import model.glass.Glass;
import org.apache.commons.cli.CommandLine;
import util.api.ExchangeRateAPI;
import util.calculator.PriceCalculator;
import util.config.Settings;
import util.export.PdfExporter;
import util.session.Session;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class HeadlessRunner {
    public static void run(CommandLine cmd) {
        String jsonPath = cmd.getOptionValue("j");
        String outPath = cmd.getOptionValue("o");
        if (jsonPath == null || outPath == null) {
            System.err.println("Missing --json or --output arguments for headless mode.");
            System.exit(1);
        }

        try (FileReader reader = new FileReader(jsonPath)) {
            Gson gson = new Gson();
            JsonObject data = gson.fromJson(reader, JsonObject.class);

            // Populate Session
            if (data.has("tipCabina")) Session.selectedCabinaType = data.get("tipCabina").getAsString();
            if (data.has("productType")) Session.selectedProductType = data.get("productType").getAsString();
            if (data.has("material")) Session.material = data.get("material").getAsString();
            if (data.has("finisajID")) Session.finisajID = data.get("finisajID").getAsInt();
            if (data.has("dimensiuni")) Session.dimensiuni = data.get("dimensiuni").getAsString();
            
            // Complex objects
            if (data.has("hardware")) Session.hardware = gson.fromJson(data.get("hardware"), new TypeToken<Map<String, String>>() {}.getType());
            if (data.has("hardwareMulti")) Session.hardwareMulti = gson.fromJson(data.get("hardwareMulti"), new TypeToken<Map<String, List<String>>>() {}.getType());
            if (data.has("hardwareQuantities")) Session.hardwareQuantities = gson.fromJson(data.get("hardwareQuantities"), new TypeToken<Map<String, Map<String, Integer>>>() {}.getType());
            if (data.has("sticlaFormaSablonMap")) Session.sticlaFormaSablonMap = gson.fromJson(data.get("sticlaFormaSablonMap"), new TypeToken<Map<Integer, String>>() {}.getType());
            
            // Sticla
            if (data.has("sticlaNume")) Session.sticlaNume = data.get("sticlaNume").getAsString();
            if (data.has("sticlaGrosime")) Session.sticlaGrosime = data.get("sticlaGrosime").getAsString();
            if (data.has("sticlaTip")) Session.sticlaTip = data.get("sticlaTip").getAsString();
            if (data.has("sticlaGaurire")) Session.sticlaGaurire = data.get("sticlaGaurire").getAsString();
            if (data.has("sticlaForma")) Session.sticlaForma = data.get("sticlaForma").getAsString();
            if (data.has("sticlaNumarGauririExtra")) Session.sticlaNumarGauririExtra = data.get("sticlaNumarGauririExtra").getAsString();
            if (data.has("sticlaNumarDecupajeExtra")) Session.sticlaNumarDecupajeExtra = data.get("sticlaNumarDecupajeExtra").getAsString();
            if (data.has("sticlaDecupajeExtra")) Session.sticlaDecupajeExtra = data.get("sticlaDecupajeExtra").getAsString();

            // Setup DB & Settings
            DatabaseManager.getConnection();
            ProductDAO productDAO = new ProductDAO();
            GlassDAO glassDAO = new GlassDAO();
            List<Glass> sticle = glassDAO.getAllGlasses();

            // Calculate price
            double curs = ExchangeRateAPI.getEuroToRon();
            double pretEuro = PriceCalculator.calculeaza(sticle, productDAO);
            
            double adaosPercent = Settings.getAdaosComercial();
            double adaosComercialEuro = pretEuro * (adaosPercent / 100);
            double pretCuAdaosEuro = pretEuro + adaosComercialEuro;
            
            // Add TVA
            double tvaPercent = Settings.getTVA();
            double valoareTvaEuro = pretCuAdaosEuro * (tvaPercent / 100);
            double pretFinalEuro = pretCuAdaosEuro + valoareTvaEuro;
            double pretFinalRon = pretFinalEuro * curs;

            // Generate PDF
            File outFile = new File(outPath);
            PdfExporter.exportToPdf(outFile, null, pretEuro, pretFinalEuro, pretFinalRon, curs);
            
            System.out.println("PDF_GENERATED_AT: " + outFile.getAbsolutePath());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
