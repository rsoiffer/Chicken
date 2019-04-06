package logic;

import java.util.Arrays;
import java.util.List;

public abstract class Set extends Expression {

    public static final Set Empty = new Set() {
        @Override
        public String print() {
            return "∅";
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList();
        }
    };
}
