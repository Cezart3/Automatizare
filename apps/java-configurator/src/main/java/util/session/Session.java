package util.session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class Session {
    public static String selectedCabinaType = "";
    public static String material = "";
    public static int finisajID;
    public static String dimensiuni = "";
    public static Map<String, Map<String, Integer>> hardwareQuantities = new HashMap<>();
    // În Session.java – adaugă aceste linii:
    public static String selectedProductType = "";  // "batanta", "panou", "culisanta", "balustrada"
    public static String selectedSubtype = "";      // doar pentru batanta: "tipu_1", "tipu_2" etc.
    // hardware simplu (un element per categorie)
    public static Map<String, String> hardware = new HashMap<>();

    // hardware multi (mai multe elemente per categorie)
    public static Map<String, List<String>> hardwareMulti = new HashMap<>();

    // Step6 - sticla
    public static String sticlaNume = "";
    public static String sticlaGrosime = "";
    public static String sticlaTip = "";
    public static String sticlaGaurire = "";
    public static String sticlaForma = "";

    // NOU: Câmpuri pentru găuriri și decupaje extra
    public static String sticlaNumarGauririExtra = "";
    public static String sticlaNumarDecupajeExtra = "";
    public static String sticlaDecupajeExtra = "";

    // Step4 - forma/sablon per sticla
    public static Map<Integer, String> sticlaFormaSablonMap = new HashMap<>();

    private static final Preferences prefs = Preferences.userNodeForPackage(Session.class);
    private static final Gson gson = new Gson();

    public static void load() {
        selectedCabinaType = prefs.get("tipCabina", "");
        material = prefs.get("material", "");
        finisajID = prefs.getInt("finisajID", 1); // default 1
        dimensiuni = prefs.get("dimensiuni", "");
        String hwJson = prefs.get("hardware", "");
        if (!hwJson.isBlank()) {
            hardware = gson.fromJson(hwJson, new TypeToken<Map<String, String>>() {}.getType());
        }

        String hwMultiJson = prefs.get("hardwareMulti", "");
        if (!hwMultiJson.isBlank()) {
            hardwareMulti = gson.fromJson(hwMultiJson, new TypeToken<Map<String, List<String>>>() {}.getType());
        }

        sticlaNume = prefs.get("sticlaNume", "");
        sticlaGrosime = prefs.get("sticlaGrosime", "");
        sticlaTip = prefs.get("sticlaTip", "");
        sticlaGaurire = prefs.get("sticlaGaurire", "");
        sticlaForma = prefs.get("sticlaForma", "");

        // NOU: Încărcare câmpuri pentru găuriri și decupaje extra
        sticlaNumarGauririExtra = prefs.get("sticlaNumarGauririExtra", "0");
        sticlaDecupajeExtra = prefs.get("sticlaDecupajeExtra", "Fara");

        // Încărcare forma/sablon per sticla
        String formaSablonJson = prefs.get("sticlaFormaSablonMap", "");
        if (!formaSablonJson.isBlank()) {
            sticlaFormaSablonMap = gson.fromJson(formaSablonJson,
                    new TypeToken<Map<Integer, String>>() {}.getType());
        } else {
            sticlaFormaSablonMap = new HashMap<>();
        }
        String hwQuantitiesJson = prefs.get("hardwareQuantities", "");
        if (!hwQuantitiesJson.isBlank()) {
            hardwareQuantities = gson.fromJson(hwQuantitiesJson,
                    new TypeToken<Map<String, Map<String, Integer>>>() {}.getType());
        } else {
            hardwareQuantities = new HashMap<>();
        }
    }

    public static void save() {
        prefs.put("tipCabina", selectedCabinaType);
        prefs.put("material", material);
        prefs.putInt("finisajID", finisajID);
        prefs.put("dimensiuni", dimensiuni);
        prefs.put("hardware", gson.toJson(hardware));
        prefs.put("hardwareMulti", gson.toJson(hardwareMulti));

        prefs.put("sticlaNume", sticlaNume);
        prefs.put("sticlaGrosime", sticlaGrosime);
        prefs.put("sticlaTip", sticlaTip);
        prefs.put("sticlaGaurire", sticlaGaurire);
        prefs.put("sticlaForma", sticlaForma);

        // NOU: Salvare câmpuri pentru găuriri și decupaje extra
        prefs.put("sticlaNumarGauririExtra", sticlaNumarGauririExtra);
        prefs.put("sticlaNumarDecupajeExtra", sticlaNumarDecupajeExtra);
        prefs.put("sticlaDecupajeExtra", sticlaDecupajeExtra);
        prefs.put("hardwareQuantities", gson.toJson(hardwareQuantities));

        prefs.put("sticlaFormaSablonMap", gson.toJson(sticlaFormaSablonMap));
    }
}