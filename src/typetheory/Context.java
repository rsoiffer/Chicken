package typetheory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Stream;
import static typetheory.Utils.mapSubset;
import static typetheory.Utils.mapSubtract;

public abstract class Context<T extends Context> {

    public static final GlobalEnvironment E0 = new GlobalEnvironment();
    public static final LocalContext L0 = new LocalContext();

    final Map<Term, Term> assumptions = new HashMap();
    final Map<Term, TypeStatement> definitions = new HashMap();

    public Stream<TypeStatement> allTypeDefs() {
        return Stream.concat(
                assumptions.entrySet().stream().map(e -> new TypeStatement(e.getKey(), e.getValue())),
                definitions.entrySet().stream().map(e -> new TypeStatement(e.getKey(), e.getValue().t2))
        );
    }

    public T appendAssum(Term x, Term t) {
        try {
            T copy = (T) getClass().newInstance();
            copy.assumptions.putAll(assumptions);
            copy.definitions.putAll(definitions);
            copy.assumptions.put(x, t);
            return copy;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public T appendDef(Term x, Term t1, Term t2) {
        try {
            T copy = (T) getClass().newInstance();
            copy.assumptions.putAll(assumptions);
            copy.definitions.putAll(definitions);
            copy.definitions.put(x, new TypeStatement(t1, t2));
            return copy;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean defines(Term x) {
        return assumptions.containsKey(x) || definitions.containsKey(x);
    }

    public boolean subset(T c) {
        return mapSubset(assumptions, c.assumptions) && mapSubset(definitions, c.definitions);
    }

    public T subtract(T c) {
        try {
            T copy = (T) getClass().newInstance();
            copy.assumptions.putAll(mapSubtract(assumptions, c.assumptions));
            copy.definitions.putAll(mapSubtract(definitions, c.definitions));
            return copy;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
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
        final Context other = (Context) obj;
        if (!Objects.equals(this.assumptions, other.assumptions)) {
            return false;
        }
        if (!Objects.equals(this.definitions, other.definitions)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.assumptions);
        hash = 79 * hash + Objects.hashCode(this.definitions);
        return hash;
    }

    @Override
    public String toString() {
        String r = "";
        for (Entry<Term, Term> e : assumptions.entrySet()) {
            r += e.getKey() + ":" + e.getValue() + "; ";
        }
        for (Entry<Term, TypeStatement> e : definitions.entrySet()) {
            r += e.getKey() + ":=" + e.getValue() + "; ";
        }
        if (r.endsWith("; ")) {
            r = r.substring(0, r.length() - 2);
        }
        return r;
    }

    public static class LocalContext extends Context<LocalContext> {

        @Override
        public String toString() {
            return "[" + super.toString() + "]";
        }
    }

    public static class GlobalEnvironment extends Context<GlobalEnvironment> {

        @Override
        public String toString() {
            return "(" + super.toString() + ")";
        }
    }
}
