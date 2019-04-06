package operators;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import logic.Expression;
import logic.Predicate;
import variables.SetMetavar;

public class VB2BOperator extends Operator<Predicate> {

    private final BiFunction<SetMetavar, Predicate, String> printer;

    public VB2BOperator(BiFunction<SetMetavar, Predicate, String> printer) {
        this.printer = printer;
    }

    @Override
    public Predicate of(Expression... args) {
        if (args.length == 2 && args[0] instanceof SetMetavar && args[1] instanceof Predicate) {
            return new VB2BExpression((SetMetavar) args[0], (Predicate) args[1]);
        }
        throw new RuntimeException("Bad arguments");
    }

    public class VB2BExpression extends Predicate {

        public final SetMetavar v;
        public final Predicate p;

        public VB2BExpression(SetMetavar v, Predicate p) {
            this.v = v;
            this.p = p;
        }

        @Override
        public String print() {
            return printer.apply(v, p);
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList(p);
        }
    }
}
