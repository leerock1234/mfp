package tjava.example;

import tjava.base.Function;
import tjava.base.Memoizer;

public class MemoizerDemo {
    private static Integer longCalculation(Integer x) {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException ignored) {
        }
        return x * 2;
    }

    private static Function<Integer, Integer> f =
            MemoizerDemo::longCalculation;

    private static Function<Integer, Integer> g = Memoizer.memoize(f);

    public static void main(String[] args) {
        /*System.out.println("time 1");
        automaticMemoizationExample(1);
        System.out.println("time 2");
        automaticMemoizationExample(1);
        System.out.println("time 3");
        automaticMemoizationExample(2);*/

        automaticMemoizationExample2();
    }

    public static void automaticMemoizationExample(int calValue) {
        long startTime = System.currentTimeMillis();
        Integer result1 = g.apply(calValue);
        long time1 = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        Integer result2 = g.apply(calValue);
        long time2 = System.currentTimeMillis() - startTime;
        System.out.println("result1:"+result1);
        System.out.println("result2:"+result2);
        System.out.println("time1:"+time1);
        System.out.println("time2:"+time2);
    }

    static Function<Integer, Function<Integer, Function<Integer, Integer>>> f3m =
            Memoizer.memoize(x ->
                    Memoizer.memoize(y ->
                            Memoizer.memoize(z ->
                                    longCalculation(x) + longCalculation(y) - longCalculation(z))));

    public static void automaticMemoizationExample2() {
        long startTime = System.currentTimeMillis();
        Integer result1 = f3m.apply(2).apply(3).apply(4);
        long time1 = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        Integer result2 = f3m.apply(2).apply(3).apply(4);
        long time2 = System.currentTimeMillis() - startTime;
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(time1);
        System.out.println(time2);
    }
}
