package tjava.example;

import tjava.base.Function;
import tjava.base.List;
import tjava.base.Stream;

public class TracingTheProcessOfEvaluationLazy {

    private static Stream<Integer> stream =
            Stream.cons(() -> 1,
                    Stream.cons(() -> 2,
                            Stream.cons(() -> 3,
                                    Stream.cons(() -> 4,
                                            Stream.cons(() -> 5, Stream.<Integer>empty())))));
    private static Function<Integer, Integer> f = x -> {
        System.out.println("Mapping " + x);
        return x * 3;
    };
    private static Function<Integer, Boolean> p = x -> {
        System.out.println("Filtering " + x);
        return x % 2 == 0;
    };

    public static void main(String... args) {
        Stream<Integer> result = stream.map(f).filter(p);
        System.out.println(result.toList());
    }
}
