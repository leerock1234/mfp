package tjava.base.collection;

import org.junit.Test;
import tjava.base.List;
import tjava.base.ListUtils;
import tjava.base.Tuple;

import static junit.framework.TestCase.assertEquals;

public class HeapTest {

    @Test
    public void insert(){
        Heap<Integer> h1 = Heap.heap(4,3,2,5,6);
        List<Integer> lp = ListUtils.unfold(h1, hp -> hp.head().flatMap(h -> hp.tail().map(t -> new Tuple<>(h, t))));

        assertEquals(List.list(6,5,4,3,2), lp);
    }
}
