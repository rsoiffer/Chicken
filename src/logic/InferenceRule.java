package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static logic.Main.implies;

public abstract class InferenceRule {

    public static class Inference {

        public final Expression e;
        public final List<Integer> dependencies;
        public final String justification;

        public Inference(Expression e, List<Integer> dependencies, String justification) {
            this.e = e;
            this.dependencies = dependencies;
            this.justification = justification;
        }

        @Override
        public String toString() {
            return "Inference{" + "e=" + e + ", dependencies=" + dependencies + ", justification=" + justification + '}';
        }
    }

    public abstract List<Inference> allInferences(List<Inference> work);

    public abstract List<Inference> applyToNew(Inference i, List<Inference> work, Map<Expression, Integer> known);

    public static class ModusPonens extends InferenceRule {

        @Override
        public List<Inference> allInferences(List<Inference> work) {
            List<Inference> options = new ArrayList();
            for (int i = 0; i < work.size(); i++) {
                Expression e2 = work.get(i).e;
                if (e2.category == implies) {
                    for (int j = 0; j < work.size(); j++) {
                        Expression e1 = work.get(j).e;
                        if (e2.parts().get(0).equals(e1)) {
                            options.add(new Inference(
                                    e2.parts().get(1),
                                    Arrays.asList(i, j),
                                    "Modus Ponens on " + e1 + " and " + e2
                            ));
                        }
                    }
                }
            }
            return options;
        }

        @Override
        public List<Inference> applyToNew(Inference i, List<Inference> work, Map<Expression, Integer> known) {
            if (i.e.category != implies || !known.containsKey(i.e.parts().get(0))) {
                return Arrays.asList();
            }
            Inference i2 = work.get(known.get(i.e.parts().get(0)));
            return Arrays.asList(new Inference(
                    i.e.parts().get(1),
                    Arrays.asList(known.get(i.e), known.get(i2.e)),
                    "Modus Ponens on " + i.e + " and " + i2.e
            ));
        }
    }
}
