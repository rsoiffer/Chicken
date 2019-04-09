package settheory.logic;

import java.util.ArrayList;
import java.util.List;
import settheory.logic.Expression.ExpressionType;
import static settheory.logic.Expression.ExpressionType.SET;
import static settheory.logic.Main.*;

public abstract class Utils {

    public static double levenshteinDistance(final String s1, final String s2) {
        if (s1.equals(s2)) {
            return 0;
        }
        if (s1.length() == 0) {
            return s2.length();
        }
        if (s2.length() == 0) {
            return s1.length();
        }
        int[] v0 = new int[s2.length() + 1];
        int[] v1 = new int[s2.length() + 1];
        int[] vtemp;
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }
        for (int i = 0; i < s1.length(); i++) {
            v1[0] = i + 1;
            int minv1 = v1[0];
            for (int j = 0; j < s2.length(); j++) {
                int cost = 1;
                if (s1.charAt(i) == s2.charAt(j)) {
                    cost = 0;
                }
                v1[j + 1] = Math.min(v1[j] + 1, Math.min(v0[j + 1] + 1, v0[j] + cost));
                minv1 = Math.min(minv1, v1[j + 1]);
            }
            vtemp = v0;
            v0 = v1;
            v1 = vtemp;
        }
        return v0[s2.length()];
    }

    public static Expression randomExpression(int depth, ExpressionType type, List<Expression> atoms) {
        if (depth == 1 || Math.random() < .3 || type == SET) {
            return randomOfType(type, atoms);
        }
//        Operator[] allOps = new Operator[]{not, implies, equal, elementOf, forall};
//        Operator[] allOps = new Operator[]{not, implies};
        Operator[] allOps = new Operator[]{not, implies, equiv, or, and};
        Operator o = allOps[(int) (Math.random() * allOps.length)];
        while (o.outputType != type) {
            o = allOps[(int) (Math.random() * allOps.length)];
        }
        Expression[] parts = new Expression[o.inputTypes.length];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = randomExpression(depth - 1, o.inputTypes[i], atoms);
        }
        return new Expression(type, o, parts, null);
    }

    public static Expression randomOfType(ExpressionType type, List<Expression> list) {
        List<Expression> filtered = new ArrayList();
        for (Expression v : list) {
            if (v.type == type) {
                filtered.add(v);
            }
        }
        return filtered.get((int) (Math.random() * filtered.size()));
    }
}
