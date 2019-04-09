package settheory.proofs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import settheory.logic.Expression;
import static settheory.logic.Main.implies;

public abstract class InferenceRule {

    public final Proof proof;

    public InferenceRule(Proof proof) {
        this.proof = proof;
    }

    public abstract void onNewStep(ProofStep s);

    public static class ModusPonens extends InferenceRule {

        private final Map<Expression, List<ProofStep>> allImplications = new HashMap();

        public ModusPonens(Proof proof) {
            super(proof);
        }

        public void addStep(ProofStep s1, ProofStep s2) {
            proof.addInference(new ProofStep(
                    s2.e.parts().get(1),
                    Arrays.asList(s1, s2),
                    "Modus Ponens on " + s1.e + " and " + s2.e
            ));
        }

        @Override
        public void onNewStep(ProofStep s) {
            if (s.e.category == implies) {
                allImplications.putIfAbsent(s.e.parts().get(0), new LinkedList());
                allImplications.get(s.e.parts().get(0)).add(s);
                if (proof.known.containsKey(s.e.parts().get(0))) {
                    ProofStep s2 = proof.known.get(s.e.parts().get(0));
                    addStep(s2, s);
                }
            }
            if (allImplications.containsKey(s.e)) {
                for (ProofStep s2 : allImplications.remove(s.e)) {
                    addStep(s, s2);
                }
            }
        }
    }
}
