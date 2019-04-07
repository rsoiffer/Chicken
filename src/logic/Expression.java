package logic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Expression {

    public static interface ExpressionCategory {

        public String printExpression(Expression e);
    }

    public static enum ExpressionType {
        PREDICATE, SET
    }

    public final ExpressionType type;
    public final ExpressionCategory category;
    final Expression[] parts;
    private final Expression[] reqDistinct;

    public Expression(ExpressionType type, ExpressionCategory category, Expression[] parts, Expression[] reqDistinct) {
        this.type = type;
        this.category = category;
        this.parts = parts;
        this.reqDistinct = reqDistinct;
    }

    @Override
    public boolean equals(Object obj) {
        final Expression other = (Expression) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
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
        hash = 43 * hash + Objects.hashCode(this.category);
        hash = 43 * hash + Arrays.deepHashCode(this.parts);
        hash = 43 * hash + Arrays.deepHashCode(this.reqDistinct);
        return hash;
    }

    public List<Expression> parts() {
        return parts == null ? Arrays.asList() : Arrays.asList(parts);
    }

    public List<Expression> partsRecursive() {
        return Stream.concat(Stream.of(this), parts().stream().flatMap(e -> e.partsRecursive().stream())).collect(Collectors.toList());
    }

    public Expression reqDistinct(Expression... reqDistinct) {
        return new Expression(type, category, parts, reqDistinct);
    }

    public Expression substitute(Map<Expression, Expression> remap) {
        if (remap.containsKey(this)) {
            return remap.get(this);
        }
        Expression[] newParts = new Expression[parts.length];
        for (int i = 0; i < parts.length; i++) {
            newParts[i] = parts[i].substitute(remap);
        }
        return new Expression(type, category, newParts, reqDistinct);
    }

    @Override
    public String toString() {
        return category.printExpression(this);
    }
}
