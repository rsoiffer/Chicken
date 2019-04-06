package operators;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import logic.Expression;
import logic.Predicate;
import logic.Set;

public class SS2BOperator extends Operator<Predicate> {

    private final BiFunction<Set, Set, String> printer;

    public SS2BOperator(BiFunction<Set, Set, String> printer) {
        this.printer = printer;
    }

    @Override
    public Predicate of(Expression... args) {
        if (args.length == 2 && args[0] instanceof Set && args[1] instanceof Set) {
            return new SS2BExpression((Set) args[0], (Set) args[1]);
        }
        throw new RuntimeException("Bad arguments");
    }

    public class SS2BExpression extends Predicate {

        public final Set s1, s2;

        public SS2BExpression(Set p1, Set p2) {
            this.s1 = p1;
            this.s2 = p2;
        }

        @Override
        public String print() {
            return printer.apply(s1, s2);
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList(s1, s2);
        }
    }
}
