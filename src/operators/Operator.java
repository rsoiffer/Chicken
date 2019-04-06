package operators;

import logic.Expression;

public abstract class Operator<T extends Expression> {

    public abstract T of(Expression... args);
}
