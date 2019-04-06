package operators;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import logic.Expression;
import logic.Set;

public class SS2SOperator extends Operator<Set> {

    private final BiFunction<Set, Set, String> printer;

    public SS2SOperator(BiFunction<Set, Set, String> printer) {
        this.printer = printer;
    }

    @Override
    public Set of(Expression... args) {
        if (args.length == 2 && args[0] instanceof Set && args[1] instanceof Set) {
            return new SS2SExpression((Set) args[0], (Set) args[1]);
        }
        throw new RuntimeException("Bad arguments");
    }

    public class SS2SExpression extends Set {

        public final Set s1, s2;

        public SS2SExpression(Set p1, Set p2) {
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
