package tjava.base;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ListUtilsLongTest {

    @Test
    public void hasSubSeqBigVolumne(){
        List<Integer> list = ListUtils.range(0,10000);
        List<Integer> check = List.list(20000);

        assertFalse(ListUtils.hasSubSeq(list, check));
    }

}
