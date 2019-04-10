package typetheory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import typetheory.Judgement.TypeJudgement;
import typetheory.Judgement.WFJudgement;

public class Universe {

    public final Set<Judgement> judgements = new HashSet();
    public final List<Term> variables;
    public final int maxType;

    public Universe(List<Term> variables, int maxType) {
        this.variables = variables;
        this.maxType = maxType;
    }

    public void applyAllRules(List<Rule> rules) {
        List<Judgement> newJudgements = rules.stream().flatMap(r -> r.outputs.apply(this)).collect(Collectors.toList());
        judgements.addAll(newJudgements);
    }

    public List<Integer> countJudgements(List<Rule> rules) {
        return rules.stream().map(r -> (int) r.outputs.apply(this).count()).collect(Collectors.toList());
    }

    public List<Integer> countNewJudgements(List<Rule> rules) {
        return rules.stream().map(r -> (int) r.outputs.apply(this).filter(j -> !judgements.contains(j)).count()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        // return typeJudgements().map(j -> j.ts + "\n").reduce("", String::concat);
        return judgements.stream().map(j -> j + "\n").reduce("", String::concat);
    }

    public Stream<TypeJudgement> typeJudgements() {
        return judgements.stream()
                .filter(j -> j instanceof Judgement.TypeJudgement)
                .map(j -> (Judgement.TypeJudgement) j);
    }

    public Stream<Term> undefVars(Context c) {
        return variables.stream().filter(v -> !c.defines(v));
    }

    public Stream<WFJudgement> wfJudgements() {
        return judgements.stream()
                .filter(j -> j instanceof Judgement.WFJudgement)
                .map(j -> (Judgement.WFJudgement) j);
    }
}
