package tjava.base.collection;

import org.junit.Test;
import tjava.base.List;
import tjava.base.ListUtils;
import tjava.base.Result;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class NTreeTest {

    @Test
    public void insertToEmptyRoot(){
        NTree<Integer> t = NTree.empty();
        t=t.insert(1);

        assertEquals(1, t.value().intValue());
        assertTrue(t.left().isEmpty());
        assertTrue(t.right().isEmpty());
    }

    @Test
    public void insertTheSame(){
        NTree<Integer> t = NTree.empty();
        t=t.insert(1);
        t=t.insert(1);

        assertEquals(1, t.value().intValue());
        assertTrue(t.left().isEmpty());
        assertTrue(t.right().isEmpty());
    }

    @Test
    public void insertLarger(){
        NTree<Integer> t = NTree.tree(List.list(2,1));

        assertEquals(1, t.value().intValue());
        assertEquals(2,t.right().value().intValue());
        assertTrue(t.left().isEmpty());
    }

    @Test
    public void insertSmaller(){
        NTree<Integer> t = NTree.tree(List.list(-2,1));

        assertEquals(1, t.value().intValue());
        assertEquals(-2,t.left().value().intValue());
        assertTrue(t.right().isEmpty());
    }

    @Test
    public void insertBigAndThenSmaller(){
        NTree<Integer> t = NTree.tree(List.list(2,3,1));

        assertEquals(1, t.value().intValue());
        assertEquals(3,t.right().value().intValue());
        assertEquals(2,t.right().left().value().intValue());
    }

    @Test
    public void member(){
        NTree<Integer> t = NTree.tree(List.list(1,3,2));

        assertTrue(t.member(2));
        assertFalse(t.member(4));
    }

    @Test
    public void memberEmpty(){
        NTree<Integer> t = NTree.empty();

        assertFalse(t.member(4));
    }

    @Test
    public void size() {
        NTree<Integer> t = NTree.tree(List.list(2,3,1));

        assertEquals(3, t.size());
    }

    @Test
    public void height() {
        NTree<Integer> t = NTree.tree(List.list(-1,1,2,3));

        assertEquals(4, t.height());
    }

    @Test
    public void balanceHeight() {
        NTree<Integer> t = NTree.tree(List.list(-1,1,3,2));

        assertEquals(3, t.height());
    }

    @Test
    public void sizeHeightForEmpty(){
        NTree<Integer> t = NTree.empty();

        assertEquals(0, t.size());
        assertEquals(0, t.height());
    }

    @Test
    public void maxminEmpty(){
        NTree<Integer> t = NTree.empty();

        assertEquals(Result.empty(), t.max());
        assertEquals(Result.empty(), t.min());
    }

    @Test
    public void max(){
        NTree<Integer> t = NTree.tree(List.list(-1,1,3,2));

        assertEquals(3, t.max().getOrElse(0).intValue());
    }

    @Test
    public void min(){
        NTree<Integer> t = NTree.tree(List.list(-1,1,3,2));

        assertEquals(-1, t.min().getOrElse(0).intValue());
    }

    @Test
    public void removeEmpty(){
        NTree<Integer> t = NTree.empty();

        assertEquals(NTree.empty(), t.remove(1));
    }

    @Test
    public void removeNotExisted(){
        NTree<Integer> t = NTree.tree(List.list(-1,1,3,2));

        assertEquals(NTree.tree(List.list(-1,1,3,2)), t.remove(4));
    }

    @Test
    public void remove(){
        NTree<Integer> t = NTree.tree(List.list(-1,1,3,2));

        assertEquals(NTree.tree(List.list(-1,1,2)), t.remove(3));
    }

    @Test
    public void mergeAtTwoSideOfRoot(){
        NTree<Integer> t = NTree.tree(List.list(-10,10,30,20));
        NTree<Integer> tm = NTree.tree(List.list(15,35,25));

        assertEquals(NTree.tree(-10,15,10,35,25,30,20), t.merge(tm));
    }

    @Test
    public void foldLeft(){
        assertEquals("4, 2, 1, 3, 6, 5, 7, Nil",
                NTree.tree(7,5,3,1,6,2,4)
                .foldLeft(List.list(), list -> a -> list.cons(a),
                        x -> y -> x.cons(y)).toString());
    }

    @Test
    public void foldRight(){
        assertEquals("4, 2, 1, 3, 6, 5, 7, Nil",
                NTree.tree(7,5,3,1,6,2,4)
                        .foldRight(List.list(), a -> list -> list.cons(a),
                                x -> y -> y.cons(x)).toString());
    }

    @Test
    public void foldInOrder(){
        assertEquals("(((ST-ST-7)-(ST-ST-5)-6)-((ST-ST-3)-(ST-ST-1)-2)-4)",
                NTree.tree(7,5,3,1,6,2,4)
                        .foldInOrder("ST", list -> a -> y -> "(" +y + "-" +list + "-" + a + ")"));
    }

    @Test
    public void foldPreOrder(){
        assertEquals("(((ST-ST-7)-(ST-ST-5)-6)-((ST-ST-3)-(ST-ST-1)-2)-4)",
                NTree.tree(7,5,3,1,6,2,4)
                        .foldPreOrder("ST", a -> list -> y -> "(" +y + "-" +list + "-" + a + ")"));
    }

    @Test
    public void foldPostOrder(){
        assertEquals("(((ST-ST-1)-(ST-ST-3)-2)-((ST-ST-5)-(ST-ST-7)-6)-4)",
                NTree.tree(7,5,3,1,6,2,4)
                        .foldPostOrder("ST", y-> list -> a -> "(" +y + "-" +list + "-" + a + ")"));
    }

    @Test
    public void mappingTree(){
        NTree<Integer> t = NTree.tree(7,5,3,1,6,2,4);
        NTree<Integer> nt = t.map(x->x*10);

        NTree<Integer> et = NTree.tree(70,50,30,10,60,20,40);
        assertEquals(et,nt);
    }

    @Test
    public void rotateLeft(){
        NTree<Integer> t = NTree.tree(7,5,3,1,6,2,4);

        assertEquals("(T (T (T (T E 1 E) 2 (T E 3 E)) 4 (T E 5 E)) 6 (T E 7 E))", t.rotateLeft().toString());
    }

    @Test
    public void rotateRight(){
        NTree<Integer> t = NTree.tree(7,5,3,1,6,2,4);

        assertEquals("(T (T E 1 E) 2 (T (T E 3 E) 4 (T (T E 5 E) 6 (T E 7 E))))", t.rotateRight().toString());
    }

    @Test
    public void listInOrder(){
        NTree<Integer> t = NTree.tree(7,5,3,1,6,2,4);
        List<Integer> i = t.toListInOrderRight();

        assertEquals(List.list(1,2,3,4,5,6,7), i);
    }

    @Test
    public void isUnbalance(){
        NTree<Integer> t = NTree.tree(7,6,5,4,3,2,1);

        assertTrue(NTree.isUnBalanced(t));
    }

    @Test
    public void isNotUnbalance(){
        NTree<Integer> t = NTree.tree(7,5,3,1,6,2,4);

        assertFalse(NTree.isUnBalanced(t));
    }

    @Test
    public void balance(){
        NTree<Integer> t = NTree.tree(7,6,5,4,3,2,1);

        assertEquals("(T (T (T E 1 E) 2 (T E 3 E)) 4 (T (T E 5 E) 6 (T E 7 E)))", NTree.balance(t).toString());
    }

    @Test
    public void autoBalance(){
        NTree<Integer> t = NTree.tree(ListUtils.range(0,400));

        assertTrue(t.height()<400);
    }
}
