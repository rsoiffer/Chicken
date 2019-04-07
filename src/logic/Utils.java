package logic;

import java.util.ArrayList;
import java.util.List;
import logic.Expression.ExpressionType;
import static logic.Expression.ExpressionType.SET;
import static logic.Main.*;

public abstract class Utils {

    public static Expression randomExpression(int depth, ExpressionType type, List<Expression> atoms) {
        if (depth == 1 || Math.random() < 1.0 / depth || type == SET) {
            return randomOfType(type, atoms);
        }
//         Operator[] allOps = new Operator[]{not, implies, equal, elementOf, forall};
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
