package tjava.base.collection;

import org.junit.Test;
import tjava.base.List;
import tjava.base.Result;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TreeTest {

    @Test
    public void insertToEmptyRoot(){
        Tree<Integer> t = Tree.empty();
        t=t.insert(1);

        assertEquals(1, t.value().intValue());
        assertTrue(t.left().isEmpty());
        assertTrue(t.right().isEmpty());
    }

    @Test
    public void insertTheSame(){
        Tree<Integer> t = Tree.empty();
        t=t.insert(1);
        t=t.insert(1);

        assertEquals(1, t.value().intValue());
        assertTrue(t.left().isEmpty());
        assertTrue(t.right().isEmpty());
    }

    @Test
    public void insertLarger(){
        Tree<Integer> t = Tree.tree(List.list(2,1));

        assertEquals(1, t.value().intValue());
        assertEquals(2,t.right().value().intValue());
        assertTrue(t.left().isEmpty());
    }

    @Test
    public void insertSmaller(){
        Tree<Integer> t = Tree.tree(List.list(-2,1));

        assertEquals(1, t.value().intValue());
        assertEquals(-2,t.left().value().intValue());
        assertTrue(t.right().isEmpty());
    }

    @Test
    public void insertBigAndThenSmaller(){
        Tree<Integer> t = Tree.tree(List.list(2,3,1));

        assertEquals(1, t.value().intValue());
        assertEquals(3,t.right().value().intValue());
        assertEquals(2,t.right().left().value().intValue());
    }

    @Test
    public void member(){
        Tree<Integer> t = Tree.tree(List.list(1,3,2));

        assertTrue(t.member(2));
        assertFalse(t.member(4));
    }

    @Test
    public void memberEmpty(){
        Tree<Integer> t = Tree.empty();

        assertFalse(t.member(4));
    }

    @Test
    public void size() {
        Tree<Integer> t = Tree.tree(List.list(2,3,1));

        assertEquals(3, t.size());
    }

    @Test
    public void height() {
        Tree<Integer> t = Tree.tree(List.list(-1,1,2,3));

        assertEquals(4, t.height());
    }

    @Test
    public void balanceHeight() {
        Tree<Integer> t = Tree.tree(List.list(-1,1,3,2));

        assertEquals(3, t.height());
    }

    @Test
    public void sizeHeightForEmpty(){
        Tree<Integer> t = Tree.empty();

        assertEquals(0, t.size());
        assertEquals(0, t.height());
    }

    @Test
    public void maxminEmpty(){
        Tree<Integer> t = Tree.empty();

        assertEquals(Result.empty(), t.max());
        assertEquals(Result.empty(), t.min());
    }

    @Test
    public void max(){
        Tree<Integer> t = Tree.tree(List.list(-1,1,3,2));

        assertEquals(3, t.max().getOrElse(0).intValue());
    }

    @Test
    public void min(){
        Tree<Integer> t = Tree.tree(List.list(-1,1,3,2));

        assertEquals(-1, t.min().getOrElse(0).intValue());
    }

    @Test
    public void removeEmpty(){
        Tree<Integer> t = Tree.empty();

        assertEquals(Tree.empty(), t.remove(1));
    }

    @Test
    public void removeNotExisted(){
        Tree<Integer> t = Tree.tree(List.list(-1,1,3,2));

        assertEquals(Tree.tree(List.list(-1,1,3,2)), t.remove(4));
    }

    @Test
    public void remove(){
        Tree<Integer> t = Tree.tree(List.list(-1,1,3,2));

        assertEquals(Tree.tree(List.list(-1,1,2)), t.remove(3));
    }

    @Test
    public void mergeAtTwoSideOfRoot(){
        Tree<Integer> t = Tree.tree(List.list(-10,10,30,20));
        Tree<Integer> tm = Tree.tree(List.list(15,35,25));

        assertEquals(Tree.tree(-10,15,10,35,25,30,20), t.merge(tm));
    }

    @Test
    public void foldLeft(){
        assertEquals("4, 2, 1, 3, 6, 5, 7, Nil",
                Tree.tree(7,5,3,1,6,2,4)
                .foldLeft(List.list(), list -> a -> list.cons(a),
                        x -> y -> x.cons(y)).toString());
    }

    @Test
    public void foldRight(){
        assertEquals("4, 2, 1, 3, 6, 5, 7, Nil",
                Tree.tree(7,5,3,1,6,2,4)
                        .foldRight(List.list(), a -> list -> list.cons(a),
                                x -> y -> y.cons(x)).toString());
    }

    @Test
    public void foldInOrder(){
        assertEquals("(((ST-ST-7)-(ST-ST-5)-6)-((ST-ST-3)-(ST-ST-1)-2)-4)",
                Tree.tree(7,5,3,1,6,2,4)
                        .foldInOrder("ST", list -> a -> y -> "(" +y + "-" +list + "-" + a + ")"));
    }

    @Test
    public void foldPreOrder(){
        assertEquals("(((ST-ST-7)-(ST-ST-5)-6)-((ST-ST-3)-(ST-ST-1)-2)-4)",
                Tree.tree(7,5,3,1,6,2,4)
                        .foldPreOrder("ST", a -> list -> y -> "(" +y + "-" +list + "-" + a + ")"));
    }

    @Test
    public void foldPostOrder(){
        assertEquals("(((ST-ST-1)-(ST-ST-3)-2)-((ST-ST-5)-(ST-ST-7)-6)-4)",
                Tree.tree(7,5,3,1,6,2,4)
                        .foldPostOrder("ST", y-> list -> a -> "(" +y + "-" +list + "-" + a + ")"));
    }

    @Test
    public void mappingTree(){
        Tree<Integer> t = Tree.tree(7,5,3,1,6,2,4);
        Tree<Integer> nt = t.map(x->x*10);

        Tree<Integer> et = Tree.tree(70,50,30,10,60,20,40);
        assertEquals(et,nt);
    }
}
