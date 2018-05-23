package tjava.example;

import tjava.base.List;
import tjava.base.ListUtils;
import tjava.base.utils.JavaRNG;
import tjava.base.utils.RNG;
import tjava.base.utils.Random;

public class GenerateRandomIntegers {
    public static void main(String[] args) {
        List<Integer> l = ListUtils.range(0,9);
        Random<Integer> random = Random.intRnd;

        RNG rng = JavaRNG.rng(0);
        List<RNG> rngs = l.foldLeft(List.list(rng), li->r->li.cons(rng.nextInt()._2));
        List<Integer> ints = rngs.map(x->x.nextInt()._1);
        System.out.println(ints);
    }
}
