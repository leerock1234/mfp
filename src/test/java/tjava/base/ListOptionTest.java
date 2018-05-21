package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ListOptionTest {

    @Test
    public void headOption(){
        List<Integer> ints = List.list(1,2,3);

        assertEquals(Result.success(3), ints.headOption());
    }

    @Test
    public void lastOption(){
        List<Integer> ints = List.list(1,2,3);

        assertEquals(Result.success(1), ints.lastOption());
    }

    @Test
    public void headOptionFromNil(){
        List<Integer> ints = List.list();

        assertEquals(Result.empty(), ints.headOption());
    }

    @Test
    public void lastOptionFromNil(){
        List<Integer> ints = List.list();

        assertEquals(Result.empty(), ints.lastOption());
    }

    @Test
    public void traverse(){
        List<Integer> list = List.list(1,2,3);

        assertEquals(Result.success(List.list("1","2","3")), ListUtils.traverseR(list, x->Result.success(x.toString())));
    }

    @Test
    public void zipWith(){
        List<Integer> list1 = List.list(4,1,2,3);
        List<Integer> list2 = List.list(-1,-2,-3);

        assertEquals(List.list(2,4,6), ListUtils.zipWith(list1, list2, x->y->x-y));
    }

    @Test
    public void zipWithBigVolume(){
        List<Integer> list1 = ListUtils.range(0,5000);
        List<Integer> list2 = ListUtils.range(0,5000);

        ListUtils.zipWith(list1, list2, x->y->x-y);
    }

    @Test
    public void map2List(){
        List<String> list1 = List.list("a","b","c");
        List<String> list2 = List.list("d","e","f");

        assertEquals(List.list("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"), ListUtils.map2(list1, list2, x->y->x+y));
    }
}
