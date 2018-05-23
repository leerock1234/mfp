package tjava.base.utils;

import tjava.base.*;

public class Generator {

    public static Tuple<Integer, RNG> integer(RNG rng) {
        return rng.nextInt();
    }

    public static Tuple<Integer, RNG> integer(RNG rng, int limit) {
        Tuple<Integer, RNG> random = rng.nextInt();
        return new Tuple<>(java.lang.Math.abs(random._1 % limit), random._2);
    }

    public static Tuple<List<Integer>, RNG> integers(RNG rng, int length) {
        Tuple<List<Integer>, RNG> result = ListUtils.range(0, length).foldLeft(new Tuple<>(List.list(), rng), tuple -> i -> {
            Tuple<Integer, RNG> t = integer(tuple._2);
            return new Tuple<>(tuple._1.cons(t._1), t._2);
        });
        return result;
    }

    /*
     * This method returns the random values in creation order (not reversed)
     * but it is less efficient because it has to reverse the result.
     */
    public static Tuple<List<Integer>, RNG> integers2(RNG rng, int length) {
        List<Tuple<Integer, RNG>> result = ListUtils.range(0, length).foldLeft(List.list(), lst -> i -> lst.cons(integer(rng)));
        List<Integer> list = List.reverse(result.map(x -> x._1));
        Result<Tuple<List<Integer>, RNG>> result2 = result.headOption().map(tr -> new Tuple<>(list, tr._2));
        return result2.getOrElse(new Tuple<>(List.list(), rng));
    }

    /*
     * An explicitly recursive solution
     */
    public static Tuple<List<Integer>, RNG> integers3(RNG rng, int length) {
        return integers3_(rng, length, List.list()).eval();
    }

    private static TailCall<Tuple<List<Integer>, RNG>> integers3_(RNG rng, int length, List<Integer> xs) {
        if (length <= 0)
            return TailCall.ret(new Tuple<>(xs, rng));
        else {
            Tuple<Integer, RNG> t1 = rng.nextInt();
            return TailCall.sus(() -> integers3_(t1._2, length - 1, xs.cons(t1._1)));
        }
    }

}
