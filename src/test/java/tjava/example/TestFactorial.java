package tjava.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tjava.base.Function;

public class TestFactorial {

	Function<Integer, Integer> factorial;{
		factorial = n -> n <= 1 ? n : n * factorial.apply(n-1);
	}

	Function<Integer, Integer> factorialFun() {
		return n -> n <= 1 ? n : n * factorialFun().apply(n-1);
	}
	
	@Test
	public void testFactorial(){
		assertEquals(6, factorial.apply(3).intValue());
	}

	@Test
	public void testFactorialFun(){
		assertEquals(6, factorialFun().apply(3).intValue());
	}

}
