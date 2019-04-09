package typetheory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import typetheory.Judgement.TypeJudgement;
import typetheory.Judgement.WFJudgement;

public class Rule2 {

    public final Function<Universe, Stream<Judgement>> outputs;

    public Rule2(Function<Universe, Stream<Judgement>> outputs) {
        this.outputs = outputs;
    }

    public static class Universe {

        public final List<Judgement> judgements;
        public final List<Term> variables;

        public Universe(List<Judgement> judgements, List<Term> variables) {
            this.judgements = judgements;
            this.variables = variables;
        }

        public Stream<TypeJudgement> typeJudgements() {
            return judgements.stream()
                    .filter(j -> j instanceof TypeJudgement)
                    .map(j -> (TypeJudgement) j);
        }

        public Stream<Term> undefVars(Context c) {
            return variables.stream().filter(v -> !c.defines(v));
        }

        public Stream<WFJudgement> wfJudgements() {
            return judgements.stream()
                    .filter(j -> j instanceof WFJudgement)
                    .map(j -> (WFJudgement) j);
        }
    }
}
