package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ResultUtilTest {

    @Test
    public void filterResult(){
        Result<Integer> integer1 = Result.success(1);
        Result<Integer> integer2 = Result.success(2);
        Result<Integer> fail = Result.failure("fail");

        assertEquals(Result.success(1), ResultUtils.filter(integer1, x->x<2, "compare fail"));
        assertEquals(Result.failure("compare fail"), ResultUtils.filter(integer2, x->x<2, "compare fail"));
        assertEquals(Result.failure("fail"), ResultUtils.filter(fail, x->x<2, "compare fail"));
    }

    @Test
    public void filterResultWithAutoMessage(){
        Result<Integer> integer1 = Result.success(1);
        Result<Integer> integer2 = Result.success(2);
        Result<Integer> fail = Result.failure("fail");

        assertEquals(Result.success(1), ResultUtils.filter(integer1, x->x<2));
        assertEquals(Result.failure("Condition not matched"), ResultUtils.filter(integer2, x->x<2));
        assertEquals(Result.failure("fail"), ResultUtils.filter(fail, x->x<2));
    }

    @Test
    public void filterEmptyResult(){
        Result<Integer> integer1 = Result.success(1);
        Result<Integer> integer2 = Result.success(2);
        Result<Integer> fail = Result.failure("fail");

        assertEquals(Result.success(1), ResultUtils.filterEmpty(integer1, x->x<2));
        assertEquals(Result.empty(), ResultUtils.filterEmpty(integer2, x->x<2));
        assertEquals(Result.failure("fail"), ResultUtils.filterEmpty(fail, x->x<2));
    }

    @Test
    public void liftResultSuccess(){
        Function<Integer, String> f = x->String.valueOf(10/x);
        Function<Result<Integer>, Result<String>> lf = ResultUtils.lift(f);

        assertEquals(Result.success("5"), lf.apply(Result.success(2)));
    }

    @Test
    public void liftResultParameterEmpty(){
        Function<Integer, String> f = x->String.valueOf(10/x);
        Function<Result<Integer>, Result<String>> lf = ResultUtils.lift(f);

        assertEquals(Result.empty(), lf.apply(Result.empty()));
    }

    @Test
    public void liftResultParameterFail(){
        Function<Integer, String> f = x->String.valueOf(10/x);
        Function<Result<Integer>, Result<String>> lf = ResultUtils.lift(f);

        assertEquals(Result.failure("fail"), lf.apply(Result.failure("fail")));
    }

    @Test
    public void liftResultFunctionFail(){
        Function<Integer, String> f = x->String.valueOf(10/x);
        Function<Result<Integer>, Result<String>> lf = ResultUtils.lift(f);

        assertEquals(Result.failure("/ by zero"), lf.apply(Result.success(0)));
    }

    @Test
    public void lift2ResultSuccess(){
        Function<Integer, Function<Integer, String>> f = x->y->String.valueOf(y/x);
        Function<Result<Integer>, Function<Result<Integer>, Result<String>>> lf = ResultUtils.lift2(f);

        assertEquals(Result.success("5"), lf.apply(Result.success(2)).apply(Result.success(10)));
    }

    @Test
    public void lift2ResultFail(){
        Function<Integer, Function<Integer, String>> f = x->y->String.valueOf(y/x);
        Function<Result<Integer>, Function<Result<Integer>, Result<String>>> lf = ResultUtils.lift2(f);

        assertEquals(Result.failure("/ by zero"), lf.apply(Result.success(0)).apply(Result.success(10)));
    }
}
