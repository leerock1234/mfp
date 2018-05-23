package tjava.base.collection;

import org.junit.Test;
import tjava.base.List;

import static junit.framework.TestCase.assertEquals;

public class TreeTest {

    @Test
    public void balance(){
        Tree<Integer> t = Tree.tree(List.list(1));
        assertEquals("(T B E 1 E)", t.toString());

        t=t.insert(2);
        assertEquals("(T B E 1 (T R E 2 E))", t.toString());

        t=t.insert(3);
        assertEquals("(T B (T B E 1 E) 2 (T B E 3 E))", t.toString());

        t=t.insert(4);
        assertEquals("(T B (T B E 1 E) 2 (T B E 3 (T R E 4 E)))", t.toString());

        t=t.insert(5);
        assertEquals("(T B (T B E 1 E) 2 (T R (T B E 3 E) 4 (T B E 5 E)))", t.toString());

        t=t.insert(6);
        assertEquals("(T B (T B E 1 E) 2 (T R (T B E 3 E) 4 (T B E 5 (T R E 6 E))))", t.toString());

        t=t.insert(7);
        assertEquals("(T B (T B (T B E 1 E) 2 (T B E 3 E)) 4 (T B (T B E 5 E) 6 (T B E 7 E)))", t.toString());
    }
}
