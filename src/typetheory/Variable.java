package typetheory;

import typetheory.Term.TermCategory;

public class Variable implements TermCategory {

    private final VariableName name = new VariableName(1);

    @Override
    public String printTerm(Term e) {
        return name.toString();
    }

    private Term term() {
        return new Term(this, null);
    }

    public static Term[] vars(int num) {
        Term[] a = new Term[num];
        for (int i = 0; i < num; i++) {
            a[i] = new Variable().term();
        }
        return a;
    }
}
