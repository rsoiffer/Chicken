package typetheory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static typetheory.Main.forall;
import static typetheory.Main.function;

public final class Term {

    public static abstract class TermCategory {

        public abstract String printTerm(Term t);

        public static abstract class AtomicTermCategory extends TermCategory {

            public Term term() {
                return new Term(this, null);
            }
        }
    }

    public final TermCategory category;
    public final List<Term> parts;

    public Term(TermCategory category, List<Term> parts) {
        this.category = category;
        this.parts = parts;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Term other = (Term) obj;
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.parts, other.parts)) {
            return false;
        }
        return true;
    }

    public Stream<Term> freeVars() {
        if (parts == null || parts.isEmpty()) {
            return Stream.of();
        }
        if (category instanceof Variable) {
            return Stream.of(this);
        }
        if (category == forall || category == function) {
            parts.stream().flatMap(Term::freeVars).filter(v -> !v.equals(parts.get(0)));
        }
        return parts.stream().flatMap(Term::freeVars);

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.category);
        hash = 17 * hash + Objects.hashCode(this.parts);
        return hash;
    }

    public Term substitute(Term var, Term replaceWith) {
        if (equals(var)) {
            return replaceWith;
        }
        if (parts == null || parts.isEmpty()) {
            return this;
        }
        if (category == forall || category == function) {
            if (parts.get(0).equals(var)) {
                return this;
            }
        }
        return new Term(category, parts.stream().map(p -> p.substitute(var, replaceWith)).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return category.printTerm(this);
    }
}
