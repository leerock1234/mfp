package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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

    @Test
    public void traverseOption(){
        List<Integer> list = List.list(1, 2, 5);

        assertEquals(Option.some(List.list(10,5,2)), ListUtils.traverse(list, x->Option.some(10/x)));
    }

    @Test
    public void traverseToGetNone(){
        List<Integer> list = List.list(1, 0, 5);

        assertEquals(Option.none(), ListUtils.traverse(list, x->x==0?Option.none():Option.some(10/x)));
    }

    @Test
    public void flattenResult(){
        List<Result<Integer>> rIntegers = List.list(Result.success(1),Result.success(2), Result.success(3));

        assertEquals(List.list(1,2,3), List.flattenResult(rIntegers));
    }

    @Test
    public void sequence(){
        List<Result<Integer>> rIntegers = List.list(Result.success(1),Result.success(2), Result.success(3));

        assertEquals(Result.success(List.list(1,2,3)), List.sequence(rIntegers));
    }

    @Test
    public void sequenceIncludingEmpty(){
        List<Result<Integer>> rIntegers = List.list(Result.success(1),Result.empty(), Result.success(3));

        assertEquals(Result.empty(), List.sequence(rIntegers));
    }

    @Test
    public void sequenceIncludingFail(){
        List<Result<Integer>> rIntegers = List.list(Result.success(1),Result.failure("inFail"), Result.success(3));

        assertEquals(Result.failure("inFail"), List.sequence(rIntegers));
    }

    @Test
    public void sequenceEmptyList(){
        List<Result<Integer>> rIntegers = List.list();

        assertEquals(Result.success(List.list()), List.sequence(rIntegers));
    }

    @Test
    public void flatMap(){
        List<Integer> i = List.list(1,2,3);

        assertEquals(List.list(2,3,4), i.flatMap(x->List.list(x+1)));
    }

    @Test
    public void flatMapEmptyList(){
        List<Integer> i = List.list();

        assertEquals(List.list(), i.flatMap(x->List.list(x+1)));
    }

    @Test
    public void conList(){
        List<Integer> i = List.list(1,2,3);
        List<Integer> c = List.list(4,5,6);

        assertEquals(List.list(1,2,3,4,5,6), i.cons(c));
    }

    @Test
    public void exist(){
        List<Integer> i = List.list(1,2,3);

        assertTrue(i.exists(x->x>0));
        assertTrue(i.exists(x->x>1));
        assertTrue(i.exists(x->x>2));
        assertFalse(i.exists(x->x>3));
    }

    @Test
    public void forAll(){
        List<Integer> i = List.list(1,2,3);

        assertTrue(i.forAll(x->x>0));
        assertFalse(i.forAll(x->x>1));
        assertTrue(i.forAll(x->x<4));
        assertFalse(i.forAll(x->x<3));
    }

}
