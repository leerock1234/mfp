package tjava.base;

import java.util.function.Supplier;

public class MapUtils {

    public static <A,B> Supplier<B> smap(Supplier<A> s, Function<A,B> f){
        return ()->f.apply(s.get());
    }
}
