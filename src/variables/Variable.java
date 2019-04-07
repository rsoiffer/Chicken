package variables;

import logic.Expression;
import logic.Expression.ExpressionCategory;
import logic.Expression.ExpressionType;

public class Variable implements ExpressionCategory {

    private final VariableName name;

    public Variable(ExpressionType type) {
        name = new VariableName(type.ordinal());
    }

    @Override
    public String printExpression(Expression e) {
        return name.toString();
    }

    public static Expression[] vars(ExpressionType... types) {
        Expression[] a = new Expression[types.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Expression(types[i], new Variable(types[i]), null, null);
        }
        return a;
    }
}
