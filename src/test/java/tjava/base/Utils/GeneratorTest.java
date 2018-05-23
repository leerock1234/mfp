package tjava.base.Utils;

import org.junit.Test;
import tjava.base.List;
import tjava.base.Tuple;
import tjava.base.utils.Generator;
import tjava.base.utils.JavaRNG;
import tjava.base.utils.RNG;

import static junit.framework.TestCase.assertEquals;

public class GeneratorTest {
    @Test
    public void generateInteger() throws Exception {
        RNG rng = JavaRNG.rng(0);
        Tuple<Integer, RNG> t1 = Generator.integer(rng);
        assertEquals(Integer.valueOf(-1155484576), t1._1);
        Tuple<Integer, RNG> t2 = Generator.integer(t1._2);
        assertEquals(Integer.valueOf(-723955400), t2._1);
        Tuple<Integer, RNG> t3 = Generator.integer(t2._2);
        assertEquals(Integer.valueOf(1033096058), t3._1);
    }

    @Test
    public void integers(){
        RNG rng = JavaRNG.rng(0);
        Tuple<List<Integer>, RNG> t = Generator.integers(rng, 3);

        assertEquals(List.list(-1155484576, -723955400, 1033096058), t._1);
        assertEquals(-1690734402,rng.nextInt()._1.intValue());

    }
}
