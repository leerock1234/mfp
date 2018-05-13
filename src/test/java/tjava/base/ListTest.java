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
        List<Integer> result = Utils.range(2,5);

        assertEquals(List.list(2,3,4), result);
    }

}
