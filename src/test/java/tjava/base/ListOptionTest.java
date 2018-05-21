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

}
