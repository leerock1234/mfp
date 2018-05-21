package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class StreamTest {

    @Test
    public void fromTake() {
        List<Integer> list = Stream.from(1).take(10).toList();

        assertEquals(List.list(1,2,3,4,5,6,7,8,9,10), list);
    }
}
