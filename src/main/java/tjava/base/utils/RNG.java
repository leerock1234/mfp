package tjava.base.utils;

import tjava.base.Tuple;

public interface RNG {
    Tuple<Integer, RNG> nextInt();
}
