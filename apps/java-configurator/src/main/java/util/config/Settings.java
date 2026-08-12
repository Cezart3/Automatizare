package util.config;

import java.util.prefs.Preferences;

public class Settings {
    private static final Preferences prefs = Preferences.userNodeForPackage(Settings.class);

    // Chei pentru setări
    private static final String KEY_TVA = "tva_percent";
    private static final String KEY_ADAOS_COMERCIAL = "adaos_comercial_percent";
    private static final String KEY_PRET_DECUPAJ = "pret_decupaj"; // NOU
    private static final String KEY_USERNAME = "username"; // NOU
    private static final String KEY_PASSWORD = "password"; // NOU


    // Valori default
    private static final double DEFAULT_TVA = 19.0; // 19% TVA
    private static final double DEFAULT_ADAOS_COMERCIAL = 0.0; // 0% adaos comercial
    private static final double DEFAULT_PRET_DECUPAJ = 19.0; // NOU: 19 EUR pentru decupaj
    private static final String DEFAULT_USERNAME ="1"; // 19% TVA
    private static final String DEFAULT_PASSWORD = "1"; // 19% TVA

    public static void setUsername(String username) {
        prefs.put(KEY_USERNAME, username);
    }
    public static void setPassword(String password) {
        prefs.put(KEY_PASSWORD, password);
    }
    public static String getUsername() {
        return prefs.get(KEY_USERNAME, DEFAULT_USERNAME);
    }
    public static String getPassword() {
        return prefs.get(KEY_PASSWORD, DEFAULT_PASSWORD);
    }

    public static double getTVA() {
        return prefs.getDouble(KEY_TVA, DEFAULT_TVA);
    }


    public static void setTVA(double tva) {
        prefs.putDouble(KEY_TVA, tva);
    }

    public static double getAdaosComercial() {
        return prefs.getDouble(KEY_ADAOS_COMERCIAL, DEFAULT_ADAOS_COMERCIAL);
    }

    public static void setAdaosComercial(double adaos) {
        prefs.putDouble(KEY_ADAOS_COMERCIAL, adaos);
    }

    // NOU: Metode pentru prețul decupajului
    public static double getPretDecupaj() {
        return prefs.getDouble(KEY_PRET_DECUPAJ, DEFAULT_PRET_DECUPAJ);
    }

    public static void setPretDecupaj(double pret) {
        prefs.putDouble(KEY_PRET_DECUPAJ, pret);
    }

}