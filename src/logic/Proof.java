package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import static logic.Expression.ExpressionType.PREDICATE;
import static logic.Expression.ExpressionType.SET;
import logic.InferenceRule.Inference;
import logic.InferenceRule.ModusPonens;
import static logic.Main.AXIOMS;
import static logic.Main.AXIOM_NAMES;
import static logic.Utils.randomExpression;
import variables.Variable;
import static variables.Variable.vars;
import variables.VariableAssignment;

public class Proof {

    public static final List<InferenceRule> INFERENCE_RULES = Arrays.asList(new ModusPonens());

    private final List<Inference> work = new ArrayList();
    private final Map<Expression, Integer> known = new HashMap();
    private Expression goal;

    public boolean addInference(Inference i) {
        if (!known.containsKey(i.e)) {
            work.add(i);
            known.put(i.e, work.size() - 1);
            if (work.size() % 1000 == 0) {
                System.out.println(work.size());
            }
            for (InferenceRule ir : INFERENCE_RULES) {
                for (Inference i2 : ir.applyToNew(i, work, known)) {
                    addInference(i2);
                }
            }
            return true;
        }
        return false;
    }

    public void addKnown(Expression e) {
        addInference(new Inference(e, Arrays.asList(), "assumption"));
    }

    public boolean prove(Expression goal) {
        this.goal = goal;

        List<Expression> variables = new LinkedList();
        for (Expression e : goal.partsRecursive()) {
            if (e.category instanceof Variable) {
                variables.add(e);
            }
        }
        for (int i = 0; i < 3; i++) {
            variables.add(vars((i < 2) ? PREDICATE : SET)[0]);
        }

        while (!known.containsKey(goal)) {
            if (Math.random() < 1) {
                int axiomID = (int) (Math.random() * AXIOMS.size());
                Expression axiom = AXIOMS.get(axiomID);
                VariableAssignment va = new VariableAssignment(axiom);
                List<Expression> newExprs = new LinkedList();
                for (int i = 0; i < 10; i++) {
                    newExprs.add(randomExpression(3, (i < 8) ? PREDICATE : SET, variables));
                }
                va.fillRandomly(newExprs);
                addInference(new Inference(axiom.substitute(va.assignment), Arrays.asList(), AXIOM_NAMES.get(axiomID)));
            } else {
                for (InferenceRule ir : INFERENCE_RULES) {
                    for (Inference i : ir.allInferences(work)) {
                        addInference(i);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        boolean[] reached = new boolean[work.size()];
        Queue<Integer> toCheck = new LinkedList();
        toCheck.add(known.get(goal));
        while (!toCheck.isEmpty()) {
            int i = toCheck.poll();
            if (!reached[i]) {
                reached[i] = true;
                toCheck.addAll(work.get(i).dependencies);
            }
        }
        String r = "";
        for (int i = 0; i < work.size(); i++) {
            if (reached[i]) {
                r += work.get(i).e + "   by " + work.get(i).justification + "\n";
            }
        }
        return r;
    }

    public List<Inference> work() {
        return work;
    }
}
