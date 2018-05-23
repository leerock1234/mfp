package tjava.base.collection.map;

import org.junit.Test;
import tjava.base.List;
import tjava.base.Result;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MapTest {

    @Test
    public void map(){
        Map<Integer, String> map = Map.empty();
        map = map.add(4,"4s");
        map = map.add(5,"5s");

        assertEquals("4s",map.get(4).getContent().getValue().getContent());
        assertEquals("5s",map.get(5).getContent().getValue().getContent());
        assertFalse(map.get(6).isSuccess());
    }

    @Test
    public void getValue(){
        Map<Integer, String> map = Map.empty();
        map = map.add(4,"4s");
        map = map.add(5,"5s");

        assertEquals(Result.success("4s"),map.getValue(4));
        assertEquals(Result.success("5s"),map.getValue(5));
        assertEquals(Result.empty(), map.getValue(6));
    }

    @Test
    public void foldLeft(){
        Map<String, Integer> map = Map.empty();
        map = map.add("4s",4);
        map = map.add("5s",5);

        assertEquals(9, map.<Integer>foldLeft(0, l->e->l+e.getValue().getContent(),x->y->x+y).intValue());
    }

    @Test
    public void values() {
        Map<String, Integer> map = Map.empty();
        map = map.add("4s", 4);
        map = map.add("7s", 7);
        map = map.add("6s", 6);
        map = map.add("5s", 5);

        assertEquals(List.list(5,4,7,6), map.values());
    }
}
