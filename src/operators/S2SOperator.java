package operators;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import logic.Expression;
import logic.Set;

public class S2SOperator extends Operator<Set> {

    private final Function< Set, String> printer;

    public S2SOperator(Function<Set, String> printer) {
        this.printer = printer;
    }

    @Override
    public Set of(Expression... args) {
        if (args.length == 1 && args[0] instanceof Set) {
            return new S2SExpression((Set) args[0]);
        }
        throw new RuntimeException("Bad arguments");
    }

    public class S2SExpression extends Set {

        public final Set s;

        public S2SExpression(Set s) {
            this.s = s;
        }

        @Override
        public String print() {
            return printer.apply(s);
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList(s);
        }
    }
}
