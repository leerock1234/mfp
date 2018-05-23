package tjava.example;

import org.junit.Test;
import tjava.base.Function;
import tjava.base.Tuple;

import java.util.function.Supplier;

import static junit.framework.TestCase.assertEquals;

public class FunctionMap {

    @Test
    public void mapFunctionTheSameSignature(){
        AB ab = FunctionMap::con;
        assertEquals(1L, ab.apply(11).longValue());
    }

    @Test
    public void mapFunction(){
        AB1 ab = FunctionMap::con1;
        assertEquals(1L, ab.get().longValue());
    }

    @Test
    public void mRnd1(){
        Random1<Integer> r1 = RNG2::nextInt;
        RC rc1 = new RC();
        assertEquals(1000,r1.apply(rc1)._1.intValue());
    }

    public static Long con1(){
        return 1L;
    }

    public static Long con(Integer i){
       return 1L;
    }

}

class RC implements RNG2 {

    @Override
    public Tuple<Integer, RNG1> nextInt() {
        return new Tuple(1000, new R1D());
    }
}

class R1D implements  RNG1 {

    @Override
    public Tuple<Integer, RNG1> nextInt() {
        return null;
    }
}

interface AB1 extends Supplier<Long> {

}

interface AB extends Function<Integer, Long> {

}

interface Random1<A> extends Function<RNG2, Tuple<A, RNG1>> {

}

interface RNG1 {
    Tuple<Integer, RNG1> nextInt();
}

interface RNG2 {
    Tuple<Integer, RNG1> nextInt();
}
