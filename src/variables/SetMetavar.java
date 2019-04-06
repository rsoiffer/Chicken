package variables;

import java.util.Arrays;
import java.util.List;
import logic.Expression;
import logic.Set;

public class SetMetavar extends Set {

    private final VariableName v = new VariableName(0);

    @Override
    public String print() {
        return v.name();
    }

    @Override
    public List<Expression> subExpressions() {
        return Arrays.asList();
    }
}
