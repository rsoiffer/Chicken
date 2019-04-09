package settheory.proofs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.stream.Collectors;
import settheory.logic.Expression;
import static settheory.logic.Expression.ExpressionType.PREDICATE;
import static settheory.logic.Expression.ExpressionType.SET;
import static settheory.logic.Main.AXIOMS;
import static settheory.logic.Main.AXIOM_NAMES;
import settheory.logic.Utils;
import settheory.proofs.InferenceRule.ModusPonens;
import static settheory.variables.Variable.vars;
import settheory.variables.VariableAssignment;

public class Proof {

    final List<ProofStep> work = new ArrayList();
    final Map<Expression, ProofStep> known = new HashMap();
    final List<Expression> variables = new ArrayList();

    private final List<InferenceRule> inferenceRules = Arrays.asList(new ModusPonens(this));
    private double totalLikelihood;
    private final TreeMap<Double, ProofStep> likelihoodTree = new TreeMap();

    private Expression goal;

    public Proof() {
        for (int i = 0; i < 8; i++) {
            variables.add(vars((i < 7) ? PREDICATE : SET)[0]);
        }
        for (int i = 0; i < AXIOMS.size(); i++) {
            addInference(new ProofStep(AXIOMS.get(i), Arrays.asList(), AXIOM_NAMES.get(i)));
        }
    }

    public boolean addInference(ProofStep s) {
        if (s.e.complexity() > 20) {
            return false;
        }
        s = new ProofStep(simplify(s.e), s.dependencies, s.justification);

        if (!known.containsKey(s.e)) {
            work.add(s);
            known.put(s.e, s);
            double c = s.e.complexity();
            totalLikelihood += 1. / (Math.exp(.1 * (c - 2) * s.depth));
            likelihoodTree.put(totalLikelihood, s);

            if (c < 3.5) {
                System.out.print(s);
            }
            if (work.size() % 10000 == 0) {
                System.out.println(work.size() + " " + known.size());
                System.out.print(s.depth + " " + s);
            }

            for (InferenceRule ir : inferenceRules) {
                ir.onNewStep(s);
            }
            return true;
        } else {
            if (known.get(s.e).depth > s.depth) {
                work.add(s);
                known.put(s.e, s);
            }
        }
        return false;
    }

    public void addKnown(Expression e) {
        ProofStep s = new ProofStep(e, Arrays.asList(), "assumption");
        if (addInference(s)) {
            setImportant(s);
        }
    }

    public boolean prove(Expression goal) {
        goal = simplify(goal);
        this.goal = goal;

        while (!known.containsKey(goal)) {
            ProofStep s = sampleStep();
            VariableAssignment va = new VariableAssignment(s.e);
            List<Expression> newExprs = new LinkedList();
            for (int i = 0; i < 4; i++) {
                newExprs.add(Utils.randomExpression(3, (i < 3) ? PREDICATE : SET, variables));
            }
            va.fillRandomly(newExprs);
            addInference(new ProofStep(s.e.substitute(va.assignment), Arrays.asList(s), "Substitution on " + s.e));
        }
        setImportant(known.get(goal));
        return true;
    }

    public ProofStep sampleStep() {
        return work.get((int) (Math.random() * AXIOMS.size()));
//        double r = Math.random() * totalLikelihood;
//        return likelihoodTree.ceilingEntry(r).getValue();
    }

    public void setImportant(ProofStep proofStep) {
        Queue<ProofStep> toCheck = new LinkedList();
        toCheck.add(proofStep);
        while (!toCheck.isEmpty()) {
            ProofStep s = toCheck.poll();
            if (!s.important) {
                s.important = true;
                toCheck.addAll(s.dependencies);
            }
        }
    }

    public Expression simplify(Expression e) {
        VariableAssignment va = new VariableAssignment(e);
        int c = 0;
        for (Expression e2 : e.variables().collect(Collectors.toList())) {
            if (e2.type == PREDICATE) {
                if (!va.assignment.containsKey(e2)) {
                    va.assignment.put(e2, variables.get(c));
                    c++;
                }
            }
        }
        return e.substitute(va.assignment);
    }

    @Override
    public String toString() {
        String r = "";
        for (ProofStep s : work) {
            if (s.important) {
                r += s;
            }
        }
        return r;
    }
}
