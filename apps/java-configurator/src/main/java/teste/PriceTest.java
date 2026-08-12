package teste;

import db.GlassDAO;
import db.ProductDAO;
import model.glass.Glass;
import util.calculator.PriceCalculator;
import util.session.Session;

import java.util.ArrayList;
import java.util.List;

public class PriceTest {

    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static ProductDAO dao = new ProductDAO();
    private static GlassDAO glassDAO = new GlassDAO();

    public static void main(String[] args) {
        System.out.println("===== START TEST PRICE CALCULATOR =====\n");

        testCabina1_Otel_Gold();
        testCabina1_Zinc_Satin();
        testCabina2_Zinc_Lucios();
        testCabina3_Otel_Gold();
        testCabina5_Otel_RoseGold();

        System.out.println("\n===== REZULTAT FINAL =====");
        System.out.println("Teste rulate: " + testsRun);
        System.out.println("Teste trecute: " + testsPassed);
        System.out.println("Teste căzute: " + (testsRun - testsPassed));
        System.out.println("===== SFARSIT =====");
        System.out.println("Apasa ENTER pentru a iesi...");
        try { System.in.read(); } catch(Exception e) {}

    }

    // --------------------
    private static void testCabina1_Otel_Gold() {
        String testName = "Cabina Tip 1 - Otel, Gold";
        testsRun++;

        List<Glass> sticle = getGlassesExample(2, 1, 0); // nrSticle, nrGauririExtra, nrDecupajeExtra

        Session.selectedCabinaType = "tipu_1";
        Session.sticlaNume = sticle.get(0).getTipSticla();
        Session.sticlaGrosime = sticle.get(0).getGrosimeMm();
        Session.finisajID = 5;
        Session.sticlaTip = "Simpla debitata";
        Session.sticlaFormaSablonMap = new java.util.HashMap<>();
        Session.sticlaNumarGauririExtra = "1";
        Session.sticlaNumarDecupajeExtra = "0";

        double expected = 492.5;
        double actual = PriceCalculator.calculeaza(sticle, dao);

        printTestResult(testName, expected, actual);
    }

    // --------------------
    private static void testCabina1_Zinc_Satin() {
        String testName = "Cabina Tip 1 - Zinc, Satin";
        testsRun++;

        List<Glass> sticle = getGlassesExample(2, 0, 0);

        Session.selectedCabinaType = "tipu_1";
        Session.sticlaNume = sticle.get(0).getTipSticla();
        Session.sticlaGrosime = sticle.get(0).getGrosimeMm();
        Session.finisajID = 1;
        Session.sticlaTip = "Simpla debitata";
        Session.sticlaFormaSablonMap = new java.util.HashMap<>();
        Session.sticlaNumarGauririExtra = "0";
        Session.sticlaNumarDecupajeExtra = "0";

        double expected = 433.0;
        double actual = PriceCalculator.calculeaza(sticle, dao);

        printTestResult(testName, expected, actual);
    }

    // --------------------
    private static void testCabina2_Zinc_Lucios() {
        String testName = "Cabina Tip 2 - Zinc, Lucios";
        testsRun++;

        List<Glass> sticle = getGlassesExample(3, 0, 1);

        Session.selectedCabinaType = "tipu_2";
        Session.sticlaNume = sticle.get(0).getTipSticla();
        Session.sticlaGrosime = sticle.get(0).getGrosimeMm();
        Session.finisajID = 2;
        Session.sticlaTip = "Securizata calita";
        Session.sticlaFormaSablonMap = new java.util.HashMap<>();
        Session.sticlaNumarGauririExtra = "0";
        Session.sticlaNumarDecupajeExtra = "1";

        double expected = 610.0; // Ajustează conform calculelor reale
        double actual = PriceCalculator.calculeaza(sticle, dao);

        printTestResult(testName, expected, actual);
    }

    private static void testCabina3_Otel_Gold() {
        String testName = "Cabina Tip 3 - Otel, Gold";
        testsRun++;

        List<Glass> sticle = getGlassesExample(4, 2, 1);

        Session.selectedCabinaType = "tipu_3";
        Session.sticlaNume = sticle.get(0).getTipSticla();
        Session.sticlaGrosime = sticle.get(0).getGrosimeMm();
        Session.finisajID = 5;
        Session.sticlaTip = "Securizata calita";
        Session.sticlaFormaSablonMap = new java.util.HashMap<>();
        Session.sticlaNumarGauririExtra = "2";
        Session.sticlaNumarDecupajeExtra = "1";

        double expected = 870.0; // Ajustează conform calculelor reale
        double actual = PriceCalculator.calculeaza(sticle, dao);

        printTestResult(testName, expected, actual);
    }

    private static void testCabina5_Otel_RoseGold() {
        String testName = "Cabina Tip 5 - Otel, RoseGold";
        testsRun++;

        List<Glass> sticle = getGlassesExample(5, 3, 2);

        Session.selectedCabinaType = "tipu_5";
        Session.sticlaNume = sticle.get(0).getTipSticla();
        Session.sticlaGrosime = sticle.get(0).getGrosimeMm();
        Session.finisajID = 6;
        Session.sticlaTip = "Simpla debitata";
        Session.sticlaFormaSablonMap = new java.util.HashMap<>();
        Session.sticlaNumarGauririExtra = "3";
        Session.sticlaNumarDecupajeExtra = "2";

        double expected = 1230.0; // Ajustează conform calculelor reale
        double actual = PriceCalculator.calculeaza(sticle, dao);

        printTestResult(testName, expected, actual);
    }

    // --------------------
    private static List<Glass> getGlassesExample(int nrSticle, int nrGauririExtra, int nrDecupajeExtra) {
        List<Glass> allGlasses = glassDAO.getAllGlasses();
        System.out.println("DEBUG: numar sticle gasite in baza: " + allGlasses.size()); // <-- verificare

        List<Glass> sticle = new ArrayList<>();

        for (int i = 0; i < nrSticle && i < allGlasses.size(); i++) {
            Glass g = allGlasses.get(i);
            if (g.getAdaosSablonProc() == null) g.setAdaosSablonProc(0.0);
            if (g.getAdaosFormaProc() == null) g.setAdaosFormaProc(0.0);
            sticle.add(g);
        }

        Session.sticlaNumarGauririExtra = String.valueOf(nrGauririExtra);
        Session.sticlaNumarDecupajeExtra = String.valueOf(nrDecupajeExtra);

        System.out.println("DEBUG: numar sticle folosite in test: " + sticle.size()); // <-- verificare

        return sticle;
    }




    private static void printTestResult(String testName, double expected, double actual) {
        boolean passed = Math.abs(expected - actual) < 0.01;
        if (passed) testsPassed++;

        System.out.println("--------------------------------------------------");
        System.out.println("Test: " + testName);
        System.out.println("Pret expected: " + expected);
        System.out.println("Pret calculat: " + actual);
        System.out.println("Rezultat: " + (passed ? "PASS ✅" : "FAIL ❌"));
        System.out.println("--------------------------------------------------\n");
    }
}
