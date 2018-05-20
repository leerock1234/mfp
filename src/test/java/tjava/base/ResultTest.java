package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ResultTest {

    @Test
    public void getOrElse(){
        Result<Integer> integer = Result.success(1);
        Result<Integer> fail = Result.failure("fail");

        assertEquals(1, integer.getOrElse(-1).intValue());
        assertEquals(-1, fail.getOrElse(-1).intValue());
    }

    @Test
    public void getOrElseUsingSupplier(){
        Result<Integer> integer = Result.success(1);
        Result<Integer> fail = Result.failure("fail");

        assertEquals(1, integer.getOrElse(()->-1).intValue());
        assertEquals(-1, fail.getOrElse(()->-1).intValue());
    }

    @Test
    public void map(){
        Result<Integer> integer = Result.success(1);
        Result<Integer> fail = Result.failure("fail");

        assertEquals(Result.success("1"), integer.map(x->x.toString()));
        assertEquals(Result.failure("fail"), fail.map(x->x.toString()));
    }

    @Test
    public void mapFunctionFail(){
        Result<Integer> integer = Result.success(0);

        assertEquals(Result.failure("/ by zero"), integer.map(x->10/x));
    }

    @Test
    public void flatmap(){
        Result<Integer> integer = Result.success(1);
        Result<Integer> fail = Result.failure("fail");

        assertEquals(Result.success("1"), integer.flatMap(x->Result.success(x.toString())));
        assertEquals(Result.failure("fail"), fail.flatMap(x->Result.success(x.toString())));
    }

    @Test
    public void flatMapFunctionFail(){
        Result<Integer> integer = Result.success(0);

        assertEquals(Result.failure("result is zero"), integer.flatMap(x->x==0?Result.failure("result is zero"):Result.success(10/x)));
    }

    @Test
    public void createFromValue(){
        Result<Integer> integer = Result.of(1);
        Result<Integer> null1 = Result.of(null);

        assertEquals(Result.success(1), integer);
        assertEquals(Result.failure("Null value"), null1);
    }

    @Test
    public void createFromValueWithCustomExceptionMessage(){
        Result<Integer> integer = Result.of(1, "fail");
        Integer nullInt = null;
        Result<Integer> null1 = Result.of(nullInt, "fail");

        assertEquals(Result.success(1), integer);
        assertEquals(Result.failure("fail"), null1);
    }

    @Test
    public void createFromValueAndFunction(){
        Result<Integer> integer1 = Result.of(x->x<2, 1);
        Result<Integer> integer2 = Result.of(x->x<2, 2);
        Result<Integer> null1 = Result.of(x->x<2, (Integer)null);

        assertEquals(Result.success(1), integer1);
        assertEquals(Result.empty(), integer2);
        assertEquals(Result.failure("Null value"), null1);
    }

    @Test
    public void createFromValueAndFunctionWithCustomExceptionMessage(){
        Result<Integer> integer1 = Result.of(x->x<2, 1, "fail");
        Result<Integer> integer2 = Result.of(x->x<2, 2, "fail");
        Result<Integer> null1 = Result.of(x->x<2, null, "fail");

        assertEquals(Result.success(1), integer1);
        assertEquals(Result.empty(), integer2);
        assertEquals(Result.failure("fail"), null1);
    }
}
