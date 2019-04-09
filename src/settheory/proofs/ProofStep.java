package settheory.proofs;

import java.util.List;
import settheory.logic.Expression;

public class ProofStep {

    public final Expression e;
    public final List<ProofStep> dependencies;
    public final String justification;
    public final int depth;
    public boolean important;

    public ProofStep(Expression e, List<ProofStep> dependencies, String justification) {
        this.e = e;
        this.dependencies = dependencies;
        this.justification = justification;
        if (dependencies == null || dependencies.isEmpty()) {
            depth = 1;
        } else {
            depth = 1 + dependencies.stream().mapToInt(s -> s.depth).max().getAsInt();
        }
    }

    @Override
    public String toString() {
        return e + "   by " + justification + "\n";
    }
}
