package typetheory;

import typetheory.Term.TermCategory.AtomicTermCategory;

public abstract class Variable extends AtomicTermCategory {

    public static Term[] vars(int num) {
        Term[] a = new Term[num];
        for (int i = 0; i < num; i++) {
            a[i] = new AnonVar().term();
        }
        return a;
    }

    public static class AnonVar extends Variable {

        private final VariableName name = new VariableName(1);

        @Override
        public String printTerm(Term e) {
            return name.toString();
        }
    }

    public static class NamedVar extends Variable {

        private final String name;

        public NamedVar(String name) {
            this.name = name;
        }

        @Override
        public String printTerm(Term e) {
            return name;
        }
    }
}
