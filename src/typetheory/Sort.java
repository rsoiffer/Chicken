package typetheory;

import java.util.Arrays;
import typetheory.Term.TermCategory;

public abstract class Sort implements TermCategory {

    public Term term() {
        return new Term(this, Arrays.asList());
    }

    public static final Sort Set = new Sort() {
        @Override
        public String printTerm(Term e) {
            return "Set";
        }
    };

    public static final Sort Prop = new Sort() {
        @Override
        public String printTerm(Term e) {
            return "Prop";
        }
    };

    public static class Type extends Sort {

        public final int i;

        public Type(int i) {
            this.i = i;
        }

        @Override
        public String printTerm(Term e) {
            return "Type(" + i + ")";
        }
    }
}
