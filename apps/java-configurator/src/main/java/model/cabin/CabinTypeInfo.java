package model.cabin;

import java.util.HashMap;
import java.util.Map;

/**
 * Clasa care descrie un stereotip de cabină de duș.
 * Păstrează feroneria grupată pe categorii și lungimile de profile.
 */
public class CabinTypeInfo {

    private final String key; // ex: "tipu_1"
    private final Map<String, Map<String, Integer>> feronerieByCategory; // categorie -> (codProdus -> cantitate)
    private final Map<String, Integer> profileLength; // profil -> lungime in metri

    public CabinTypeInfo(String key) {
        this.key = key;
        this.feronerieByCategory = new HashMap<>();
        this.profileLength = new HashMap<>();
    }

    public String getKey() {
        return key;
    }

    public Map<String, Map<String, Integer>> getFeronerieByCategory() {
        return feronerieByCategory;
    }

    public Map<String, Integer> getProfileLength() {
        return profileLength;
    }

    /**
     * Adaugă o piesă de feronerie într-o categorie.
     */
    public void addFeronerie(String category, String codProdus, int cantitate) {
        feronerieByCategory
                .computeIfAbsent(category, k -> new HashMap<>())
                .put(codProdus, cantitate);
    }

    /**
     * Adaugă un profil și lungimea sa totală (în metri).
     */
    public void addProfileLength(String profil, int length) {
        profileLength.put(profil, length);
    }

    @Override
    public String toString() {
        return "CabinTypeInfo{" +
                "key='" + key + '\'' +
                ", feronerieByCategory=" + feronerieByCategory +
                ", profileLength=" + profileLength +
                '}';
    }
}
