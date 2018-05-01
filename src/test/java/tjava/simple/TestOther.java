package tjava.simple;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tjava.base.Function;

public class TestOther {
	
	@Test
	public void testAMethod(){
		A oa = new A();
		Function<Integer, Integer> des = A::des;
		Function<Integer, Integer> inc = oa::inc;
		Function<Integer, Integer> inc1 = oa.inc1();
		assertEquals(2, des.apply(3).intValue());
		assertEquals(4, inc.apply(3).intValue());
		assertEquals(4, inc.apply(3).intValue());
	}
	
}

class A {
	int inc(int a){
		return ++a;
	}
	
	Function<Integer, Integer> inc1(){
		return this::inc;
	}
	
	static int des(int a){
		return --a;
	}
}
