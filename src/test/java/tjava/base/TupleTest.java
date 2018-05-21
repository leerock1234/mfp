package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TupleTest {
	
	@Test
	public void flatMap(){
		Tuple<Integer, Integer> t = new Tuple(1,2);

		assertEquals(new Tuple("2","12"), t.flatMap(x->y->new Tuple(String.valueOf(x*2), String.valueOf(y+10))));
	}
}
