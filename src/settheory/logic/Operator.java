package settheory.logic;

import java.util.function.BiFunction;
import java.util.function.Function;
import settheory.logic.Expression.ExpressionCategory;
import settheory.logic.Expression.ExpressionType;
import static settheory.logic.Expression.ExpressionType.PREDICATE;
import static settheory.logic.Expression.ExpressionType.SET;

public class Operator implements ExpressionCategory {

    public final ExpressionType[] inputTypes;
    public final ExpressionType outputType;
    private final Function<Expression[], String> printer;

    private Operator(Function<Expression[], String> printer, ExpressionType outputType, ExpressionType... inputTypes) {
        this.inputTypes = inputTypes;
        this.outputType = outputType;
        this.printer = printer;
    }

    public static Operator opB2B(Function<Expression, String> printer) {
        return new Operator(args -> printer.apply(args[0]), PREDICATE, PREDICATE);
    }

    public static Operator opBB2B(BiFunction<Expression, Expression, String> printer) {
        return new Operator(args -> printer.apply(args[0], args[1]), PREDICATE, PREDICATE, PREDICATE);
    }

    public static Operator opS2S(Function<Expression, String> printer) {
        return new Operator(args -> printer.apply(args[0]), SET, SET);
    }

    public static Operator opSB2B(BiFunction<Expression, Expression, String> printer) {
        return new Operator(args -> printer.apply(args[0], args[1]), PREDICATE, SET, PREDICATE);
    }

    public static Operator opSS2B(BiFunction<Expression, Expression, String> printer) {
        return new Operator(args -> printer.apply(args[0], args[1]), PREDICATE, SET, SET);
    }

    public static Operator opSS2S(BiFunction<Expression, Expression, String> printer) {
        return new Operator(args -> printer.apply(args[0], args[1]), SET, SET, SET);
    }

    public Expression of(Expression... args) {
        if (args.length != inputTypes.length) {
            throw new RuntimeException("Wrong number of arguments");
        }
        for (int i = 0; i < inputTypes.length; i++) {
            if (args[i].type != inputTypes[i]) {
                throw new RuntimeException("Argument " + i + " has incorrect type");
            }
        }
        return new Expression(outputType, this, args, null);
    }

    @Override
    public String printExpression(Expression e) {
        return printer.apply(e.parts);
    }
}
