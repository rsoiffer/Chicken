package logic;

import java.util.Arrays;
import java.util.List;

public abstract class Predicate extends Expression {

    public static final Predicate True = new Predicate() {
        @Override
        public String print() {
            return "True";
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList();
        }
    };

    public static final Predicate False = new Predicate() {
        @Override
        public String print() {
            return "False";
        }

        @Override
        public List<Expression> subExpressions() {
            return Arrays.asList();
        }
    };
}
