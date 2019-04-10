package typetheory;

import java.util.Objects;
import typetheory.Context.GlobalEnvironment;
import typetheory.Context.LocalContext;

public abstract class Judgement {

    public final GlobalEnvironment E;
    public final LocalContext L;

    public Judgement(GlobalEnvironment E, LocalContext L) {
        this.E = E;
        this.L = L;
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
        final Judgement other = (Judgement) obj;
        if (!Objects.equals(this.E, other.E)) {
            return false;
        }
        if (!Objects.equals(this.L, other.L)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.E);
        hash = 79 * hash + Objects.hashCode(this.L);
        return hash;
    }

    public static class TypeJudgement extends Judgement {

        public final TypeStatement ts;

        public TypeJudgement(GlobalEnvironment E, LocalContext L, TypeStatement typeStatement) {
            super(E, L);
            this.ts = typeStatement;
        }

        public TypeJudgement(GlobalEnvironment E, LocalContext L, Term t1, Term t2) {
            this(E, L, new TypeStatement(t1, t2));
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
            final TypeJudgement other = (TypeJudgement) obj;
            if (!Objects.equals(this.ts, other.ts)) {
                return false;
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + Objects.hashCode(this.ts);
            hash = 67 * hash + super.hashCode();
            return hash;
        }

        @Override
        public String toString() {
            return E + "" + L + " ⊢ " + ts;
        }
    }

    public static class WFJudgement extends Judgement {

        public WFJudgement(GlobalEnvironment E, LocalContext L) {
            super(E, L);
        }

        @Override
        public String toString() {
            return "WF" + E + L;
        }
    }
}
