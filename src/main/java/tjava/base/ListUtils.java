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

	public static <A,B> Result<List<B>> traverseR(List<A> list, Function<A, Result<B>> fun) {
		return list.foldRight(Result.success(List.list()), oo->oa->ResultUtils.map2(oa, fun.apply(oo), x->y->x.cons(y)));
	}

	public static <A, B, C> List<C> zipWith(List<A> list1, List<B> list2,
											Function<A, Function<B, C>> f){
		return List.reverse(_zipWith(list1, list2, f, List.list()).eval());
	}

	public static <A, B, C> TailCall<List<C>> _zipWith(List<A> list1, List<B> list2,
											Function<A, Function<B, C>> f, List<C> acc){
		return (list1.isEmpty() || list2.isEmpty())
			? TailCall.ret(acc)
			: TailCall.sus(() -> _zipWith(list1.tail(), list2.tail(), f, acc.cons(f.apply(list1.head()).apply(list2.head()))));
	}

	public static <A,B,C> List<C> map2(List<A> la, List<B> lb, Function<A, Function<B, C>> f){
	    return null;
    }
}
