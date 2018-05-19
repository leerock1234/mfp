package tjava.base;

public class ListUtils {

	public static List<Integer> range(int start, int end){
		return List.unfold(start, x->x+1, x->x<end);
	}

	public static <A> Option<List<A>> sequence(List<Option<A>> list) {
		return list.foldRight(Option.some(List.list()), oo->oa->OptionUtils.flatMap(oa, la->OptionUtils.map(oo, x->la.cons(x))));
	}
}
