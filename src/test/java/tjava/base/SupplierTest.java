package tjava.base;

import org.junit.Test;

import java.util.function.Supplier;

import static junit.framework.TestCase.assertEquals;

public class SupplierTest {

    @Test
    public void supplier(){
        A a = new A(()->1);
        int result = a.getIntNum();
        assertEquals(10, result);
    }
}

class A {
    Supplier<Integer> si;

    public A(Supplier<Integer> si){
        this.si = si;
    }

    public int getIntNum(){
        return si.get()*10;
    }
}
