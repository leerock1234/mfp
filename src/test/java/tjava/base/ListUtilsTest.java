package tjava.base;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void startWithFalse(){
        List<Integer> list = List.list(1,2,3,4,5);
        List<Integer> check = List.list(3,4,5,6);

        assertFalse(ListUtils.startWith(list, check));
    }

    @Test
    public void startWith(){
        List<Integer> list = List.list(1,2,3,4,5);
        List<Integer> check = List.list(3,4,5);

        assertTrue(ListUtils.startWith(list, check));
    }

    @Test
    public void startWithEmpty(){
        List<Integer> list = List.list(1,2,3,4,5);
        List<Integer> check = List.list();

        assertTrue(ListUtils.startWith(list, check));
        assertTrue(ListUtils.startWith(check, check));
    }

    @Test
    public void startWithBigVolumne(){
        List<Integer> list = ListUtils.range(1,5000);
        List<Integer> check = List.list(10000);

        assertFalse(ListUtils.startWith(list, check));
    }

    @Test
    public void hasSubSeq(){
        List<Integer> list = List.list(1,2,3,4,5,6);
        List<Integer> check = List.list(3,4,5);

        assertTrue(ListUtils.hasSubSeq(list, check));
    }

    @Test
    public void hasSubSeqEmpty(){
        List<Integer> list = List.list(1,2,3,4,5,6);
        List<Integer> check = List.list();

        assertTrue(ListUtils.hasSubSeq(list, check));
        assertTrue(ListUtils.hasSubSeq(check, check));
    }

    @Test
    public void hasSubSeqFalse(){
        List<Integer> list = List.list(1,2,3,4,5,6);
        List<Integer> check = List.list(3,4,5,5);

        assertFalse(ListUtils.hasSubSeq(list, check));
    }

    @Test
    public void first(){
        List<Integer> list = List.list(1,2,3,4,5,6);

        assertEquals(List.list(4,5,6), list.first(3));
    }

    @Test
    public void groupBy(){
        List<Payment> payments = List.list(
            new Payment("a",10)
            ,new Payment("b", 30)
            ,new Payment("a", 20)
            ,new Payment("b", 40)
        );
        Map<String, List<Payment>> paymentGroupBy = ListUtils.groupBy(payments, x->x.name);

        assertEquals(List.list(new Payment("a",10),new Payment("a",20)), paymentGroupBy.get("a"));
        assertEquals(List.list(new Payment("b",30),new Payment("b",40)), paymentGroupBy.get("b"));
        assertEquals(2, paymentGroupBy.keySet().size());
    }

    @Test
    public void unfold(){
        assertEquals(List.list(9,8,7,6,5,4,3,2,1,0), ListUtils.unfold(0, i -> i < 10
                ? Result.success(new Tuple<>(i, i + 1))
                : Result.empty()));
    }
}

class Payment {
    String name;
    Integer amount;

    public Payment(String name, Integer amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(name, payment.name) &&
                Objects.equals(amount, payment.amount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, amount);
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
