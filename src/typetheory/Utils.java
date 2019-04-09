package typetheory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Utils {

    public static <T, U> boolean mapSubset(Map<T, U> m1, Map<T, U> m2) {
        return m1.entrySet().stream().allMatch(e -> m2.containsKey(e.getKey()) && m2.get(e.getKey()).equals(e.getValue()));
    }

    public static <T, U> Map<T, U> mapSubtract(Map<T, U> m1, Map<T, U> m2) {
        Map<T, U> r = new HashMap();
        for (Entry<T, U> e : m1.entrySet()) {
            if (!(m2.containsKey(e.getKey()) && m2.get(e.getKey()).equals(e.getValue()))) {
                r.put(e.getKey(), e.getValue());
            }
        }
        return r;
    }
}
