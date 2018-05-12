package tjava.base;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FunctionTest {

    @Test
    public void should_be_able_compose_two_functions(){
        Function<Integer, Integer> addTwo = x->x+2;
        Function<Integer, Integer> mulTwo = x->x*2;
        Function<Integer, Integer> composeTwo = Function.<Integer, Integer, Integer>higherAndThen().apply(addTwo).apply(mulTwo);

        assertEquals(8, composeTwo.apply(2).intValue());

    }

    @Test
    public void should_be_able_compose_two_functions_using_higher_compose(){
        Function<Integer, Integer> addTwo = x->x+2;
        Function<Integer, Integer> mulTwo = x->x*2;
        Function<Integer, Integer> composeTwo = Function.<Integer,Integer,Integer>higherCompose().apply(mulTwo).apply(addTwo);

        assertEquals(8, composeTwo.apply(2).intValue());

    }

    @Test
    public void should_be_able_compose_two_lambdas(){
        Function<Integer, Integer> composeTwo = Function.<Integer, Integer, Integer>higherAndThen()
                .apply(x->x+2).apply(x->x*2);

        assertEquals(8, composeTwo.apply(2).intValue());
    }

    @Test
    public void composeShouldWork(){
        Function<Integer, Integer> composeTwo = Function.composeAndThen(x->x+2, x->x*2);

        assertEquals(8, composeTwo.apply(2).intValue());
    }

    Integer cosAndThen(Integer i){
        return Function.composeAndThen((Function<Integer,Integer>) x->x+2, x->x*2).apply(i);
    }

    @Test
    public void lambdaToFunctionUsingAndThen(){
        assertEquals(8, cosAndThen(2).intValue());
    }

    Integer cos(Integer i){
        return Function.compose( x->x*2, (Function<Integer,Integer>)x->x+2).apply(i);
    }

    @Test
    public void lambdaToFunction(){
        assertEquals(8, cos(2).intValue());
    }

    @Test
    public void should_be_able_to_partial_a_parameter(){
        Function<Integer, Function<Integer,Integer>> l = x->y->x/y;
        Function<Integer, Integer> partialedL = Function.partialA(8,l);

        assertEquals(4, partialedL.apply(2).intValue());
    }

    @Test
    public void should_be_able_to_partial_the_other_parameter(){
        Function<Integer, Function<Integer,Integer>> l = x->y->x/y;
        Function<Integer, Integer> partialedL = Function.partialB(2,l);

        assertEquals(4, partialedL.apply(8).intValue());
    }

    @Test
    public void should_be_able_to_curry(){
        Function<Tuple<Integer,Integer>, Integer> l = x->x._1 / x._2;
        Function<Integer, Function<Integer, Integer>> curriedResult = Function.curry(l);

        assertEquals(4, curriedResult.apply(8).apply(2).intValue());
    }
}
