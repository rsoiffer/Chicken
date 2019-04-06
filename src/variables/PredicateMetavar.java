package variables;

import java.util.Arrays;
import java.util.List;
import logic.Expression;
import logic.Predicate;

public class PredicateMetavar extends Predicate {

    private final VariableName v = new VariableName(1);

    @Override
    public String print() {
        return v.name();
    }

    @Override
    public List<Expression> subExpressions() {
        return Arrays.asList();
    }
}
