package typetheory;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
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
import typetheory.Variable.AnonVar;
import typetheory.Variable.NamedVar;

public class Main {

    public static Operator forall, function, apply, letin;
    public static Term implVar = new AnonVar().term();

    public static void main(String[] args) {

        forall = op3((x, T, U) -> "(∀" + x + ":" + T + "," + U + ")");
        function = op3((x, T, u) -> "(λ" + x + ":" + T + "." + u + ")");
        apply = op2((t, u) -> "(" + (t.category == apply ? t.toString().substring(1, t.toString().length() - 1) : t) + " " + u + ")");
        letin = op4((x, t, T, u) -> "let " + x + ":=" + t + ":" + T + " in " + u);

        Rule wEmpty = new Rule(universe -> {
            return Stream.of(new WFJudgement(E0, L0));
        });
        Rule wLocalAssum = new Rule(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category instanceof Sort)
                    .flatMap(j -> universe.undefVars(j.L)
                    .map(v -> new WFJudgement(j.E, j.L.appendAssum(v, j.ts.t1))));
        });
        Rule wLocalDef = new Rule(universe -> {
            return universe.typeJudgements()
                    .flatMap(j -> universe.undefVars(j.L)
                    .map(v -> new WFJudgement(j.E, j.L.appendDef(v, j.ts.t1, j.ts.t2))));
        });
        Rule wGlobalAssum = new Rule(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category instanceof Sort)
                    .flatMap(j -> universe.undefVars(j.E)
                    .map(v -> new WFJudgement(j.E.appendAssum(v, j.ts.t1), j.L)));
        });
        Rule wGlobalDef = new Rule(universe -> {
            return universe.typeJudgements()
                    .flatMap(j -> universe.undefVars(j.E)
                    .map(v -> new WFJudgement(j.E.appendDef(v, j.ts.t1, j.ts.t2), j.L)));
        });
        Rule axProp = new Rule(universe -> {
            return universe.wfJudgements()
                    .map(j -> new TypeJudgement(j.E, j.L, Prop.term(), new Type(1).term()));
        });
        Rule axSet = new Rule(universe -> {
            return universe.wfJudgements()
                    .map(j -> new TypeJudgement(j.E, j.L, Set.term(), new Type(1).term()));
        });
        Rule axType = new Rule(universe -> {
            return universe.wfJudgements()
                    .flatMap(j -> IntStream.range(1, universe.maxType)
                    .mapToObj(i -> new TypeJudgement(j.E, j.L, new Type(i).term(), new Type(i + 1).term())));
        });
        Rule var = new Rule(universe -> {
            return universe.wfJudgements()
                    .flatMap(j -> j.L.allTypeDefs()
                    .map(ts -> new TypeJudgement(j.E, j.L, ts.t1, ts.t2)));
        });
        Rule const_ = new Rule(universe -> {
            return universe.wfJudgements()
                    .flatMap(j -> j.E.allTypeDefs()
                    .map(ts -> new TypeJudgement(j.E, j.L, ts.t1, ts.t2)));
        });
        Rule prodProp = new Rule(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category instanceof Sort)
                    .flatMap(j -> universe.typeJudgements()
                    .flatMap(j2 -> {
                        if (!j2.E.equals(j.E) || !j2.ts.t2.category.equals(Prop) || !j.L.subset(j2.L)) {
                            return Stream.of();
                        }
                        LocalContext L = j2.L.subtract(j.L);
                        if (!L.definitions.isEmpty() || L.assumptions.size() != 1) {
                            return Stream.of();
                        }
                        Entry<Term, Term> e = L.assumptions.entrySet().iterator().next();
                        if (!(e.getKey().category instanceof Variable) || !e.getValue().equals(j.ts.t1)) {
                            return Stream.of();
                        }
                        return Stream.of(new TypeStatement(e.getKey(), j2.ts.t1));
                    })
                    .map(ts -> new TypeJudgement(j.E, j.L, forall.of(ts.t1, j.ts.t1, ts.t2), Prop.term())));
        });
        Rule prodSet = new Rule(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category == Prop || j.ts.t2.category == Set)
                    .flatMap(j -> universe.typeJudgements()
                    .flatMap(j2 -> {
                        if (!j2.E.equals(j.E) || !j2.ts.t2.category.equals(Set) || !j.L.subset(j2.L)) {
                            return Stream.of();
                        }
                        LocalContext L = j2.L.subtract(j.L);
                        if (!L.definitions.isEmpty() || L.assumptions.size() != 1) {
                            return Stream.of();
                        }
                        Entry<Term, Term> e = L.assumptions.entrySet().iterator().next();
                        if (!(e.getKey().category instanceof Variable) || !e.getValue().equals(j.ts.t1)) {
                            return Stream.of();
                        }
                        return Stream.of(new TypeStatement(e.getKey(), j2.ts.t1));
                    })
                    .map(ts -> new TypeJudgement(j.E, j.L, forall.of(ts.t1, j.ts.t1, ts.t2), Set.term())));
        });
        Rule prodType = new Rule(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category instanceof Type)
                    .flatMap(j -> universe.typeJudgements()
                    .flatMap(j2 -> {
                        if (!j2.E.equals(j.E) || !j2.ts.t2.equals(j.ts.t2) || !j.L.subset(j2.L)) {
                            return Stream.of();
                        }
                        LocalContext L = j2.L.subtract(j.L);
                        if (!L.definitions.isEmpty() || L.assumptions.size() != 1) {
                            return Stream.of();
                        }
                        Entry<Term, Term> e = L.assumptions.entrySet().iterator().next();
                        if (!(e.getKey().category instanceof Variable) || !e.getValue().equals(j.ts.t1)) {
                            return Stream.of();
                        }
                        return Stream.of(new TypeStatement(e.getKey(), j2.ts.t1));
                    })
                    .map(ts -> new TypeJudgement(j.E, j.L, forall.of(ts.t1, j.ts.t1, ts.t2), j.ts.t2)));
        });
        Rule lam = new Rule(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t1.category == forall)
                    .flatMap(j -> universe.typeJudgements()
                    .flatMap(j2 -> {
                        if (!j2.E.equals(j.E) || !j2.ts.t2.equals(j.ts.t1.parts.get(2)) || !j.L.subset(j2.L)) {
                            return Stream.of();
                        }
                        LocalContext L = j2.L.subtract(j.L);
                        if (!L.definitions.isEmpty() || L.assumptions.size() != 1) {
                            return Stream.of();
                        }
                        Entry<Term, Term> e = L.assumptions.entrySet().iterator().next();
                        if (!e.getKey().equals(j.ts.t1.parts.get(0)) || !e.getValue().equals(j.ts.t1.parts.get(1))) {
                            return Stream.of();
                        }
                        return Stream.of(j2.ts.t1);
                    })
                    .map(t -> new TypeJudgement(j.E, j.L, function.of(j.ts.t1.parts.get(0), j.ts.t1.parts.get(1), t), j.ts.t1)));
        });
        Rule app = new Rule(universe -> {
            return universe.typeJudgements()
                    .filter(j -> j.ts.t2.category == forall)
                    .flatMap(j -> universe.typeJudgements()
                    .filter(j2 -> j2.E.equals(j.E) && j2.L.equals(j.L))
                    .filter(j2 -> j2.ts.t2.equals(j.ts.t2.parts.get(1)))
                    .map(j2 -> new TypeJudgement(j.E, j.L, apply.of(j.ts.t1, j2.ts.t1),
                    j.ts.t2.parts.get(2).substitute(j.ts.t2.parts.get(0), j2.ts.t1))));
        });
        Rule let = new Rule(universe -> {
            return universe.typeJudgements()
                    .flatMap(j -> universe.typeJudgements()
                    .flatMap(j2 -> {
                        if (!j2.E.equals(j.E) || !j.L.subset(j2.L)) {
                            return Stream.of();
                        }
                        LocalContext L = j2.L.subtract(j.L);
                        if (!L.assumptions.isEmpty() || L.definitions.size() != 1) {
                            return Stream.of();
                        }
                        Entry<Term, TypeStatement> e = L.definitions.entrySet().iterator().next();
                        if (!j.ts.equals(e.getValue())) {
                            return Stream.of();
                        }
                        return Stream.of(new TypeJudgement(j.E, j.L, letin.of(e.getKey(), j.ts.t1, j.ts.t2, j2.ts.t1), j2.ts.t2.substitute(e.getKey(), j.ts.t1)));
                    }));
        });

        List<Rule> allRules = Arrays.asList(
                // wEmpty,
                wLocalAssum,
                // wLocalDef,
                axProp, axSet, axType, var, const_, prodProp, prodSet, prodType, lam, app, let);

        Term x = new AnonVar().term(), y = new AnonVar().term();
        Term False = new NamedVar("False").term();
        Term not = new NamedVar("not").term();
//        Term goal = forall.of(x, Prop.term(), impl(x, x));
        Term goal = forall.of(x, Prop.term(), impl(apply(not, x), apply(not, x)));
//        Term goal = forall.of(x, Prop.term(), impl(x, apply(not, apply(not, x))));
//        Term goal = impl(x, x);
        System.out.println(goal);

//        LocalContext L = new LocalContext()
//                .appendAssum(False, Prop.term())
//                .appendDef(not, function.of(y, Prop.term(), impl(y, False)), impl(Prop.term(), Prop.term()));
//        Universe u = new Universe(Arrays.asList(x, implVar, not, False), 1);
//        u.judgements.add(new WFJudgement(E0, L));
        Universe u = new Universe(Arrays.asList(x, implVar), 1);
        u.judgements.add(new WFJudgement(E0, L0));

        for (int i = 0; i < 30; i++) {
            System.out.println(u.countJudgements(allRules));
            System.out.println(u.countNewJudgements(allRules));
            u.applyAllRules(allRules);
            System.out.println(u.judgements.size());
            u.typeJudgements().filter(j -> j.ts.t2.equals(goal)).forEach(j -> System.out.println(j));
            System.out.println(u);
        }

//        Term N = new NamedVar("N").term(), z = new NamedVar("z").term(), S = new NamedVar("S").term();
//        Term le = new NamedVar("le").term(), lez = new NamedVar("lez").term(), leS = new NamedVar("leS").term();
//        Term ind = new NamedVar("ind").term();
//        Term x = new AnonVar().term(), y = new AnonVar().term(), P = new AnonVar().term(), I = new AnonVar().term();
//        LocalContext L = new LocalContext()
//                .appendAssum(N, new Type(1).term())
//                .appendAssum(z, N)
//                .appendAssum(S, impl(N, N))
//                .appendAssum(le, impl(N, N, Prop.term()))
//                .appendAssum(lez, forall.of(x, N, apply(le, z, x)))
//                .appendAssum(leS, forall.of(x, N, forall.of(y, N,
//                        impl(apply(le, x, y), apply(le, apply(S, x), apply(S, y))))))
//                .appendAssum(ind, forall.of(P, impl(N, Prop.term()), impl(apply(P, z),
//                        forall.of(x, N, impl(apply(P, x), apply(P, apply(S, x)))), forall.of(x, N, apply(P, x)))));
//
//        TypeJudgement goal = new TypeJudgement(E0, L,
//                apply(ind, fun(x, N, apply(le, x, x)), apply(lez, z), fun(x, N, I, apply(le, x, x), apply(leS, x, x, I))),
//                forall.of(x, N, apply(le, x, x)));
//        System.out.println(goal.ts);
//        // Judgement goal = new TypeJudgement(E0, L, apply(leS, z, z, apply(lez, z)), apply(le, apply(S, z), apply(S, z)));
//        // Judgement goal = new TypeJudgement(E0, L, apply.of(lez, z), apply(le, z, z));
//
//        Universe u = new Universe(Arrays.asList(N, z, S, le, lez, leS, x, y, P, I, implVar));
//        u.judgements.add(new WFJudgement(E0, L));
//
//        for (int i = 0; i < 30; i++) {
//            u.applyAllRules(allRules);
//            System.out.println(u.judgements.size());
//            System.out.println(u.judgements.contains(goal));
//            // System.out.println(u);
//            u.typeJudgements()
//                    .filter(j -> j.ts.toString().startsWith("(ind"))
//                    .forEach(j -> System.out.println(j.ts));
//        }
    }

    public static Term apply(Term... a) {
        if (a.length < 2) {
            throw new RuntimeException("Too few arguments");
        }
        if (a.length == 2) {
            return apply.of(a[0], a[1]);
        }
        return apply.of(apply(Arrays.copyOf(a, a.length - 1)), a[a.length - 1]);
    }

    public static Term fun(Term... a) {
        if (a.length < 3) {
            throw new RuntimeException("Too few arguments");
        }
        if (a.length == 3) {
            return function.of(a[0], a[1], a[2]);
        }
        return function.of(a[0], a[1], fun(Arrays.copyOfRange(a, 2, a.length)));
    }

    public static Term impl(Term... a) {
        if (a.length < 2) {
            throw new RuntimeException("Too few arguments");
        }
        if (a.length == 2) {
            return forall.of(implVar, a[0], a[1]);
        }
        return forall.of(implVar, a[0], impl(Arrays.copyOfRange(a, 1, a.length)));
    }
}
