package variables;

import logic.Expression;

public class Variable extends Expression {

    private final VariableName name;

    public Variable(ExpressionType type) {
        super(type, new Object(), new Expression[]{}, new Expression[]{});
        name = new VariableName(type.ordinal());
    }

    @Override
    public String print() {
        return name.toString();
    }

    public static Variable[] vars(ExpressionType... types) {
        Variable[] a = new Variable[types.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Variable(types[i]);
        }
        return a;
    }
}
