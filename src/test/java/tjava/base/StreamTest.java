package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class StreamTest {

    @Test
    public void fromTake() {
        List<Integer> list = Stream.from(1).take(10).toList();

        assertEquals(List.list(1,2,3,4,5,6,7,8,9,10), list);
    }

    @Test
    public void takeWhileBigVolume(){
        Stream<Integer> list = Stream.from(1).takeWhile(x->x<10000);
    }

    @Test
    public void takeWhile(){
        Stream<Integer> list = Stream.from(1).takeWhile(x->x<4);

        assertEquals(1, list.head()._1.intValue());
        list = list.tail();
        assertEquals(2, list.head()._1.intValue());
        list = list.tail();
        assertEquals(3, list.head()._1.intValue());
        list = list.tail();
        assertTrue(list.isEmpty());
    }

    @Test
    public void takeWhileEmptyInfinite(){
        Stream<Integer> list = Stream.from(1).takeWhile(x->x>4);

        assertTrue(list.isEmpty());
    }

    @Test
    public void dropWhile() {
        Stream<Integer> list = Stream.from(1).dropWhile(x -> x < 4);

        assertEquals(4, list.head()._1.intValue());
    }

    @Test
    public void dropWhileBigAmount() {
        final int times = 10000;
        Stream<Integer> list = Stream.from(1).dropWhile(x -> x < times);

        assertEquals(times, list.head()._1.intValue());
    }

    @Test
    public void existEmpty(){
        Stream<Integer> list = Stream.from(1).takeWhile(x -> x < 0);

        assertFalse(list.exists(x->x>=4));
    }

    @Test
    public void exist(){
        Stream<Integer> list = Stream.from(1).takeWhile(x -> x < 4);

        assertTrue(list.exists(x->x>=3));
        assertFalse(list.exists(x->x>=4));
    }

    @Test
    public void existBigAmount() {
        Stream<Integer> list = Stream.from(1);

        list.exists(x->x>=11000);
    }

    @Test
    public void foldRight(){
        Stream<Integer> list = Stream.from(1).take(10);

        assertEquals(55, list.foldRight(()->0, x-> y->y.get()+x).intValue());
    }

    @Test(expected = StackOverflowError.class)
    public void foldRightBigVolume(){
        Stream<Integer> list = Stream.from(1).take(10000);

        list.<Integer>foldRight(()->0, x-> y->y.get()+x);
    }

    @Test
    public void map(){
        Stream<Integer> list = Stream.from(0);

        Stream<String> sList = list.map(x->String.valueOf(x+1));

        assertEquals("1", sList.head());
        sList = sList.tail();
        assertEquals("2", sList.head());
        sList = sList.tail();
        assertEquals("3", sList.head());
    }

    @Test
    public void flatMap(){
        Stream<Integer> list = Stream.from(0);

        Stream<String> sList = list.flatMap(x->Stream.cons(()->String.valueOf(x+1), Stream.empty()));

        assertEquals("1", sList.head());
        sList = sList.tail();
        assertEquals("2", sList.head());
        sList = sList.tail();
        assertEquals("3", sList.head());
    }

    @Test
    public void filter(){
        Stream<Integer> list = Stream.from(0);

        Stream<Integer> sList = list.filter(x->x%2==0);

        assertEquals(0, sList.head()._1.intValue());
        sList = sList.tail();
        assertEquals(2, sList.head()._1.intValue());
        sList = sList.tail();
        assertEquals(4, sList.head()._1.intValue());
    }

    @Test
    public void filterBigVolumne(){
        Stream<Integer> list = Stream.from(0);

        Stream<Integer> sList = list.filter(x->x>10000);

        assertEquals(10001, sList.head()._1.intValue());
        sList = sList.tail();
        assertEquals(10002, sList.head()._1.intValue());
    }

    @Test
    public void append(){
        Stream<Integer> list = Stream.from(0);

        Stream<Integer> alist = Stream.from(0).take(2);

        Stream<Integer> adList = alist.append(()->list);

        assertEquals(0, adList.head()._1.intValue());
        adList = adList.tail();
        assertEquals(1, adList.head()._1.intValue());
        adList = adList.tail();
        assertEquals(0, adList.head()._1.intValue());
        adList = adList.tail();
        assertEquals(1, adList.head()._1.intValue());
        adList = adList.tail();
        assertEquals(2, adList.head()._1.intValue());

    }

    @Test
    public void repeat(){
        Stream<String> list = Stream.repeat("abc");

        assertEquals("abc", list.head());
        list = list.tail();
        assertEquals("abc", list.head());
    }

    @Test
    public void iterate(){
        Stream<Integer> list = Stream.iterate(2, x->x+2);

        assertEquals(2, list.head()._1.intValue());
        list = list.tail();
        assertEquals(4, list.head()._1.intValue());
    }

    @Test
    public void unfold(){
        Stream<String> list = Stream.unfold(0, x->x<3?Result.success(new Tuple(String.valueOf(x),x+1)):Result.empty());

        assertEquals("0", list.head());
        list = list.tail();
        assertEquals("1", list.head());
        list = list.tail();
        assertEquals("2", list.head());
        list = list.tail();
        assertTrue(list.isEmpty());
    }

}
