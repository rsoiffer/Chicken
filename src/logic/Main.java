package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import static logic.Expression.ExpressionType.PREDICATE;
import static logic.Expression.ExpressionType.SET;
import static logic.Operator.*;
import variables.Variable;
import static variables.Variable.vars;
import variables.VariableAssignment;
import variables.VariableName;

public class Main {

    public static final List<String> AXIOM_NAMES = new ArrayList();
    public static final List<Expression> AXIOMS = new ArrayList();

    public static Operator not, implies, equal, elementOf, forall;

    public static void main(String[] args) {

        // Propositional Calculus
        not = opB2B(p -> "¬" + p);
        implies = opBB2B((p1, p2) -> "(" + p1 + " → " + p2 + ")");

        addAxiom("Axiom Simp", () -> {
            Variable[] v = vars(PREDICATE, PREDICATE);
            return implies.of(v[0], implies.of(v[1], v[0]));
        });
        addAxiom("Axiom Frege", () -> {
            Variable[] v = vars(PREDICATE, PREDICATE, PREDICATE);
            return implies.of(implies.of(v[0], implies.of(v[1], v[2])), implies.of(implies.of(v[0], v[1]), implies.of(v[0], v[2])));
        });
        addAxiom("Axiom Transp", () -> {
            Variable[] v = vars(PREDICATE, PREDICATE);
            return implies.of(implies.of(not.of(v[0]), not.of(v[1])), implies.of(v[1], v[0]));
        });

        // Predicate Calculus
        equal = opSS2B((s1, s2) -> s1 + "=" + s2);
        elementOf = opSS2B((s1, s2) -> s1 + "∈" + s2);
        forall = opSB2B((v, p) -> "∀" + v + ":" + p);

        addAxiom("Quantified Implication", () -> {
            Variable[] v = vars(SET, PREDICATE, PREDICATE);
            return implies.of(forall.of(v[0], implies.of(v[1], v[2])), implies.of(forall.of(v[0], v[1]), forall.of(v[0], v[2])));
        });
        addAxiom("Distinctness", () -> {
            Variable[] v = vars(SET, PREDICATE);
            return implies.of(v[1], forall.of(v[0], v[1])).reqDistinct(v);
        });
        addAxiom("Existence", () -> {
            Variable[] v = vars(SET, SET);
            return not.of(forall.of(v[0], not.of(equal.of(v[0], v[1]))));
        });
        addAxiom("Equality", () -> {
            Variable[] v = vars(SET, SET, SET);
            return implies.of(equal.of(v[0], v[1]), implies.of(equal.of(v[0], v[2]), equal.of(v[1], v[2])));
        });
        addAxiom("Left Equality for Binary Predicate", () -> {
            Variable[] v = vars(SET, SET, SET);
            return implies.of(equal.of(v[0], v[1]), implies.of(elementOf.of(v[0], v[2]), elementOf.of(v[1], v[2])));
        });
        addAxiom("Right Equality for Binary Predicate", () -> {
            Variable[] v = vars(SET, SET, SET);
            return implies.of(equal.of(v[0], v[1]), implies.of(elementOf.of(v[2], v[0]), elementOf.of(v[2], v[1])));
        });
        addAxiom("Quantified Negation", () -> {
            Variable[] v = vars(SET, PREDICATE);
            return implies.of(not.of(forall.of(v[0], v[1])), forall.of(v[0], not.of(forall.of(v[0], v[1]))));
        });
        addAxiom("Quantified Commutation", () -> {
            Variable[] v = vars(SET, SET, PREDICATE);
            return implies.of(forall.of(v[0], forall.of(v[1], v[2])), forall.of(v[1], forall.of(v[0], v[2])));
        });
        addAxiom("Substitution", () -> {
            Variable[] v = vars(SET, SET, PREDICATE);
            return implies.of(equal.of(v[0], v[1]), implies.of(forall.of(v[1], v[2]), forall.of(v[0], implies.of(equal.of(v[0], v[1]), v[2]))));
        });
        addAxiom("Quantified Equality", () -> {
            Variable[] v = vars(SET, SET, SET);
            return implies.of(not.of(equal.of(v[0], v[1])), implies.of(equal.of(v[1], v[2]), forall.of(v[0], equal.of(v[1], v[2]))));
        });

        // Set Theory
        // [Insert things here]
        //
        // Definitions
        if (false) {
            Operator equiv = opBB2B((p1, p2) -> "(" + p1 + " ↔ " + p2 + ")");
            addAxiom("Definition of ↔ (part 1)", () -> {
                Variable[] v = vars(PREDICATE, PREDICATE);
                return implies.of(equiv.of(v[0], v[1]), implies.of(v[0], v[1]));
            });
            addAxiom("Definition of ↔ (part 2)", () -> {
                Variable[] v = vars(PREDICATE, PREDICATE);
                return implies.of(equiv.of(v[0], v[1]), implies.of(v[1], v[0]));
            });
            addAxiom("Definition of ↔ (part 3)", () -> {
                Variable[] v = vars(PREDICATE, PREDICATE);
                return implies.of(implies.of(v[0], v[1]), implies.of(implies.of(v[1], v[0]), equiv.of(v[0], v[1])));
            });

            Operator or = opBB2B((p1, p2) -> "(" + p1 + " ∨ " + p2 + ")");
            addAxiom("Definition of ∨", () -> {
                Variable[] v = vars(PREDICATE, PREDICATE);
                return equiv.of(or.of(v[0], v[1]), implies.of(not.of(v[0]), v[1]));
            });

            Operator and = opBB2B((p1, p2) -> "(" + p1 + " ∧ " + p2 + ")");
            addAxiom("Definition of ∧", () -> {
                Variable[] v = vars(PREDICATE, PREDICATE);
                return equiv.of(and.of(v[0], v[1]), not.of(or.of(not.of(v[0]), not.of(v[1]))));
            });

            Operator exists = opSB2B((v, p) -> "∃" + v + ":" + p);
            addAxiom("Definition of ∃", () -> {
                Variable[] v = vars(SET, PREDICATE);
                return equiv.of(exists.of(v[0], v[1]), not.of(forall.of(v[0], not.of(v[1]))));
            });

            Operator subset = opSS2B((s1, s2) -> s1 + "⊂" + s2);
            addAxiom("Definition of ⊂", () -> {
                Variable[] v = vars(SET, SET, SET);
                return equiv.of(subset.of(v[0], v[1]), forall.of(v[2], implies.of(elementOf.of(v[2], v[0]), elementOf.of(v[2], v[1]))));
            });

            Operator pair = opSS2S((s1, s2) -> "{" + s1 + ", " + s2 + "}");

            Operator singleton = opS2S(s -> "{" + s + "}");
            addAxiom("Definition of {x}", () -> {
                Variable[] v = vars(SET);
                return equal.of(singleton.of(v[0]), pair.of(v[0], v[0]));
            });
        }

        for (int i = 0; i < AXIOM_NAMES.size(); i++) {
            System.out.println(AXIOM_NAMES.get(i));
            VariableName.clearNames();
            System.out.println(AXIOMS.get(i).print());
            System.out.println();
        }

        Variable v = new Variable(PREDICATE);
        prove(implies.of(v, v));
    }

    public static void addAxiom(String name, Supplier<Expression> s) {
        AXIOM_NAMES.add(name);
        AXIOMS.add(s.get());
    }

    public static void prove(Expression goal) {
        List<Expression> known = new LinkedList();
        List<List<Expression>> dependencies = new LinkedList();
        List<String> justifications = new LinkedList();

        List<Variable> variables = new LinkedList();
        for (int i = 0; i < 6; i++) {
            variables.add(new Variable((i < 3) ? PREDICATE : SET));
        }

        while (!known.contains(goal)) {
            if (known.isEmpty() || Math.random() < .001) {
                int axiomID = (int) (Math.random() * AXIOMS.size());
                Expression axiom = AXIOMS.get(axiomID);
                VariableAssignment va = new VariableAssignment(axiom);
                va.fillRandomly(variables);
                if (!known.contains(axiom.substitute(va))) {
                    known.add(axiom.substitute(va));
                    dependencies.add(Arrays.asList());
                    justifications.add(AXIOM_NAMES.get(axiomID));
                }
            } else {
                Expression expr1 = known.get((int) (Math.random() * known.size()));
                Expression expr2 = known.get((int) (Math.random() * known.size()));
                if (expr2.parent() == implies) {
                    if (expr2.parts().get(0).equals(expr1)) {
                        if (!known.contains(expr2.parts().get(1))) {
                            known.add(expr2.parts().get(1));
                            dependencies.add(Arrays.asList(expr1, expr2));
                            justifications.add("Modus ponens on " + expr1 + " and " + expr2);
                            VariableName.clearNames();
                            System.out.println(known);
                        }
                    }
                }
            }
        }
        System.out.println("Success!  " + known.size());
    }
}
