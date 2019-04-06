package variables;

import java.util.HashMap;
import java.util.Map;

public class VariableName {

    private static final String[][] nameArray = {
        {"x", "y", "z", "w", "v", "u", "t", "s", "r", "q"},
        {"𝜑", "𝜓", "𝜒"},
        {"A", "B", "C", "D", "E", "F"}
    };

    private static final Map<VariableName, String> currentNames = new HashMap();
    private static int[] nameCount = new int[nameArray.length];

    private final int namingScheme;

    public VariableName(int namingScheme) {
        this.namingScheme = namingScheme;
    }

    public static void clearNames() {
        currentNames.clear();
        for (int i = 0; i < nameCount.length; i++) {
            nameCount[i] = 0;
        }
    }

    private String intToName(int i) {
        String r = nameArray[namingScheme][i % nameArray[namingScheme].length];
        while (i >= nameArray[namingScheme].length) {
            r += "'";
            i -= nameArray[namingScheme].length;
        }
        return r;
    }

    public String name() {
        if (!currentNames.containsKey(this)) {
            currentNames.put(this, intToName(nameCount[namingScheme]));
            nameCount[namingScheme]++;
        }
        return currentNames.get(this);
    }
}
