package tjava.base;

import java.util.HashMap;
import java.util.Map;

import static tjava.base.List.list;
import static tjava.base.TailCall.ret;
import static tjava.base.TailCall.sus;

public class ListUtils {

	public static List<Integer> range(int start, int end){
		return List.unfold(start, x->x+1, x->x<end);
	}

	public static <A> Option<List<A>> sequence(List<Option<A>> list) {
		return traverse(list, x->x);
	}

	public static <A,B> Option<List<B>> traverse(List<A> list, Function<A, Option<B>> fun) {
		return list.foldRight(Option.some(list()), oo->oa->OptionUtils.map2(oa, fun.apply(oo), x->y->x.cons(y)));
	}

	public static <A,B> Result<List<B>> traverseR(List<A> list, Function<A, Result<B>> fun) {
		return list.foldRight(Result.success(list()), oo->oa->ResultUtils.map2(oa, fun.apply(oo), x->y->x.cons(y)));
	}

	public static <A, B, C> List<C> zipWith(List<A> list1, List<B> list2,
											Function<A, Function<B, C>> f){
		return List.reverse(_zipWith(list1, list2, f, list()).eval());
	}

	public static <A, B, C> TailCall<List<C>> _zipWith(List<A> list1, List<B> list2,
											Function<A, Function<B, C>> f, List<C> acc){
		return (list1.isEmpty() || list2.isEmpty())
			? ret(acc)
			: sus(() -> _zipWith(list1.tail(), list2.tail(), f, acc.cons(f.apply(list1.head()).apply(list2.head()))));
	}

	public static <A,B,C> List<C> map2(List<A> la, List<B> lb, Function<A, Function<B, C>> f){
	    return la.flatMap(a->lb.map(b->f.apply(a).apply(b)));
    }

	public static <A,B,C> List<C> product(List<A> la, List<B> lb, Function<A, Function<B, C>> f){
		return map2(la,lb, f);
	}

    public static <A, B, C> Tuple<List<A>, List<B>> unzip(List<C> list, Function<C, Tuple<A,B>> f){
		return list.foldRight(new Tuple<List<A>, List<B>>(List.NIL, List.NIL),
				c->t->(f.apply(c)).<Tuple<List<A>, List<B>>>flatMap(c_1->c_2->
						new Tuple(t._1.cons(c_1),t._2.cons(c_2))));
	}

	public static <A, B> Tuple<List<A>, List<B>> unzip(List<Tuple<A, B>> list){
		return unzip(list, x->new Tuple(x._1, x._2));
	}

	public static <A> boolean startWith(List<A> list, List<A> check) {
		return list.size() >= check.size() && list.first(check.size()).equals(check);
	}

	public static <A> boolean hasSubSeq(List<A> list, List<A> check) {
		return _hasSubSeq(list, check).eval();
	}

	public static <A> TailCall<Boolean> _hasSubSeq(List<A> list, List<A> check) {
		return ListUtils.startWith(list, check)
				? ret(true)
				: list.isEmpty()
				? ret(false)
				: sus( () -> _hasSubSeq(list.tail(), check) );
	}

    public static <A, B> Map<B, List<A>> groupBy(List<A> list, Function<A, B> f) {
        return list.foldRight(new HashMap<B, List<A>>(), t -> mt -> {
            B k = f.apply(t);
            mt.put(k, mt.get(k)!=null?mt.get(k).cons(t):list(t));
            return mt;
        });
    }

	public static <A, S> List<A> unfold(S z,
										Function<S, Result<Tuple<A, S>>> f) {
		return List.reverse(unfold(list(), z, f).eval());
	}
	private static <A, S> TailCall<List<A>> unfold(List<A> acc, S z,
												   Function<S, Result<Tuple<A, S>>> f) {
		Result<Tuple<A, S>> r = f.apply(z);
		Result<TailCall<List<A>>> result =
				r.map(rt -> sus(() -> unfold(acc.cons(rt._1), rt._2, f)));
		return result.getOrElse(ret(acc));
	}


}
