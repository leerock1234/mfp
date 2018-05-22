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

        assertEquals(1, list.head().intValue());
        list = list.tail();
        assertEquals(2, list.head().intValue());
        list = list.tail();
        assertEquals(3, list.head().intValue());
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

        assertEquals(4, list.head().intValue());
    }

    @Test
    public void dropWhileBigAmount() {
        final int times = 10000;
        Stream<Integer> list = Stream.from(1).dropWhile(x -> x < times);

        assertEquals(times, list.head().intValue());
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
    public void strictFoldRight(){
        Stream<Integer> list = Stream.from(1).take(10);

        assertEquals(55, list.<Integer>strictFoldRight(()->0, x-> y->y.get()+x).intValue());
    }

    @Test
    public void strictFoldRightBigVolume(){
        Stream<Integer> list = Stream.from(1).take(10000);

        list.<Integer>strictFoldRight(()->0, x-> y->y.get()+x);
    }

    @Test
    public void foldRight(){
        Stream<Integer> list = Stream.from(1).take(10);

        assertEquals(55, list.<Integer>foldRight(()->0, x-> y->y.get()+x).intValue());
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
}
