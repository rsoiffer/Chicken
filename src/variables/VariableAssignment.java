package variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logic.Expression;
import logic.Expression.ExpressionType;

public class VariableAssignment {

    private final Expression exp;
    public final Map<Variable, Variable> assignment = new HashMap();

    public VariableAssignment(Expression exp) {
        this.exp = exp;
    }

    public Expression apply() {
        return null;
    }

    public void fillRandomly(List<Variable> newVars) {
        Map<ExpressionType, List<Variable>> newVarsByType = new HashMap();
        for (Variable v : newVars) {
            newVarsByType.putIfAbsent(v.type, new ArrayList());
            newVarsByType.get(v.type).add(v);
        }
        for (Expression e : exp.partsRecursive()) {
            if (e instanceof Variable) {
                List<Variable> options = newVarsByType.get(e.type);
                assignment.put((Variable) e, options.get((int) (Math.random() * options.size())));
            }
        }
    }
}
