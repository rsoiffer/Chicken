package variables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logic.Expression;
import static logic.Utils.randomOfType;

public class VariableAssignment {

    private final Expression exp;
    public final Map<Expression, Expression> assignment = new HashMap();

    public VariableAssignment(Expression exp) {
        this.exp = exp;
    }

    public Expression apply() {
        return null;
    }

    public void fillRandomly(List<Expression> newVars) {
        for (Expression e : exp.partsRecursive()) {
            if (e.category instanceof Variable) {
                assignment.put(e, randomOfType(e.type, newVars));
            }
        }
    }
}
