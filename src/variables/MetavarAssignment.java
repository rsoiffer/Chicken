package variables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import logic.Expression;
import logic.Predicate;
import logic.Set;

public class MetavarAssignment {

    public final Expression exp;
    public final List<PredicateMetavar> predicateMetavars;
    public final List<SetMetavar> setMetavars;
    public final Map<PredicateMetavar, Predicate> predicateAssignment = new HashMap();
    public final Map<SetMetavar, Set> setAssignment = new HashMap();

    public MetavarAssignment(Expression exp) {
        this.exp = exp;
        predicateMetavars = (List) exp.subExpressionsRecursive().stream().filter(p -> p instanceof PredicateMetavar).collect(Collectors.toList());
        setMetavars = (List) exp.subExpressionsRecursive().stream().filter(p -> p instanceof SetMetavar).collect(Collectors.toList());
    }

    public Expression apply() {
        return null;
    }

    public void fillRandomly(List<PredicateMetavar> predicateVars, List<SetMetavar> setVars) {
        for (PredicateMetavar p : predicateMetavars) {
            PredicateMetavar p2 = predicateVars.get((int) (Math.random() * predicateVars.size()));
            predicateAssignment.put(p, p2);
        }
        for (SetMetavar s : setMetavars) {
            SetMetavar s2 = setVars.get((int) (Math.random() * setVars.size()));
            setAssignment.put(s, s2);
        }
    }
}
