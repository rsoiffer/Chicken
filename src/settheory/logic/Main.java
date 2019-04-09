package settheory.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import static settheory.logic.Expression.ExpressionType.PREDICATE;
import static settheory.logic.Expression.ExpressionType.SET;
import static settheory.logic.Operator.*;
import settheory.proofs.Proof;
import static settheory.variables.Variable.vars;
import settheory.variables.VariableName;

public class Main {

    public static final List<String> AXIOM_NAMES = new ArrayList();
    public static final List<Expression> AXIOMS = new ArrayList();

    public static Operator not, implies, equal, elementOf, forall;
    public static Operator equiv, or, and, exists, subset, pair, singleton;

    public static void main(String[] args) {

        // Propositional Calculus
        not = opB2B(p -> "¬" + p);
        implies = opBB2B((p1, p2) -> "(" + p1 + " → " + p2 + ")");

        addAxiom("Axiom Simp", () -> {
            Expression[] v = vars(PREDICATE, PREDICATE);
            return implies.of(v[0], implies.of(v[1], v[0]));
        });
        addAxiom("Axiom Frege", () -> {
            Expression[] v = vars(PREDICATE, PREDICATE, PREDICATE);
            return implies.of(implies.of(v[0], implies.of(v[1], v[2])), implies.of(implies.of(v[0], v[1]), implies.of(v[0], v[2])));
        });
        addAxiom("Axiom Transp", () -> {
            Expression[] v = vars(PREDICATE, PREDICATE);
            return implies.of(implies.of(not.of(v[0]), not.of(v[1])), implies.of(v[1], v[0]));
        });

        if (false) {
            // Predicate Calculus
            equal = opSS2B((s1, s2) -> s1 + "=" + s2);
            elementOf = opSS2B((s1, s2) -> s1 + "∈" + s2);
            forall = opSB2B((v, p) -> "∀" + v + ":" + p);

            addAxiom("Quantified Implication", () -> {
                Expression[] v = vars(SET, PREDICATE, PREDICATE);
                return implies.of(forall.of(v[0], implies.of(v[1], v[2])), implies.of(forall.of(v[0], v[1]), forall.of(v[0], v[2])));
            });
            addAxiom("Distinctness", () -> {
                Expression[] v = vars(SET, PREDICATE);
                return implies.of(v[1], forall.of(v[0], v[1])).reqDistinct(v);
            });
            addAxiom("Existence", () -> {
                Expression[] v = vars(SET, SET);
                return not.of(forall.of(v[0], not.of(equal.of(v[0], v[1]))));
            });
            addAxiom("Equality", () -> {
                Expression[] v = vars(SET, SET, SET);
                return implies.of(equal.of(v[0], v[1]), implies.of(equal.of(v[0], v[2]), equal.of(v[1], v[2])));
            });
            addAxiom("Left Equality for Binary Predicate", () -> {
                Expression[] v = vars(SET, SET, SET);
                return implies.of(equal.of(v[0], v[1]), implies.of(elementOf.of(v[0], v[2]), elementOf.of(v[1], v[2])));
            });
            addAxiom("Right Equality for Binary Predicate", () -> {
                Expression[] v = vars(SET, SET, SET);
                return implies.of(equal.of(v[0], v[1]), implies.of(elementOf.of(v[2], v[0]), elementOf.of(v[2], v[1])));
            });
            addAxiom("Quantified Negation", () -> {
                Expression[] v = vars(SET, PREDICATE);
                return implies.of(not.of(forall.of(v[0], v[1])), forall.of(v[0], not.of(forall.of(v[0], v[1]))));
            });
            addAxiom("Quantified Commutation", () -> {
                Expression[] v = vars(SET, SET, PREDICATE);
                return implies.of(forall.of(v[0], forall.of(v[1], v[2])), forall.of(v[1], forall.of(v[0], v[2])));
            });
            addAxiom("Substitution", () -> {
                Expression[] v = vars(SET, SET, PREDICATE);
                return implies.of(equal.of(v[0], v[1]), implies.of(forall.of(v[1], v[2]), forall.of(v[0], implies.of(equal.of(v[0], v[1]), v[2]))));
            });
            addAxiom("Quantified Equality", () -> {
                Expression[] v = vars(SET, SET, SET);
                return implies.of(not.of(equal.of(v[0], v[1])), implies.of(equal.of(v[1], v[2]), forall.of(v[0], equal.of(v[1], v[2]))));
            });
        }

        // Set Theory
        // [Insert things here]
        //
        // Definitions
        if (true) {
            equiv = opBB2B((p1, p2) -> "(" + p1 + " ↔ " + p2 + ")");
            addAxiom("Definition of ↔ (part 1)", () -> {
                Expression[] v = vars(PREDICATE, PREDICATE);
                return implies.of(equiv.of(v[0], v[1]), implies.of(v[0], v[1]));
            });
            addAxiom("Definition of ↔ (part 2)", () -> {
                Expression[] v = vars(PREDICATE, PREDICATE);
                return implies.of(equiv.of(v[0], v[1]), implies.of(v[1], v[0]));
            });
            addAxiom("Definition of ↔ (part 3)", () -> {
                Expression[] v = vars(PREDICATE, PREDICATE);
                return implies.of(implies.of(v[0], v[1]), implies.of(implies.of(v[1], v[0]), equiv.of(v[0], v[1])));
            });

            or = opBB2B((p1, p2) -> "(" + p1 + " ∨ " + p2 + ")");
            addAxiom("Definition of ∨", () -> {
                Expression[] v = vars(PREDICATE, PREDICATE);
                return equiv.of(or.of(v[0], v[1]), implies.of(not.of(v[0]), v[1]));
            });

            and = opBB2B((p1, p2) -> "(" + p1 + " ∧ " + p2 + ")");
            addAxiom("Definition of ∧", () -> {
                Expression[] v = vars(PREDICATE, PREDICATE);
                return equiv.of(and.of(v[0], v[1]), not.of(or.of(not.of(v[0]), not.of(v[1]))));
            });
        }

        if (false) {
            exists = opSB2B((v, p) -> "∃" + v + ":" + p);
            addAxiom("Definition of ∃", () -> {
                Expression[] v = vars(SET, PREDICATE);
                return equiv.of(exists.of(v[0], v[1]), not.of(forall.of(v[0], not.of(v[1]))));
            });

            subset = opSS2B((s1, s2) -> s1 + "⊂" + s2);
            addAxiom("Definition of ⊂", () -> {
                Expression[] v = vars(SET, SET, SET);
                return equiv.of(subset.of(v[0], v[1]), forall.of(v[2], implies.of(elementOf.of(v[2], v[0]), elementOf.of(v[2], v[1]))));
            });

            pair = opSS2S((s1, s2) -> "{" + s1 + ", " + s2 + "}");

            singleton = opS2S(s -> "{" + s + "}");
            addAxiom("Definition of {x}", () -> {
                Expression[] v = vars(SET);
                return equal.of(singleton.of(v[0]), pair.of(v[0], v[0]));
            });
        }

        for (int i = 0; i < AXIOM_NAMES.size(); i++) {
            System.out.println(AXIOM_NAMES.get(i));
            VariableName.clearNames();
            System.out.println(AXIOMS.get(i).toString());
            System.out.println();
        }

        Proof proof = new Proof();
        Expression[] v = vars(PREDICATE, PREDICATE, PREDICATE);

        Expression identity = implies.of(v[0], v[0]);
        Expression idNot = implies.of(not.of(v[0]), not.of(v[0]));
        Expression peirce = implies.of(implies.of(implies.of(v[0], v[1]), v[0]), v[0]);

        Expression notnot1 = implies.of(v[0], not.of(not.of(v[0])));
        Expression con2i = implies.of(implies.of(v[0], not.of(v[1])), implies.of(v[1], not.of(v[0])));
        Expression nsyl3a = implies.of(implies.of(v[0], not.of(v[1])), implies.of(implies.of(v[1], v[1]), implies.of(v[1], not.of(v[0]))));
        Expression nsyl3 = implies.of(implies.of(v[0], not.of(v[1])), implies.of(implies.of(v[2], v[1]), implies.of(v[2], not.of(v[0]))));
        Expression mt2d = implies.of(implies.of(v[0], v[2]), implies.of(implies.of(v[0], implies.of(v[1], not.of(v[2]))), implies.of(v[0], not.of(v[1]))));

        proof.addKnown(v[0]);
        Expression goal = not.of(not.of(v[0]));

        VariableName.clearNames();
        proof.prove(goal);
        System.out.println(proof);
    }

    public static void addAxiom(String name, Supplier<Expression> s) {
        AXIOM_NAMES.add(name);
        AXIOMS.add(s.get());
    }
}
