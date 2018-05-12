package tjava.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class FlattenResultTest {
	
	@Test
	public void list_should_get_only_success_result_into_list(){
		List<Result<Integer>> list = List.list(Result.success(Integer.valueOf(1)),Result.failure("fail"), Result.success(Integer.valueOf(2)));
		List<Integer> resultList = List.flattenResult(list);
		List<Integer> expectedList = List.list(Integer.valueOf(1), Integer.valueOf(2));
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void list_should_equals_when_comparing_two_lists_with_the_same_content(){
		List<Integer> list1 = List.list(Integer.valueOf(1),Integer.valueOf(2));
		List<Integer> list2 = List.list(Integer.valueOf(1),Integer.valueOf(2));
		assertEquals(list1, list2);
	}

	@Test
	public void list_should_equals_when_comparing_two_Nils(){
		List<Integer> list1 = List.NIL;
		List<Integer> list2 = List.NIL;
		assertEquals(list1, list2);
	}

	@Test
	public void result_should_equals_when_comparing_two_same_success(){
		Result result1 = Result.success("1");
		Result result2 = Result.success("1");
		assertEquals(result1, result2);
	}

	@Test
	public void result_should_not_equals_when_comparing_two_diff_success(){
		Result result1 = Result.success("1");
		Result result2 = Result.success("2");
		assertNotEquals(result1, result2);
	}

	@Test
	public void result_should_equals_when_comparing_two_Failures_with_the_same_message(){
		Result result1 = Result.failure("myMes");
		Result result2 = Result.failure("myMes");
		assertEquals(result1, result2);
	}

	@Test
	public void result_should_not_equals_when_comparing_two_Failures_with_the_diff_message(){
		Result result1 = Result.failure("myMes1");
		Result result2 = Result.failure("myMes2");
		assertNotEquals(result1, result2);
	}

	@Test
	public void list_should_be_able_to_get_reverse(){
		List<Integer> list = List.list(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
		List<Integer> resultList = List.reverse(list);
		List<Integer> expectedList = List.list(Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(1));
		assertEquals(expectedList, resultList);
	}
	
	@Test
	public void list_should_be_able_to_use_foldleft_to_calculate(){
		List<Integer> list = List.list(1, 2, 3, 4, 5);
		assertEquals("(((((0 + 1) + 2) + 3) + 4) + 5)",list.foldLeft("0", y -> x -> addISLeft(x, y)));
	}
	
	public static String addISLeft(Integer i, String s) {
		return "(" + s + " + " + i + ")";
	}

	@Test
	public void list_should_be_able_to_use_foldright_to_calculate(){
		List<Integer> list = List.list(1, 2, 3, 4, 5);
		assertEquals("(1 + (2 + (3 + (4 + (5 + 0)))))",list.foldRight("0", x -> y -> addISRight(x, y)));
	}
	
	public static String addISRight(Integer i, String s) {
		return "(" + i + " + " + s + ")";
	}
	
	@Test
	public void list_should_be_able_to_be_filtered(){
		List<Result<Integer>> list = List.list(Result.success(1), Result.success(2), Result.failure("abc"));

		List<Result<Integer>> result = list.filter(a -> a.isSuccess());
		
		List<Result<Integer>> expect = List.list(Result.success(1), Result.success(2));
		assertEquals(expect, result);
	}

	/*@Test
	public void list_should_sequence_option(){
		List<Option<Integer>> list = List.list(Option.some(1), Option.some(2), Option.some(3));
		
		Option<List<Integer>> option = List.sequenceOption(list);
		
		List<Integer> expect = List.list(1,2,3);
		assertEquals(expect, option);
	}*/

}
