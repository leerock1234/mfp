package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ListUtilsTest {

    @Test
    public void map2(){
        List<Integer> i = List.list(1,2);
        List<Integer> c = List.list(40,50);

        assertEquals(List.list(41,51,42,52), ListUtils.map2(i, c, x->y->x+y));
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

    @Test
    public void unzip(){
        List<Integer> list = List.list(3,6,9);

        assertEquals(new Tuple(List.list(1,2,3), List.list(2,4,6)), ListUtils.unzip(list, x->new Tuple(x/3,x*2/3)));
    }

    @Test
    public void unzipTuple(){
        List<Tuple<Integer,Integer>> list = List.list(new Tuple(1,2),new Tuple(2,4), new Tuple(3,6));

        assertEquals(new Tuple(List.list(1,2,3), List.list(2,4,6)), ListUtils.unzip(list));
    }
}
