package util.calculator;

import db.ProductDAO;
import model.cabin.CabinTypeInfo;
import model.cabin.CabinTypes;
import model.glass.Glass;
import util.config.ProductType;
import util.session.Session;
import util.config.Settings;

import java.util.*;

public class PriceCalculator {

    private static final Set<String> GARNITURI_CU_FINISAJ = Set.of(
            "S11", "S12", "S14",
            "K01", "K02", "K03", "K10", "K15", "K16", "K17", "K18", "K19"
    );

    private static final Set<String> GARNITURI_SPECIAL = Set.of("S01", "S02", "S10");

    // --- Refactorizare calcul sticlă ---
    private static class SticlaInfo {
        double suprafata = 0;
        double perimetru = 0;
        double pretSticlaTotal = 0;
        double slefuireTotal = 0;
        double gaurireTotal = 0;
        double adaosTotal = 0;
        double gauririExtraTotal = 0;
        double decupajeExtraTotal = 0;
        List<Double> suprafeteSticle = new ArrayList<>(); // Suprafața fiecărei sticle individuale
    }

    private static SticlaInfo calculeazaSticla(Glass g, List<Double> inaltimi, List<Double> latimi) {
        SticlaInfo info = new SticlaInfo();

        // Calculăm suprafața și perimetrul pentru fiecare sticlă individual
        for (int i = 0; i < inaltimi.size(); i++) {
            double h = inaltimi.get(i);
            double lat = latimi.get(i);
            double suprafataSticla = h * lat;
            info.suprafata += suprafataSticla;
            info.perimetru += 2 * (h + lat);
            info.suprafeteSticle.add(suprafataSticla);
            System.out.println("🔹 Sticla " + (i + 1) + ": " + h + "m x " + lat + "m = " + suprafataSticla + "m²");
        }

        // Pret sticla de bază
        double pretSticla = "Simpla debitata".equals(Session.sticlaTip) ? g.getSimplaDebitata() : g.getSecurizataCalita();
        double pretSticlaFaraAdaos = info.suprafata * pretSticla;

        // Aplicăm adaosurile pentru formă/sablon pe fiecare sticlă individual
        double pretSticlaCuAdaos = 0;
        for (int i = 0; i < info.suprafeteSticle.size(); i++) {
            double suprafataSticla = info.suprafeteSticle.get(i);
            double pretSticlaIndividual = suprafataSticla * pretSticla;

            // Verificăm dacă sticla are formă sau sablon
            String optiuneSticla = Session.sticlaFormaSablonMap.get(i);
            if ("Formă".equals(optiuneSticla)) {
                double adaosForma = g.getAdaosFormaProc();
                pretSticlaIndividual *= (1 + adaosForma / 100);
                System.out.println("🧩 Aplicat adaos formă (" + adaosForma + "%) pentru sticla " + (i+1) +
                        ": " + (suprafataSticla * pretSticla) + " -> " + pretSticlaIndividual);
            } else if ("Sablon".equals(optiuneSticla)) {
                double adaosSablon = g.getAdaosSablonProc();
                pretSticlaIndividual *= (1 + adaosSablon / 100);
                System.out.println("🧩 Aplicat adaos sablon (" + adaosSablon + "%) pentru sticla " + (i+1) +
                        ": " + (suprafataSticla * pretSticla) + " -> " + pretSticlaIndividual);
            }

            pretSticlaCuAdaos += pretSticlaIndividual;
        }

        info.pretSticlaTotal = pretSticlaCuAdaos;

        System.out.println("🔹 Sticlă: " + g.getTipSticla() + ", tip: " + Session.sticlaTip
                + " | suprafata totala=" + info.suprafata + "m² x pret/unit=" + pretSticla
                + " = " + pretSticlaFaraAdaos + " + adaosuri = " + info.pretSticlaTotal);

        // Slefuire
        info.slefuireTotal = info.perimetru * g.getManoperaSlefuire();
        System.out.println("🛠️ Slefuire: " + g.getManoperaSlefuire() + " x perimetru total " + info.perimetru + "m = " + info.slefuireTotal);

        // Gaurire (valoarea returnata aici este valoarea manoperei; multiplicarile pe articole se fac unde e cazul)
        info.gaurireTotal = switch (Session.sticlaGaurire) {
            case "4-20" -> g.getManoperaGaurire4_20();
            case "21-30" -> g.getManoperaGaurire21_30();
            case "31-60" -> g.getManoperaGaurire31_60_cnc();
            default -> 0;
        };
        System.out.println("✂️ Gaurire: " + info.gaurireTotal);

        // NOU: Calcul găuriri extra
        info.gauririExtraTotal = calculeazaGauririExtra(g);

        // NOU: Calcul decupaje extra
        info.decupajeExtraTotal = calculeazaDecupajeExtra();

        // Eliminăm adaosul global vechi deoarece acum avem adaosuri individuale per sticlă
        info.adaosTotal = 0;

        return info;
    }

    // NOU: Metodă pentru calcul găuriri extra
    private static double calculeazaGauririExtra(Glass g) {
        if (Session.sticlaNumarGauririExtra == null || Session.sticlaNumarGauririExtra.isEmpty()) {
            return 0;
        }

        try {
            int numarGauririExtra = Integer.parseInt(Session.sticlaNumarGauririExtra);
            if (numarGauririExtra <= 0) {
                return 0;
            }

            double pretPerGaura = switch (Session.sticlaGaurire) {
                case "4-20" -> g.getManoperaGaurire4_20();
                case "21-30" -> g.getManoperaGaurire21_30();
                case "31-60" -> g.getManoperaGaurire31_60_cnc();
                default -> 0;
            };

            double totalGauririExtra = numarGauririExtra * pretPerGaura;

            System.out.println("🔩 GĂURIRI EXTRA: " + numarGauririExtra + " găuri x " +
                    pretPerGaura + " lei/gaura = " + totalGauririExtra + " lei");
            System.out.println("   - Tip găurire: " + Session.sticlaGaurire);
            System.out.println("   - Preț per gaură: " + pretPerGaura + " lei");

            return totalGauririExtra;
        } catch (NumberFormatException e) {
            System.err.println("❌ Eroare la parsarea numărului de găuriri extra: " + Session.sticlaNumarGauririExtra);
            return 0;
        }
    }

    // NOU: Metodă pentru calcul decupaje extra - ACTUALIZATĂ să folosească Settings
    private static double calculeazaDecupajeExtra() {
        if (Session.sticlaNumarDecupajeExtra == null || Session.sticlaNumarDecupajeExtra.isEmpty()) {
            return 0;
        }

        try {
            int numarDecupajeExtra = Integer.parseInt(Session.sticlaNumarDecupajeExtra);
            if (numarDecupajeExtra <= 0) {
                return 0;
            }

            // MODIFICARE: Folosim prețul din Settings în loc de constantă hardcodată
            double pretDecupajExtra = Settings.getPretDecupaj();
            double totalDecupajeExtra = numarDecupajeExtra * pretDecupajExtra;

            System.out.println("🔪 DECUPAJE EXTRA: " + numarDecupajeExtra + " decupaje x " +
                    pretDecupajExtra + " lei/decupaj = " + totalDecupajeExtra + " lei");
            System.out.println("   - Preț din setări per decupaj: " + pretDecupajExtra + " lei");

            return totalDecupajeExtra;
        } catch (NumberFormatException e) {
            System.err.println("❌ Eroare la parsarea numărului de decupaje extra: " + Session.sticlaNumarDecupajeExtra);
            return 0;
        }
    }

    // --- Refactorizare calcul feronerie ---
    private static double calculeazaFeronerie(Map<String, Map<String, Integer>> feronerie,
                                              Map<String, Integer> profileLength,
                                              List<Double> inaltimi, List<Double> latimi,
                                              ProductDAO dao, Glass g,
                                              String tipCabina) {
        double total = 0;

        System.out.println("🧩 Feronerie detaliat:");

        // Calcul pentru categorii principale de feronerie
        total += calculeazaCategoriiFeronerie(feronerie, inaltimi, latimi, dao);

        // Calcul pentru profile speciale
        total += calculeazaProfileSpeciale(profileLength, inaltimi, latimi, dao);

        // Calcul pentru operațiuni de găurire și decupe
        total += calculeazaOperatiuniFeronerie(feronerie, g, tipCabina);

        return total;
    }

    private static double calculeazaCategoriiFeronerie(Map<String, Map<String, Integer>> feronerie,
                                                       List<Double> inaltimi, List<Double> latimi,
                                                       ProductDAO dao) {
        double total = 0;

        for (String cat : feronerie.keySet()) {
            double subtotalCat = 0;
            Map<String, Integer> items = feronerie.get(cat);

            for (Map.Entry<String, Integer> e : items.entrySet()) {
                String cod = e.getKey();
                int cant = e.getValue();

                // VERIFICARE CRITICĂ: Folosim doar dacă produsul este selectat
                if (!isProductSelected(cat, cod)) {
                    System.out.println("⏭️  Omitem " + cat + " " + cod + " - produs neselectat");
                    continue; // Sărim peste produsele care nu sunt selectate
                }

                // MODIFICARE: Folosim cantitatea din sesiune dacă există
                int cantitateActuala = getCantitateActuala(cat, cod, cant);

                FeronerieItem item = determinaTipFeronerie(cat, cod, inaltimi, latimi);
                double pretUnit = dao.getPrice(cat, cod, item.materialId, item.finisajId);
                double subtotal = pretUnit * item.lungime * cantitateActuala;

                subtotalCat += subtotal;
                total += subtotal;

                System.out.println((item.specialProfile ? "📏 " : "🧩 ") + cat + " " + cod +
                        ": pret=" + pretUnit + " x lungime=" + item.lungime +
                        " x cantitate=" + cantitateActuala + " (default: " + cant + ")" +
                        " = " + subtotal);
            }
            if (subtotalCat > 0) {
                System.out.println("📌 Subtotal " + cat + " = " + subtotalCat);
            }
        }

        return total;
    }

    private static double calculeazaProfileSpeciale(Map<String, Integer> profileLength,
                                                    List<Double> inaltimi, List<Double> latimi,
                                                    ProductDAO dao) {
        if (profileLength.isEmpty()) {
            return 0;
        }

        double subtotalProfile = 0;
        System.out.println("🧩 Profile speciale din profileLength:");

        for (Map.Entry<String, Integer> e : profileLength.entrySet()) {
            String cod = e.getKey();
            int cant = e.getValue();

            // VERIFICARE CRITICĂ: Folosim doar dacă profilul este selectat
            if (!isProductSelected("profile", cod)) {
                System.out.println("⏭️  Omitem profil " + cod + " - neselectat");
                continue; // Sărim peste profilele care nu sunt selectate
            }

            // MODIFICARE: Folosim cantitatea din sesiune dacă există
            int cantitateActuala = getCantitateActuala("profile", cod, cant);

            FeronerieItem item = determinaTipFeronerie("profile", cod, inaltimi, latimi);
            double pretUnit = dao.getPrice("profile", cod, item.materialId, item.finisajId);

            // MODIFICARE IMPORTANTĂ: Pentru GPU și U20, calculăm lungimea pentru fiecare bucată
            double lungimeTotala;
            if ("GPU".equalsIgnoreCase(cod) || "U20".equalsIgnoreCase(cod)) {
                // Fiecare bucată are lungimea calculată individual
                lungimeTotala = item.lungime * cantitateActuala;
            } else {
                // Pentru alte profile, folosim lungimea standard
                lungimeTotala = item.lungime;
            }

            double subtotal = pretUnit * lungimeTotala;

            subtotalProfile += subtotal;
            System.out.println("📏 Profile " + cod + ": pret=" + pretUnit +
                    " x lungime=" + lungimeTotala + "m (lungime/buc=" + item.lungime + "m x " + cantitateActuala + " buc)" +
                    " = " + subtotal);
        }

        if (subtotalProfile > 0) {
            System.out.println("📌 Subtotal profile speciale = " + subtotalProfile);
        }
        return subtotalProfile;
    }

    // METODĂ NOUĂ: Verifică dacă un produs este selectat în sesiune
    private static boolean isProductSelected(String category, String productCode) {
        List<String> selectedProducts = Session.hardwareMulti.get(category);
        boolean isSelected = selectedProducts != null && selectedProducts.contains(productCode);
        System.out.println("🔍 Verificare selecție " + category + "/" + productCode + ": " + (isSelected ? "SELECTAT" : "NESELECTAT"));
        return isSelected;
    }

    // METODĂ NOUĂ: Obține cantitatea actuală din sesiune
    private static int getCantitateActuala(String categorie, String codProdus, int cantitateImplicita) {
        // Verificăm dacă există cantități modificate în sesiune
        if (Session.hardwareQuantities != null && Session.hardwareQuantities.containsKey(categorie)) {
            Map<String, Integer> cantitatiCategorie = Session.hardwareQuantities.get(categorie);
            if (cantitatiCategorie.containsKey(codProdus)) {
                int cantitateActuala = cantitatiCategorie.get(codProdus);
                System.out.println("🔢 Cantitate actuală " + categorie + "/" + codProdus + ": " + cantitateActuala + " (implicită: " + cantitateImplicita + ")");
                return cantitateActuala;
            }
        }
        System.out.println("🔢 Folosim cantitatea implicită " + categorie + "/" + codProdus + ": " + cantitateImplicita);
        return cantitateImplicita;
    }

    private static double calculeazaOperatiuniFeronerie(Map<String, Map<String, Integer>> feronerie,
                                                        Glass g, String tipCabina) {
        double total = 0;

        // Găurire manere - MODIFICARE: Folosim doar produsele selectate
        int nrManere = 0;
        Map<String, Integer> manere = feronerie.get("manere_buton");
        if (manere != null) {
            for (Map.Entry<String, Integer> entry : manere.entrySet()) {
                String cod = entry.getKey();
                // VERIFICARE: Doar dacă este selectat
                if (isProductSelected("manere_buton", cod)) {
                    int cantitateImplicita = entry.getValue();
                    nrManere += getCantitateActuala("manere_buton", cod, cantitateImplicita);
                }
            }
        }
        double gaurireManereButon = g.getManoperaGaurire21_30() * nrManere;
        System.out.println("✂️ Gaurire manere (21-30) | nr_manere=" + nrManere + " | total=" + gaurireManereButon);
        total += gaurireManereButon;

        // Decupe feronerie - MODIFICARE: Folosim doar produsele selectate
        int nrBalamale = 0;
        Map<String, Integer> balamale = feronerie.get("balamale");
        if (balamale != null) {
            for (Map.Entry<String, Integer> entry : balamale.entrySet()) {
                String cod = entry.getKey();
                // VERIFICARE: Doar dacă este selectat
                if (isProductSelected("balamale", cod)) {
                    int cantitateImplicita = entry.getValue();
                    nrBalamale += getCantitateActuala("balamale", cod, cantitateImplicita);
                }
            }
        }
        double decupeFeronerieTotal = calculeazaDecupeFeronerie(g, tipCabina, nrBalamale);
        total += decupeFeronerieTotal;

        return total;
    }

    private static double calculeazaDecupeFeronerie(Glass g, String tipCabina, int nrBalamale) {
        double manoperaDecupeFeron = g.getManoperaDecupeFeron();

        boolean decupeDublu = switch (tipCabina) {
            case "tipu_3", "tipu_4", "tipu_5", "tipu_6" -> true;
            default -> false;
        };

        if (decupeDublu) {
            System.out.println("ℹ️ Cabina " + tipCabina + " -> decupe feronerie dublate");
            manoperaDecupeFeron *= 2;
        }

        double decupeFeronerieTotal = manoperaDecupeFeron * nrBalamale;
        System.out.println("✂️ Decupe feronerie: " + manoperaDecupeFeron +
                " x nr_balamale=" + nrBalamale + " = " + decupeFeronerieTotal);

        return decupeFeronerieTotal;
    }

    // Clasa helper pentru a grupa datele despre un item de feronerie
    private static class FeronerieItem {
        int materialId;
        int finisajId;
        double lungime;
        boolean specialProfile;

        FeronerieItem(int materialId, int finisajId, double lungime, boolean specialProfile) {
            this.materialId = materialId;
            this.finisajId = finisajId;
            this.lungime = lungime;
            this.specialProfile = specialProfile;
        }
    }

    private static FeronerieItem determinaTipFeronerie(String categoria, String cod,
                                                       List<Double> inaltimi, List<Double> latimi) {
        int materialId;
        int finisajId;
        double lungime = 1;
        boolean specialProfile = false;

        switch (categoria.toLowerCase()) {
            case "garnituri":
                if (GARNITURI_CU_FINISAJ.contains(cod)) {
                    materialId = 1;
                    finisajId = Session.finisajID;
                } else if (GARNITURI_SPECIAL.contains(cod)) {
                    materialId = 10;
                    finisajId = 10;
                } else {
                    materialId = 10;
                    finisajId = 10;
                }
                break;

            case "balamale":
                finisajId = Session.finisajID;
                materialId = (finisajId == 1 || finisajId == 2) ? 1 : 3;
                break;

            case "profile":
                if ("U20".equalsIgnoreCase(cod)) {
                    materialId = 2;
                    finisajId = Session.finisajID;
                    specialProfile = true;
                    // MODIFICARE: Lungimea se calculează pentru fiecare bucată individual
                    lungime = calculeazaLungimeProfila(inaltimi, latimi);
                } else if ("GPU".equalsIgnoreCase(cod)) {
                    materialId = 10;
                    finisajId = 10;
                    specialProfile = true;
                    // MODIFICARE: Lungimea se calculează pentru fiecare bucată individual
                    lungime = calculeazaLungimeProfila(inaltimi, latimi);
                } else {
                    materialId = 3;
                    finisajId = Session.finisajID;
                    lungime = 1; // Lungime standard pentru alte profile
                }
                break;

            case "profile_rigidizare_si_conectori":
                if ("GT02-304".equalsIgnoreCase(cod)) {
                    materialId = 3;
                    finisajId = Session.finisajID;
                    specialProfile = true;
                    lungime = calculeazaLungimeProfila(inaltimi, latimi);
                } else {
                    materialId = 3;
                    finisajId = Session.finisajID;
                    lungime = 1;
                }
                break;

            default:
                materialId = 3;
                finisajId = Session.finisajID;
                lungime = 1;
        }

        return new FeronerieItem(materialId, finisajId, lungime, specialProfile);
    }

    private static double calculeazaLungimeProfila(List<Double> inaltimi, List<Double> latimi) {
        double lungimeMax = 0;
        for (int i = 0; i < inaltimi.size(); i++) {
            lungimeMax = Math.max(lungimeMax, inaltimi.get(i) + latimi.get(i));
        }
        // MODIFICARE: Returnăm lungimea pentru o singură bucată
        // Fiecare bucată va avea această lungime
        return (lungimeMax < 3) ? 3 : (lungimeMax < 6 ? 6 : 9);
    }

    private static List<List<Double>> parseDimensiuni() {
        List<Double> inaltimi = new ArrayList<>();
        List<Double> latimi = new ArrayList<>();
        String[] dim = Session.dimensiuni.split("x");
        for (int i = 0; i < dim.length; i++) {
            double val = Double.parseDouble(dim[i].trim()) / 1000.0;
            if (i % 2 == 0) inaltimi.add(val);
            else latimi.add(val);
        }
        return Arrays.asList(inaltimi, latimi);
    }

    public static double calculeazaTip(String tip, List<Glass> sticle, ProductDAO dao) {
        Glass g = sticle.stream()
                .filter(x -> x.getTipSticla().equals(Session.sticlaNume)
                        && x.getGrosimeMm().equals(Session.sticlaGrosime))
                .findFirst().orElse(null);
        if (g == null) {
            System.err.println("❌ Sticla nu a fost găsită");
            return 0;
        }

        List<Double> inaltimi;
        List<Double> latimi;
        try {
            List<List<Double>> dims = parseDimensiuni();
            inaltimi = dims.get(0);
            latimi = dims.get(1);
        } catch (Exception e) {
            System.err.println("❌ Dimensiuni invalide: " + Session.dimensiuni);
            return 0;
        }

        if (inaltimi.size() != latimi.size()) {
            System.err.println("❌ Număr inegal de înălțimi și lățimi");
            return 0;
        }

        double total = 0;
        System.out.println("===== Încep calcul cabina " + tip + " =====");
        System.out.println("DEBUG: Map forma/sablon: " + Session.sticlaFormaSablonMap);
        System.out.println("DEBUG: Cantități hardware: " + Session.hardwareQuantities);
        System.out.println("DEBUG: Produse selectate (hardwareMulti): " + Session.hardwareMulti);

        SticlaInfo info = calculeazaSticla(g, inaltimi, latimi);
        total += info.pretSticlaTotal + info.slefuireTotal + info.gaurireTotal + info.adaosTotal;

        // NOU: Adăugăm găuririle extra și decupajele extra la total
        total += info.gauririExtraTotal + info.decupajeExtraTotal;

        CabinTypeInfo cabina = CabinTypes.get(tip);

        // MODIFICARE: Obținem feroneria actualizată cu cantitățile din sesiune
        Map<String, Map<String, Integer>> feronerieActualizata = getFeronerieActualizata(cabina);
        Map<String, Integer> profileLengthActualizat = getProfileLengthActualizat(cabina);

        System.out.println("🔧 Feronerie actualizată pentru calcul: " + feronerieActualizata);
        System.out.println("🔧 Profile actualizate pentru calcul: " + profileLengthActualizat);

        total += calculeazaFeronerie(feronerieActualizata, profileLengthActualizat, inaltimi, latimi, dao, g, tip);

        total = Math.round(total * 100.0) / 100.0;

        // NOU: Log final cu detalii complete
        System.out.println("📊 REZUMAT FINAL:");
        System.out.println("   - Sticlă: " + info.pretSticlaTotal + " euro");
        System.out.println("   - Slefuire: " + info.slefuireTotal + " euro");
        System.out.println("   - Găurire: " + info.gaurireTotal + " euro");
        System.out.println("   - Găuriri extra: " + info.gauririExtraTotal + " euro");
        System.out.println("   - Decupaje extra: " + info.decupajeExtraTotal + " euro");
        System.out.println("   - Feronerie: " + (total - info.pretSticlaTotal - info.slefuireTotal - info.gaurireTotal - info.gauririExtraTotal - info.decupajeExtraTotal) + " euro");
        System.out.println("✅ Total cabina " + tip + ": " + total + " euro");
        System.out.println("===== Sfârșit calcul =====");
        return total;
    }

    // METODĂ ACTUALIZATĂ: Include doar produsele selectate
    private static Map<String, Map<String, Integer>> getFeronerieActualizata(CabinTypeInfo cabina) {
        Map<String, Map<String, Integer>> feronerieActualizata = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> entry : cabina.getFeronerieByCategory().entrySet()) {
            String categorie = entry.getKey();
            Map<String, Integer> produse = entry.getValue();
            Map<String, Integer> produseActualizate = new HashMap<>();

            for (Map.Entry<String, Integer> produs : produse.entrySet()) {
                String cod = produs.getKey();
                // VERIFICARE CRITICĂ: Include doar dacă este selectat
                if (isProductSelected(categorie, cod)) {
                    int cantitateImplicita = produs.getValue();
                    int cantitateActuala = getCantitateActuala(categorie, cod, cantitateImplicita);
                    produseActualizate.put(cod, cantitateActuala);
                    System.out.println("✅ Include " + categorie + "/" + cod + " în calcul (cantitate: " + cantitateActuala + ")");
                } else {
                    System.out.println("❌ Exclude " + categorie + "/" + cod + " din calcul (neselectat)");
                }
            }

            if (!produseActualizate.isEmpty()) {
                feronerieActualizata.put(categorie, produseActualizate);
            }
        }

        return feronerieActualizata;
    }

    // METODĂ ACTUALIZATĂ: Include doar profilele selectate
    private static Map<String, Integer> getProfileLengthActualizat(CabinTypeInfo cabina) {
        Map<String, Integer> profileLengthActualizat = new HashMap<>();

        for (Map.Entry<String, Integer> entry : cabina.getProfileLength().entrySet()) {
            String cod = entry.getKey();
            // VERIFICARE CRITICĂ: Include doar dacă este selectat
            if (isProductSelected("profile", cod)) {
                int cantitateImplicita = entry.getValue();
                int cantitateActuala = getCantitateActuala("profile", cod, cantitateImplicita);
                profileLengthActualizat.put(cod, cantitateActuala);
                System.out.println("✅ Include profil " + cod + " în calcul (cantitate: " + cantitateActuala + ")");
            } else {
                System.out.println("❌ Exclude profil " + cod + " din calcul (neselectat)");
            }
        }

        return profileLengthActualizat;
    }

    public static double calculeaza(List<Glass> sticle, ProductDAO dao) {
        if (Session.selectedCabinaType == null || Session.selectedCabinaType.isBlank()) {
            return 0;
        }

        // === LOGICA NOUĂ: Suport complet pentru PANOU, CULISANTĂ, BALUSTRADĂ ===
        ProductType productType = ProductType.fromSession();

        if (productType == ProductType.PANOU) {
            // Panoul se calculează IDENTIC cu tipul 1 (batanta simplă), dar cu o singură sticlă
            return calculeazaTip("tipu_1", sticle, dao);
        }

        // === LOGICA VECHE: Batanta cu subtipuri ===
        return switch (Session.selectedCabinaType) {
            case "tipu_1", "tipu_2" -> calculeazaTip("tipu_1", sticle, dao);
            case "tipu_3", "tipu_4" -> calculeazaTip("tipu_3", sticle, dao);
            case "tipu_5", "tipu_6" -> calculeazaTip("tipu_5", sticle, dao);
            default -> 0;
        };
    }
}