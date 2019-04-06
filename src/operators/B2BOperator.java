package operators;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import logic.Expression;
import logic.Predicate;

public class B2BOperator extends Operator<Predicate> {

    private final Function<Predicate, String> printer;

    public B2BOperator(Function<Predicate, String> printer) {
        this.printer = printer;
    }

    @Override
    public Predicate of(Expression... args) {
        if (args.length == 1 && args[0] instanceof Predicate) {
            return new B2BExpression((Predicate) args[0]);
        }
        throw new RuntimeException("Bad arguments");
    }

    public class B2BExpression extends Predicate {

        public final Predicate p;

        public B2BExpression(Predicate p) {
            this.p = p;
        }

        @Override
        public String print() {
            return printer.apply(p);
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList(p);
        }
    }
}
