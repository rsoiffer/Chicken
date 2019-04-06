package operators;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import logic.Expression;
import logic.Predicate;

public class BB2BOperator extends Operator<Predicate> {

    private final BiFunction<Predicate, Predicate, String> printer;

    public BB2BOperator(BiFunction<Predicate, Predicate, String> printer) {
        this.printer = printer;
    }

    @Override
    public Predicate of(Expression... args) {
        if (args.length == 2 && args[0] instanceof Predicate && args[1] instanceof Predicate) {
            return new BB2BExpression((Predicate) args[0], (Predicate) args[1]);
        }
        throw new RuntimeException("Bad arguments");
    }

    public class BB2BExpression extends Predicate {

        public final Predicate p1, p2;

        public BB2BExpression(Predicate p1, Predicate p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public String print() {
            return printer.apply(p1, p2);
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList(p1, p2);
        }
    }
}
