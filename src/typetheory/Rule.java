package typetheory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import typetheory.Context.GlobalEnvironment;
import typetheory.Context.LocalContext;
import typetheory.Operator.TriFunction;

public class Rule {

    public final int numInputs;
    public final TriFunction<GlobalEnvironment, LocalContext, List<Term>, Function<List<Judgement>, Boolean>> condition;
    public final TriFunction<GlobalEnvironment, LocalContext, List<Term>, Judgement> output;

    public Rule(int numInputs, TriFunction<GlobalEnvironment, LocalContext, List<Term>, Function<List<Judgement>, Boolean>> conditions, TriFunction<GlobalEnvironment, LocalContext, List<Term>, Judgement> output) {
        this.numInputs = numInputs;
        this.condition = conditions;
        this.output = output;
    }

    public RuleInstance of(GlobalEnvironment E, LocalContext L, Term... args) {
        if (args.length != numInputs) {
            throw new RuntimeException("Wrong number of arguments");
        }
        return new RuleInstance(
                condition.apply(E, L, Arrays.asList(args)),
                output.apply(E, L, Arrays.asList(args))
        );
    }

    public static class RuleInstance {

        private final Function<List<Judgement>, Boolean> condition;
        private final Judgement output;

        private RuleInstance(Function<List<Judgement>, Boolean> condition, Judgement output) {
            this.condition = condition;
            this.output = output;
        }

        public boolean canApply(List<Judgement> universe) {
            return condition.apply(universe);
        }

        public Judgement output() {
            return output;
        }
    }
}
