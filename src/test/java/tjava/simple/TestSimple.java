package tjava.simple;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tjava.base.Function;
import tjava.base.Utils;

public class TestSimple {

	@Test
	public void testCallAddTwo() {
		assertEquals(3, Simple.addTwo.apply(1).apply(2).intValue());
	}

	@Test
	public void testCompose() {
		Function<Integer, Integer> fun = Utils.compose(Simple.square, Simple.triple);
		assertEquals(64, fun.apply(2).intValue());
	}

	@Test
	public void testHigherCompose() {
		Function<Integer, Integer> fun = Utils.<Integer, Integer, Integer>higerCompose().apply(Simple.inc).apply(Simple.triple);
		assertEquals(9, fun.apply(2).intValue());
	}

	@Test
	public void testHigherAndThen() {
		Function<Integer, Integer> fun = Utils.<Integer, Integer, Integer>higerAndThen().apply(Simple.inc).apply(Simple.triple);
		assertEquals(27, fun.apply(2).intValue());
	}

	@Test
	public void testPartialB() {
		Function<Integer, Function<Integer, Integer>> dec = x->y->x-y;
		Function<Integer, Integer> dec1 = Utils.partialB(1, dec);
		Function<Integer, Integer> dec2 = Utils.partialB(2, dec);

		assertEquals(9, dec1.apply(10).intValue());
		assertEquals(8, dec1.apply(9).intValue());
		assertEquals(7, dec2.apply(9).intValue());
	}
}
