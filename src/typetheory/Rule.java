package typetheory;

import java.util.function.Function;
import java.util.stream.Stream;

public class Rule {

    public final Function<Universe, Stream<Judgement>> outputs;

    public Rule(Function<Universe, Stream<Judgement>> outputs) {
        this.outputs = outputs;
    }
}
