// src/util/GlassDimensionCalculator.java
package util.calculator;

import model.cabin.CabinTypeInfo;
import model.cabin.CabinTypes;
import util.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clasa care calculează dimensiunile FINALE ale sticlelor după aplicarea scăderilor
 * pentru balamale, garnituri și profile.
 * Valorile sunt aproximative — le vei ajusta ulterior în funcție de furnizori.
 */
public class GlassDimensionCalculator {

    // === SCĂDERI STANDARD (în milimetri) – le vei ajusta tu ulterior ===
    private static final Map<String, Double> BALAMA_SCADERE = Map.of(
            "SH301", 10.0,   // 10mm pe fiecare parte (total 20mm pe lățime/înălțime)
            "SH303", 12.0,
            "SH304", 12.0,
            "SH305", 15.0
            // adaugă alte balamale pe viitor
    );

    private static final Map<String, Double> GARNITURA_SCADERE = Map.of(
            "S01", 2.0,   // garnitură magnetică – 2mm pe latură
            "S02", 2.0,
            "S10", 2.0,
            "S11", 3.0,   // garnitură cu finisaj – mai groasă
            "S12", 3.0,
            "K01", 4.0,   // garnituri speciale
            "K02", 4.0
    );

    private static final Map<String, Double> PROFIL_SCADERE = Map.of(
            "U20", 20.0,   // profil U – 20mm pe latură
            "GPU", 25.0,   // profil GPU – 25mm
            "GT02-304", 15.0,
            "C34", 10.0,
            "C35", 10.0,
            "C36", 12.0
    );

    /**
     * Calculează dimensiunile finale (după tăiere) pentru fiecare sticlă
     * @param inaltimi lista cu înălțimile introduse de utilizator (în mm)
     * @param latimi lista cu lățimile introduse de utilizator (în mm)
     * @return mapă: index sticlă → dimensiuni finale (înălțime × lățime)
     */
    public static Map<Integer, String> calculeazaDimensiuniFinale(List<Double> inaltimi, List<Double> latimi) {
        Map<Integer, String> rezultat = new HashMap<>();

        CabinTypeInfo cabina = CabinTypes.get(Session.selectedCabinaType);
        if (cabina == null) {
            // fallback: fără scăderi
            for (int i = 0; i < inaltimi.size(); i++) {
                rezultat.put(i, format(inaltimi.get(i), latimi.get(i)));
            }
            return rezultat;
        }

        // Colectăm toate scăderile pe înălțime și lățime
        double scadereInaltime = 0.0;
        double scadereLatime = 0.0;

        // Balamale – de obicei pe înălțime
        for (var entry : cabina.getFeronerieByCategory().getOrDefault("balamale", Map.of()).entrySet()) {
            String cod = entry.getKey();
            if (isProductSelected("balamale", cod)) {
                scadereInaltime += BALAMA_SCADERE.getOrDefault(cod, 10.0) * 2; // pe ambele părți
            }
        }

        // Garnituri – pe ambele laturi
        for (var entry : cabina.getFeronerieByCategory().getOrDefault("garnituri", Map.of()).entrySet()) {
            String cod = entry.getKey();
            if (isProductSelected("garnituri", cod)) {
                double scadere = GARNITURA_SCADERE.getOrDefault(cod, 2.0);
                scadereInaltime += scadere;
                scadereLatime += scadere;
            }
        }

        // Profile – depinde de tip
        for (var entry : cabina.getFeronerieByCategory().getOrDefault("profile", Map.of()).entrySet()) {
            String cod = entry.getKey();
            if (isProductSelected("profile", cod)) {
                double scadere = PROFIL_SCADERE.getOrDefault(cod, 0.0);
                if ("U20".equalsIgnoreCase(cod) || "GPU".equalsIgnoreCase(cod)) {
                    scadereLatime += scadere;  // U20 și GPU sunt pe lățime (jos)
                } else {
                    scadereInaltime += scadere; // alte profile pe înălțime
                }
            }
        }

        for (var entry : cabina.getFeronerieByCategory().getOrDefault("profile_rigidizare_si_conectori", Map.of()).entrySet()) {
            String cod = entry.getKey();
            if (isProductSelected("profile_rigidizare_si_conectori", cod)) {
                double scadere = PROFIL_SCADERE.getOrDefault(cod, 10.0);
                scadereInaltime += scadere * 2; // de obicei pe ambele părți
            }
        }

        // Aplicăm scăderile pe fiecare sticlă
        for (int i = 0; i < inaltimi.size(); i++) {
            double hFinal = inaltimi.get(i) - scadereInaltime;
            double lFinal = latimi.get(i) - scadereLatime;

            // Nu permitem valori negative
            hFinal = Math.max(300, hFinal);
            lFinal = Math.max(300, lFinal);

            rezultat.put(i, format(hFinal, lFinal));
        }

        return rezultat;
    }

    private static boolean isProductSelected(String category, String cod) {
        List<String> selected = Session.hardwareMulti.getOrDefault(category, List.of());
        return selected.contains(cod);
    }

    private static String format(double h, double l) {
        return String.format("%.0f × %.0f mm", h, l);
    }

    // === METODĂ UTILITY: Pentru afișare în UI ===
    public static String getDimensiuniFinaleText() {
        if (Session.dimensiuni == null || Session.dimensiuni.isBlank()) {
            return "Dimensiuni finale: Necunoscute";
        }

        String[] parts = Session.dimensiuni.split("x");
        List<Double> inaltimi = new ArrayList<>();
        List<Double> latimi = new ArrayList<>();

        for (int i = 0; i < parts.length; i += 2) {
            inaltimi.add(Double.parseDouble(parts[i].trim()));
            if (i + 1 < parts.length) {
                latimi.add(Double.parseDouble(parts[i + 1].trim()));
            }
        }

        Map<Integer, String> finale = calculeazaDimensiuniFinale(inaltimi, latimi);

        StringBuilder sb = new StringBuilder("Dimensiuni finale după tăiere:\n");
        finale.forEach((i, dim) -> sb.append("• Sticla ").append(i + 1).append(": ").append(dim).append("\n"));
        return sb.toString();
    }
}