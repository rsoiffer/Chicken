package typetheory;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static typetheory.Context.E0;
import static typetheory.Context.L0;
import typetheory.Context.LocalContext;
import typetheory.Judgement.TypeJudgement;
import typetheory.Judgement.WFJudgement;
import static typetheory.Operator.*;
import static typetheory.Sort.Prop;
import static typetheory.Sort.Set;
import typetheory.Sort.Type;
import static typetheory.Variable.vars;

public class Main {

    public static Operator forall, function, apply, letin;

    public static void main(String[] args) {

        forall = op3((x, T, U) -> "(∀" + x + ":" + T + "," + U + ")");
        function = op3((x, T, u) -> "(λ" + x + ":" + T + "." + u + ")");
        apply = op2((t, u) -> "(" + t + " " + u + ")");
        letin = op4((x, t, T, u) -> "let " + x + ":=" + t + ":" + T + " in " + u);

        Rule wEmpty = new Rule(0,
                (E, L, a) -> universe -> true,
                (E, L, a) -> new WFJudgement(E0, L0)
        );
        Rule wLocalAssum = new Rule(3,
                (E, L, a) -> universe
                -> (universe.contains(new TypeJudgement(E, L, a.get(0), a.get(1)))
                && a.get(1).category instanceof Sort
                && !L.defines(a.get(2))),
                (E, L, a) -> new WFJudgement(E, L.appendAssum(a.get(2), a.get(0)))
        );
        Rule wLocalDef = new Rule(3,
                (E, L, a) -> universe
                -> (universe.contains(new TypeJudgement(E, L, a.get(0), a.get(1)))
                && !L.defines(a.get(2))),
                (E, L, a) -> new WFJudgement(E, L.appendDef(a.get(2), a.get(0), a.get(1)))
        );
        Rule wGlobalAssum = new Rule(3,
                (E, L, a) -> universe
                -> (universe.contains(new TypeJudgement(E, L0, a.get(0), a.get(1)))
                && a.get(1).category instanceof Sort
                && !E.defines(a.get(2))),
                (E, L, a) -> new WFJudgement(E.appendAssum(a.get(2), a.get(0)), L0)
        );
        Rule wGlobalDef = new Rule(3,
                (E, L, a) -> universe
                -> (universe.contains(new TypeJudgement(E, L0, a.get(0), a.get(1)))
                && !E.defines(a.get(2))),
                (E, L, a) -> new WFJudgement(E.appendDef(a.get(2), a.get(0), a.get(1)), L0)
        );
        Rule axProp = new Rule(0,
                (E, L, a) -> universe -> universe.contains(new WFJudgement(E, L)),
                (E, L, a) -> new TypeJudgement(E, L, Prop.term(), new Type(1).term())
        );
        Rule axSet = new Rule(0,
                (E, L, a) -> universe -> universe.contains(new WFJudgement(E, L)),
                (E, L, a) -> new TypeJudgement(E, L, Set.term(), new Type(1).term())
        );
        Rule axType = new Rule(0,
                (E, L, a) -> universe -> universe.contains(new WFJudgement(E, L)),
                (E, L, a) -> new TypeJudgement(E, L, a.get(0), new Type(((Type) a.get(0).category).i + 1).term())
        );

        Rule2 wEmpty2 = new Rule2(universe -> {
            return Stream.of(new WFJudgement(E0, L0));
        });
        Rule2 wLocalAssum2 = new Rule2(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category instanceof Sort)
                    .flatMap(j -> universe.undefVars(j.L)
                    .map(v -> new WFJudgement(j.E, j.L.appendAssum(v, j.ts.t1))));
        });
        Rule2 wLocalDef2 = new Rule2(universe -> {
            return universe.typeJudgements()
                    .flatMap(j -> universe.undefVars(j.L)
                    .map(v -> new WFJudgement(j.E, j.L.appendDef(v, j.ts.t1, j.ts.t2))));
        });
        Rule2 wGlobalAssum2 = new Rule2(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category instanceof Sort)
                    .flatMap(j -> universe.undefVars(j.E)
                    .map(v -> new WFJudgement(j.E.appendAssum(v, j.ts.t1), j.L)));
        });
        Rule2 wGlobalDef2 = new Rule2(universe -> {
            return universe.typeJudgements()
                    .flatMap(j -> universe.undefVars(j.E)
                    .map(v -> new WFJudgement(j.E.appendDef(v, j.ts.t1, j.ts.t2), j.L)));
        });
        Rule2 axProp2 = new Rule2(universe -> {
            return universe.wfJudgements()
                    .map(j -> new TypeJudgement(j.E, j.L, Prop.term(), new Type(1).term()));
        });
        Rule2 axSet2 = new Rule2(universe -> {
            return universe.wfJudgements()
                    .map(j -> new TypeJudgement(j.E, j.L, Set.term(), new Type(1).term()));
        });
        Rule2 axType2 = new Rule2(universe -> {
            return universe.wfJudgements()
                    .flatMap(j -> IntStream.range(1, 5)
                    .mapToObj(i -> new TypeJudgement(j.E, j.L, new Type(i).term(), new Type(i + 1).term())));
        });
        Rule2 var2 = new Rule2(universe -> {
            return universe.wfJudgements()
                    .flatMap(j -> j.L.allTypeDefs()
                    .map(ts -> new TypeJudgement(j.E, j.L, ts.t1, ts.t2)));
        });
        Rule2 const2 = new Rule2(universe -> {
            return universe.wfJudgements()
                    .flatMap(j -> j.E.allTypeDefs()
                    .map(ts -> new TypeJudgement(j.E, j.L, ts.t1, ts.t2)));
        });

        Predicate<LocalContext> prodProp2Pred = L -> {
            return false;
        };
        Rule2 prodProp2 = new Rule2(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category instanceof Sort)
                    .flatMap(j -> universe.typeJudgements()
                    .filter(j2 -> j2.E.equals(j.E)
                    && j2.ts.t2.category.equals(Prop)
                    && prodProp2Pred.test(j2.L.subtract(j.L)))
                    .map(j2 -> new TypeJudgement(j.E, j.L, forall.of(x, j.ts.t1, j2.ts.t1), Set.term())))
        });

        Term[] v = vars(3);
        System.out.println(forall.of(v[0], apply.of(v[1], v[0]), v[2]));

    }
}
