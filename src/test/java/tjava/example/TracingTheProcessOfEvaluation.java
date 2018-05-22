package tjava.example;

import tjava.base.Function;
import tjava.base.List;

public class TracingTheProcessOfEvaluation {

    private static Function<Integer, Integer> f = x -> {
        System.out.println("Mapping " + x);
        return x * 3;
    };

    private static Function<Integer, Boolean> p = x -> {
        System.out.println("Filtering " + x);
        return x % 2 == 0;
    };

    public static void main(String... args) {
        List<Integer> list = List.list(1, 2, 3, 4, 5).map(f).filter(p);
        System.out.println(list);
    }
}
