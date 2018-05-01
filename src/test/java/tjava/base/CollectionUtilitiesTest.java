package tjava.base;

import static org.junit.Assert.assertEquals;
import static tjava.base.CollectionUtilities.foldLeft;
import static tjava.base.CollectionUtilities.foldRight;
import static tjava.base.CollectionUtilities.list;
import static tjava.base.CollectionUtilities.reverse;

import java.util.List;

import org.junit.Test;

public class CollectionUtilitiesTest {

	@Test
	public void testFold() {
		List<Integer> list = list(1, 2, 3, 4, 5);
		Integer result = foldLeft(list, 0, x -> y -> x + y);
		assertEquals(15, result.intValue());
	}

	@Test
	public void testFoldLeftVerification() {
		List<Integer> list = list(1, 2, 3, 4, 5);

		String identity = "0";
		Function<String, Function<Integer, String>> f = x -> y -> addSILeft(x, y);
		String result = foldLeft(list, identity, f);
		assertEquals("(((((0 + 1) + 2) + 3) + 4) + 5)", result);
	}

	String addSILeft(String s, Integer i) {
		return "(" + s + " + " + i + ")";
	}

	@Test
	public void testFoldRightVerification() {
		List<Integer> list = list(1, 2, 3, 4, 5);

		String identity = "0";
		Function<String, Function<Integer, String>> f = x -> y -> addSIRight(x, y);
		String result = foldRight(list, identity, f);
		assertEquals("(1 + (2 + (3 + (4 + (5 + 0)))))", result);
	}

	String addSIRight(String s, Integer i) {
		return "(" + i + " + " + s + ")";
	}

	@Test
	public void testReverse(){
		List<Integer> src = list(1,2,3,4,5);
		List<Integer> rev = reverse(src);
		assertEquals(list(5,4,3,2,1), rev);
		
	}
}
