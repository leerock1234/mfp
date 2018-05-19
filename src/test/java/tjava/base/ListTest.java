package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ListTest {

    @Test
    public void listMap(){
        List<Integer> l = List.list(1,2,3,4);

        List<String> result = l.map(a->((Integer)(a+1)).toString());

        assertEquals(List.list("2","3","4","5"), result);
    }

    @Test
    public void listHead(){
        List<Integer> l = List.list(1,2,3,4);

        Integer i = l.head();

        assertEquals(4, i.intValue());
    }

    @Test
    public void unfold(){
        List<Integer> result = List.unfold(2, x->x+2, x->x<10);

        assertEquals(List.list(2,4,6,8), result);
    }

    @Test
    public void unfold2000(){
        List<Integer> result = List.unfold(1, x->x+1, x->x<=2000);

        assertEquals(2000, result.size());
    }

    @Test
    public void rangeMethod(){
        List<Integer> result = ListUtils.range(2,5);

        assertEquals(List.list(2,3,4), result);
    }

    @Test
    public void sequenceOption(){
        List<Option<Integer>> list = List.list(Option.some(1), Option.some(2), Option.some(3));

        assertEquals(Option.some(List.list(1,2,3)), ListUtils.sequence(list));
    }

    @Test
    public void sequenceToGetNone(){
        List<Option<Integer>> list = List.list(Option.some(1), Option.none(), Option.some(3));

        assertEquals(Option.none(), ListUtils.sequence(list));
    }

    @Test
    public void sequenceLargeOption(){
        List<Option<Integer>> list = ListUtils.range(0,10000).map(x->Option.some(x));

        ListUtils.sequence(list);
    }

}
