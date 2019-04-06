package logic;

import variables.SetMetavar;
import java.util.function.Function;

public interface Condition extends Function<SetMetavar, Predicate> {
}
