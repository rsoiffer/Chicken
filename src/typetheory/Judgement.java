package typetheory;

import typetheory.Context.GlobalEnvironment;
import typetheory.Context.LocalContext;

public abstract class Judgement {

    public final GlobalEnvironment E;
    public final LocalContext L;

    public Judgement(GlobalEnvironment E, LocalContext L) {
        this.E = E;
        this.L = L;
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
