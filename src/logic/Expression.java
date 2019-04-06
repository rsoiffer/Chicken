package logic;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Expression {

    public Expression[] reqDistinct;

    public boolean atomic() {
        return subExpressions().isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Expression)) {
            return false;
        }
        return print().equals(((Expression) obj).print());
    }

    @Override
    public int hashCode() {
        return print().hashCode();
    }

    public abstract String print();

    public Expression reqDistinct(Expression... reqDistinct) {
        this.reqDistinct = reqDistinct;
        return this;
    }

    public abstract List<Expression> subExpressions();

    public List<Expression> subExpressionsRecursive() {
        return Stream.concat(Stream.of(this), subExpressions().stream().flatMap(e -> e.subExpressionsRecursive().stream())).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return print();
    }
}
