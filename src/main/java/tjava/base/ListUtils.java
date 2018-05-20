package tjava.base;

public class ListUtils {

	public static List<Integer> range(int start, int end){
		return List.unfold(start, x->x+1, x->x<end);
	}

	public static <A> Option<List<A>> sequence(List<Option<A>> list) {
		return traverse(list, x->x);
	}

	public static <A,B> Option<List<B>> traverse(List<A> list, Function<A, Option<B>> fun) {
		return list.foldRight(Option.some(List.list()), oo->oa->OptionUtils.map2(oa, fun.apply(oo), x->y->x.cons(y)));
	}
}
