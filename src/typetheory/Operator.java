package typetheory;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import typetheory.Term.TermCategory;

public class Operator implements TermCategory {

    public final int numInputs;
    private final Function<List<Term>, String> printer;

    public Operator(int numInputs, Function<List<Term>, String> printer) {
        this.numInputs = numInputs;
        this.printer = printer;
    }

    public static Operator op1(Function<Term, String> printer) {
        return new Operator(1, args -> printer.apply(args.get(0)));
    }

    public static Operator op2(BiFunction<Term, Term, String> printer) {
        return new Operator(2, args -> printer.apply(args.get(0), args.get(1)));
    }

    public static Operator op3(TriFunction<Term, Term, Term, String> printer) {
        return new Operator(3, args -> printer.apply(args.get(0), args.get(1), args.get(2)));
    }

    public static Operator op4(QuadFunction<Term, Term, Term, Term, String> printer) {
        return new Operator(4, args -> printer.apply(args.get(0), args.get(1), args.get(2), args.get(3)));
    }

    public Term of(Term... args) {
        if (args.length != numInputs) {
            throw new RuntimeException("Wrong number of arguments");
        }
        return new Term(this, Arrays.asList(args));
    }

    @Override
    public String printTerm(Term t) {
        return printer.apply(t.parts);
    }

    public static interface TriFunction<T, U, V, W> {

        public W apply(T t, U u, V v);
    }

    public static interface QuadFunction<T, U, V, W, X> {

        public X apply(T t, U u, V v, W w);
    }
}
