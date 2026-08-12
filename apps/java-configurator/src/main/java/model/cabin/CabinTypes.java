package model.cabin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CabinTypes {

    private static final Map<String, CabinTypeInfo> types;

    static {
        Map<String, CabinTypeInfo> map = new HashMap<>();

        // TIP 1
        CabinTypeInfo tip1 = new CabinTypeInfo("tipu_1");
        tip1.addFeronerie("balamale", "SH301", 2);
        tip1.addFeronerie("manere_buton", "BR20", 1);
        tip1.addFeronerie("profile_rigidizare_si_conectori", "GT02-304", 1);
        tip1.addFeronerie("profile_rigidizare_si_conectori", "C34", 1);
        tip1.addFeronerie("profile_rigidizare_si_conectori", "C35", 1);
        tip1.addFeronerie("garnituri","S01",1);
        tip1.addFeronerie("garnituri","S02",1);
        tip1.addFeronerie("garnituri","S10",1);
        tip1.addProfileLength("U20", 1);
        tip1.addProfileLength("GPU", 1);
        map.put("tipu_1", tip1);

        // TIP 2
        CabinTypeInfo tip2 = new CabinTypeInfo("tipu_2");
        tip2.addFeronerie("balamale", "SH301", 2);
        tip2.addFeronerie("manere_buton", "BR20", 1);
        tip2.addFeronerie("profile_rigidizare_si_conectori", "GT02-304", 1);
        tip2.addFeronerie("profile_rigidizare_si_conectori", "C34", 1);
        tip2.addFeronerie("profile_rigidizare_si_conectori", "C35", 1);
        tip2.addFeronerie("garnituri","S01",1);
        tip2.addFeronerie("garnituri","S02",1);
        tip2.addFeronerie("garnituri","S10",1);
        tip2.addProfileLength("U20", 1);
        tip2.addProfileLength("GPU", 1);
        map.put("tipu_2", tip2);

        // TIP 3
        CabinTypeInfo tip3 = new CabinTypeInfo("tipu_3");
        tip3.addFeronerie("balamale", "SH303", 2);
        tip3.addFeronerie("manere_buton", "BR20", 1);

        tip3.addFeronerie("profile_rigidizare_si_conectori", "C34", 2);
        tip3.addFeronerie("profile_rigidizare_si_conectori", "C35", 2);
        tip3.addProfileLength("U20", 1);
        tip3.addProfileLength("GPU", 1);
        tip3.addFeronerie("garnituri","S01",1);
        tip3.addFeronerie("garnituri","S02",1);
        tip3.addFeronerie("garnituri","S10",1);
        tip3.addFeronerie("profile_rigidizare_si_conectori", "GT02-304", 2);
        map.put("tipu_3", tip3);

        // TIP 4
        CabinTypeInfo tip4 = new CabinTypeInfo("tipu_4");
        tip4.addFeronerie("balamale", "SH303", 2);
        tip4.addFeronerie("manere_buton", "BR20", 1);
        tip4.addFeronerie("profile_rigidizare_si_conectori", "GT02-304", 1);
        tip4.addFeronerie("profile_rigidizare_si_conectori", "C34", 1);
        tip4.addFeronerie("profile_rigidizare_si_conectori", "C35", 1);
        tip4.addFeronerie("profile_rigidizare_si_conectori", "C36", 1);
        tip4.addProfileLength("U20", 1);
        tip4.addProfileLength("GPU", 1);
        tip4.addFeronerie("garnituri","S01",1);
        tip4.addFeronerie("garnituri","S02",1);
        tip4.addFeronerie("garnituri","S10",1);
        map.put("tipu_4", tip4);

        // TIP 5
        CabinTypeInfo tip5 = new CabinTypeInfo("tipu_5");
        tip5.addFeronerie("balamale", "SH303", 4);
        tip5.addFeronerie("manere_buton", "BR20", 2);
        tip5.addFeronerie("profile_rigidizare_si_conectori", "GT02-304", 2);
        tip5.addFeronerie("profile_rigidizare_si_conectori", "C34", 2);
        tip5.addFeronerie("profile_rigidizare_si_conectori", "C35", 2);
        tip5.addProfileLength("U20", 1);
        tip5.addProfileLength("GPU", 1);
        tip5.addFeronerie("garnituri","S01",2);
        tip5.addFeronerie("garnituri","S02",2);
        tip5.addFeronerie("garnituri","S10",2);

        map.put("tipu_5", tip5);

        // TIP 6:
        CabinTypeInfo tip6 = new CabinTypeInfo("tipu_6");
        tip6.addFeronerie("balamale", "SH303", 4);
        tip6.addFeronerie("manere_buton", "BR20", 2);
        tip6.addFeronerie("profile_rigidizare_si_conectori", "GT02-304", 2);
        tip6.addFeronerie("profile_rigidizare_si_conectori", "C34", 2);
        tip6.addFeronerie("profile_rigidizare_si_conectori", "C35", 2);
        tip6.addProfileLength("U20", 1);
        tip6.addProfileLength("GPU", 1);
        tip6.addFeronerie("garnituri","S01",2);
        tip6.addFeronerie("garnituri","S02",2);
        tip6.addFeronerie("garnituri","S10",2);
        map.put("tipu_6", tip6);


        //tip 7:panou
        CabinTypeInfo panou = new CabinTypeInfo("panou");
        panou.addFeronerie("balamale", "SH301", 2);
        panou.addFeronerie("profile_rigidizare_si_conectori", "GT02-304", 1);
        panou.addFeronerie("profile_rigidizare_si_conectori", "C34", 1);
        panou.addFeronerie("profile_rigidizare_si_conectori", "C35", 1);
        panou.addFeronerie("garnituri","S01",1);
        panou.addFeronerie("garnituri","S02",1);
        panou.addFeronerie("garnituri","S10",1);
        panou.addProfileLength("U20", 1);
        panou.addProfileLength("GPU", 1);
        map.put("panou", panou);
        types = Collections.unmodifiableMap(map);
    }

    public static Map<String, CabinTypeInfo> getTypes() {
        return types;
    }

    public static boolean exists(String typeKey) {
        if (typeKey == null) return false;
        return types.containsKey(typeKey);
    }

    public static CabinTypeInfo get(String typeKey) {
        if (typeKey == null) return null;
        return types.get(typeKey);
    }
}