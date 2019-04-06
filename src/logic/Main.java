package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import operators.B2BOperator;
import operators.BB2BOperator;
import operators.S2SOperator;
import operators.SS2BOperator;
import operators.SS2SOperator;
import operators.VB2BOperator;
import variables.MetavarAssignment;
import variables.PredicateMetavar;
import variables.SetMetavar;
import variables.VariableName;

public class Main {

    public static final List<String> axiomNames = new ArrayList();
    public static final List<Predicate> axioms = new ArrayList();

    public static void main(String[] args) {

        // Propositional Calculus
        B2BOperator not = new B2BOperator(p -> "¬" + p);
        BB2BOperator implies = new BB2BOperator((p1, p2) -> "(" + p1 + " → " + p2 + ")");

        addAxiom("Axiom Simp", () -> {
            PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar();
            return implies.of(v1, implies.of(v2, v1));
        });
        addAxiom("Axiom Frege", () -> {
            PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar(), v3 = new PredicateMetavar();
            return implies.of(implies.of(v1, implies.of(v2, v3)), implies.of(implies.of(v1, v2), implies.of(v1, v3)));
        });
        addAxiom("Axiom Transp", () -> {
            PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar();
            return implies.of(implies.of(not.of(v1), not.of(v2)), implies.of(v2, v1));
        });

        // Predicate Calculus
        SS2BOperator equal = new SS2BOperator((s1, s2) -> s1 + "=" + s2);
        SS2BOperator elementOf = new SS2BOperator((s1, s2) -> s1 + "∈" + s2);
        VB2BOperator forall = new VB2BOperator((v, p) -> "∀" + v + ":" + p);

        addAxiom("Quantified Implication", () -> {
            SetMetavar v1 = new SetMetavar();
            PredicateMetavar v2 = new PredicateMetavar(), v3 = new PredicateMetavar();
            return implies.of(forall.of(v1, implies.of(v2, v3)), implies.of(forall.of(v1, v2), forall.of(v1, v3)));
        });
        addAxiom("Distinctness", () -> {
            SetMetavar v1 = new SetMetavar();
            PredicateMetavar v2 = new PredicateMetavar();
            return (Predicate) implies.of(v2, forall.of(v1, v2)).reqDistinct(v1, v2);
        });
        addAxiom("Existence", () -> {
            SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar();
            return not.of(forall.of(v1, not.of(equal.of(v1, v2))));
        });
        addAxiom("Equality", () -> {
            SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar(), v3 = new SetMetavar();
            return implies.of(equal.of(v1, v2), implies.of(equal.of(v1, v3), equal.of(v2, v3)));
        });
        addAxiom("Left Equality for Binary Predicate", () -> {
            SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar(), v3 = new SetMetavar();
            return implies.of(equal.of(v1, v2), implies.of(elementOf.of(v1, v3), elementOf.of(v2, v3)));
        });
        addAxiom("Right Equality for Binary Predicate", () -> {
            SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar(), v3 = new SetMetavar();
            return implies.of(equal.of(v1, v2), implies.of(elementOf.of(v3, v1), elementOf.of(v3, v2)));
        });
        addAxiom("Quantified Negation", () -> {
            SetMetavar v1 = new SetMetavar();
            PredicateMetavar v2 = new PredicateMetavar();
            return implies.of(not.of(forall.of(v1, v2)), forall.of(v1, not.of(forall.of(v1, v2))));
        });
        addAxiom("Quantified Commutation", () -> {
            SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar();
            PredicateMetavar v3 = new PredicateMetavar();
            return implies.of(forall.of(v1, forall.of(v2, v3)), forall.of(v2, forall.of(v1, v3)));
        });
        addAxiom("Substitution", () -> {
            SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar();
            PredicateMetavar v3 = new PredicateMetavar();
            return implies.of(equal.of(v1, v2), implies.of(forall.of(v2, v3), forall.of(v1, implies.of(equal.of(v1, v2), v3))));
        });
        addAxiom("Quantified Equality", () -> {
            SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar(), v3 = new SetMetavar();
            return implies.of(not.of(equal.of(v1, v2)), implies.of(equal.of(v2, v3), forall.of(v1, equal.of(v2, v3))));
        });

        // Set Theory
        // [Insert things here]
        //
        // Definitions
        if (false) {
            BB2BOperator equiv = new BB2BOperator((p1, p2) -> "(" + p1 + " ↔ " + p2 + ")");
            addAxiom("Definition of ↔ (part 1)", () -> {
                PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar();
                return implies.of(equiv.of(v1, v2), implies.of(v1, v2));
            });
            addAxiom("Definition of ↔ (part 2)", () -> {
                PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar();
                return implies.of(equiv.of(v1, v2), implies.of(v2, v1));
            });
            addAxiom("Definition of ↔ (part 3)", () -> {
                PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar();
                return implies.of(implies.of(v1, v2), implies.of(implies.of(v2, v1), equiv.of(v1, v2)));
            });

            BB2BOperator or = new BB2BOperator((p1, p2) -> "(" + p1 + " ∨ " + p2 + ")");
            addAxiom("Definition of ∨", () -> {
                PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar();
                return equiv.of(or.of(v1, v2), implies.of(not.of(v1), v2));
            });

            BB2BOperator and = new BB2BOperator((p1, p2) -> "(" + p1 + " ∧ " + p2 + ")");
            addAxiom("Definition of ∧", () -> {
                PredicateMetavar v1 = new PredicateMetavar(), v2 = new PredicateMetavar();
                return equiv.of(and.of(v1, v2), not.of(or.of(not.of(v1), not.of(v2))));
            });

            VB2BOperator exists = new VB2BOperator((v, p) -> "∃" + v + ":" + p);
            addAxiom("Definition of ∃", () -> {
                SetMetavar v1 = new SetMetavar();
                PredicateMetavar v2 = new PredicateMetavar();
                return equiv.of(exists.of(v1, v2), not.of(forall.of(v1, not.of(v2))));
            });

            SS2BOperator subset = new SS2BOperator((s1, s2) -> s1 + "⊂" + s2);
            addAxiom("Definition of ⊂", () -> {
                SetMetavar v1 = new SetMetavar(), v2 = new SetMetavar(), v3 = new SetMetavar();
                return equiv.of(subset.of(v1, v2), forall.of(v3, implies.of(elementOf.of(v3, v1), elementOf.of(v3, v2))));
            });

            SS2SOperator pair = new SS2SOperator((s1, s2) -> "{" + s1 + ", " + s2 + "}");

            S2SOperator singleton = new S2SOperator(s -> "{" + s + "}");
            addAxiom("Definition of {x}", () -> {
                SetMetavar v = new SetMetavar();
                return equal.of(singleton.of(v), pair.of(v, v));
            });
        }

        for (int i = 0; i < axiomNames.size(); i++) {
            System.out.println(axiomNames.get(i));
            VariableName.clearNames();
            System.out.println(axioms.get(i).print());
            System.out.println();
        }
    }

    public static void addAxiom(String name, Supplier<Predicate> s) {
        axiomNames.add(name);
        axioms.add(s.get());
    }

    public static void prove(Predicate goal) {
        List<Predicate> known = new LinkedList();
        List<List<Predicate>> dependencies = new LinkedList();
        List<String> justifications = new LinkedList();

        List<PredicateMetavar> predicateVars = new LinkedList();
        for (int i = 0; i < 5; i++) {
            predicateVars.add(new PredicateMetavar());
        }
        List<SetMetavar> setVars = new LinkedList();
        for (int i = 0; i < 5; i++) {
            setVars.add(new SetMetavar());
        }

        while (true) {
            if (known.isEmpty() || Math.random() < .5) {
                Predicate axiom = axioms.get((int) (Math.random() * axioms.size()));
                MetavarAssignment mva = new MetavarAssignment(axiom);
                mva.fillRandomly(predicateVars, setVars);
            }

            if (known.contains(goal)) {
                break;
            }
        }
        System.out.println("Success!  " + known.size());
    }
}
