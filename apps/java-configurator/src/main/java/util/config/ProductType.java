// src/util/ProductType.java
package util.config;

import util.session.Session;

public enum ProductType {
    BATANTA("batanta", true, "tipu_", 2, 4),
    PANOU("panou", false, null, 1, 1),
    CULISANTA("culisanta", false, null, 2, 3),     // exemplu viitor
    BALUSTRADA("balustrada", false, null, 1, 1);   // exemplu viitor

    private final String key;
    private final boolean hasSubtypes;
    private final String subtypePrefix; // ex: "tipu_"
    private final int minGlasses;
    private final int maxGlasses;

    ProductType(String key, boolean hasSubtypes, String subtypePrefix, int minGlasses, int maxGlasses) {
        this.key = key;
        this.hasSubtypes = hasSubtypes;
        this.subtypePrefix = subtypePrefix;
        this.minGlasses = minGlasses;
        this.maxGlasses = maxGlasses;
    }

    public String getKey() { return key; }
    public boolean hasSubtypes() { return hasSubtypes; }
    public String getSubtypePrefix() { return subtypePrefix; }
    public int getMinGlasses() { return minGlasses; }
    public int getMaxGlasses() { return maxGlasses; }

    public static ProductType fromKey(String key) {
        if (key == null) return null;
        for (ProductType type : values()) {
            if (type.key.equals(key) || key.startsWith(type.key)) {
                return type;
            }
        }
        return null;
    }

    public static ProductType fromSession() {
        return fromKey(Session.selectedProductType);
    }
}