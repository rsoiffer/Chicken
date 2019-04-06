package logic;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import variables.Variable;
import variables.VariableAssignment;

public abstract class Expression {

    public static enum ExpressionType {
        PREDICATE, SET
    }

    public final ExpressionType type;
    private final Object parent;
    private final Expression[] parts;
    private final Expression[] reqDistinct;

    public Expression(ExpressionType type, Object parent, Expression[] parts, Expression[] reqDistinct) {
        this.type = type;
        this.parent = parent;
        this.parts = parts;
        this.reqDistinct = reqDistinct;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Expression other = (Expression) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.parent, other.parent)) {
            return false;
        }
        if (!Arrays.deepEquals(this.parts, other.parts)) {
            return false;
        }
        if (!Arrays.deepEquals(this.reqDistinct, other.reqDistinct)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.type);
        hash = 43 * hash + Objects.hashCode(this.parent);
        hash = 43 * hash + Arrays.deepHashCode(this.parts);
        hash = 43 * hash + Arrays.deepHashCode(this.reqDistinct);
        return hash;
    }

    public Object parent() {
        return parent;
    }

    public List<Expression> parts() {
        return Arrays.asList(parts);
    }

    public List<Expression> partsRecursive() {
        return Stream.concat(Stream.of(this), parts().stream().flatMap(e -> e.partsRecursive().stream())).collect(Collectors.toList());
    }

    public abstract String print();

    public Expression reqDistinct(Expression... reqDistinct) {
        Expression self = this;
        return new Expression(type, parent, parts, reqDistinct) {
            @Override
            public String print() {
                return self.print() + "_";
            }
        };
    }

    public Expression substitute(VariableAssignment va) {
        if (this instanceof Variable) {
            return va.assignment.get((Variable) this);
        }
        Expression[] newParts = new Expression[parts.length];
        for (int i = 0; i < parts.length; i++) {
            newParts[i] = parts[i].substitute(va);
        }
        Expression self = this;
        return new Expression(type, parent, newParts, reqDistinct) {
            @Override
            public String print() {
                return self.print() + "_";
            }
        };
    }

    @Override
    public String toString() {
        return print();
    }
}
