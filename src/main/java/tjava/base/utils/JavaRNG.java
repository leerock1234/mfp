package tjava.base.utils;

import tjava.base.Tuple;

import java.util.Random;

public class JavaRNG implements RNG {
    private final Random random;

    private JavaRNG(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public Tuple<Integer, RNG> nextInt() {
        return new Tuple<>(random.nextInt(), this);
    }

    public static RNG rng(long seed) {
        return new JavaRNG(seed);
    }
}
