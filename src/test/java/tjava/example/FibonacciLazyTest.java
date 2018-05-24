package tjava.example;

import org.junit.Test;
import tjava.base.Stream;
import tjava.base.Tuple;

import static junit.framework.TestCase.assertEquals;

public class FibonacciLazyTest {

    @Test
    public void fib() {
        Stream<Integer> is = Stream.iterate(new Tuple<>(0,1), x->new Tuple<>(x._2, x._1+x._2))
                .map(x->x._1);

        assertEquals(0, is.head()._1.intValue());
        is = is.tail();
        assertEquals(1, is.head()._1.intValue());
        is = is.tail();
        assertEquals(1, is.head()._1.intValue());
        is = is.tail();
        assertEquals(2, is.head()._1.intValue());
        is = is.tail();
        assertEquals(3, is.head()._1.intValue());
        is = is.tail();
        assertEquals(5, is.head()._1.intValue());
    }
}
