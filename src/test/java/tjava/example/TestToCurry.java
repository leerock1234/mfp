package tjava.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tjava.base.Function;
import tjava.base.Tuple;

public class TestToCurry {
	
	<A, B, C, D> String func(A a, B b, C c, D d) {
		return String.format("%s, %s, %s, %s", a, b, c, d);
	}

	<A,B, C, D> Function<A, Function<B, Function<C, Function<D, String>>>> curriedFunc() { 
		return a -> b -> c -> d -> String.format("%s, %s, %s, %s", a,b,c,d);
	}
	
	@Test
	public void testToCurry(){
		assertEquals(func("1","2","3","4"), 
				curriedFunc().apply("1").apply("2").apply("3").apply("4"));
	}
	
	Function<Tuple<Integer, Integer>, Integer> tuplePlus = a -> a._1 + a._2;
	
}
