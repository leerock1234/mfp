package tjava.base;

import static tjava.base.TailCall.ret;
import static tjava.base.TailCall.sus;
import static tjava.base.Case.*;
import static tjava.base.Result.*;

public abstract class List<A> {

	public abstract A head();

	public abstract List<A> tail();

	public abstract boolean isEmpty();

	public abstract <U> List<U> map(Function<A, U> f) ;

	public List<A> cons(A head){
		return new Cons(head, this);
	}

	@SuppressWarnings("rawtypes")
	public static final List NIL = new Nil();

	private List() {
	}

	public abstract int size();

	private static class Nil<A> extends List<A> {

		@Override
		public <U> List<U> map(Function<A, U> f) {
		    return new Nil<U>();
        }

		private Nil() {
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return getClass().equals(obj.getClass());
		}

		@Override
		public A head() {
			throw new IllegalStateException("head called en empty list");
		}

		@Override
		public List<A> tail() {
			throw new IllegalStateException("tail called en empty list");
		}

		@Override
		public boolean isEmpty() {
			return true;
		}
	}

	private static class Cons<A> extends List<A> {

	    int size;

		@Override
		public <U> List<U> map(Function<A, U> f) {
			Function<A, Function<List<U>, List<U>>> transformF = a->u->u.cons(f.apply(a));
		    return foldRight(new Nil<U>(), transformF);
		}

		@Override
		public int size() {
			return size;
		}

		private final A head;
		private final List<A> tail;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((head == null) ? 0 : head.hashCode());
			result = prime * result + ((tail == null) ? 0 : tail.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			Cons other = (Cons) obj;
			return getClass().equals(obj.getClass()) && head.equals(other.head) && tail.equals(other.tail);
		}

		private Cons(A head, List<A> tail) {
			this.head = head;
			this.tail = tail;
			this.size = tail.size() + 1;
		}

		@Override
		public A head() {
			return head;
		}

		@Override
		public List<A> tail() {
			return tail;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <A> List<A> list() {
		return NIL;
	}


	@SafeVarargs
	public static <A> List<A> list(A... a) {
		List<A> n = list();
		for (int i = 0; i < a.length; i++) {
			n = new Cons<>(a[i], n);
		}
		return n;
	}

	public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
		return foldLeft_(identity, this, f).eval();
	}

	private <B> TailCall<B> foldLeft_(B acc, List<A> list, Function<B, Function<A, B>> f) {
		return list.isEmpty() ? ret(acc) : sus(() -> foldLeft_(f.apply(acc).apply(list.head()), list.tail(), f));
	}

	private <B> TailCall<B> foldRight_(B acc, List<A> ts, Function<A, Function<B, B>> f) {
		return ts.isEmpty() ? ret(acc) : sus(() -> foldRight_(f.apply(ts.head()).apply(acc), ts.tail(), f));
	}

	public <B> B foldRight(B identity, Function<A, Function<B, B>> f) {
		return foldRight_(identity, reverse(this), f).eval();
	}
	
	public static <A> List<A> reverse(List<A> list){
		if (list.isEmpty()) return list;
		return reverse_(list(list.head()), list.tail()).eval();
	}

	public static <A> TailCall<List<A>> reverse_(List<A> result, List<A> list){
		if (list.isEmpty()) return ret(result);
		return sus(() -> reverse_(result.cons(list.head()), list.tail()));
	}

	public static <A> List<A> flattenResult(List<Result<A>> list) {
		return list.foldRight(list(), y->x->y.isSuccess()?x.cons(y.getContent()):x);
	}
	
	public static <A> Result<List<A>> sequence(List<Result<A>> input){
		return null;
	}

/*	public static <A> Option<List<A>> sequenceOption(List<Option<A>> input){
		return input.foldRight(Option.some(List.list()),
				x -> y -> Option.map2(x, y, a -> b -> b.cons(a)));
	}*/

	public List<A> filter(Function<A, Boolean> fun) {
		return foldRight(list(), y->x->fun.apply(y)?x.cons(y):x);
	}

	public static <T> List<T> unfold(T seed, Function<T, T> f, Function<T, Boolean> p){
	    return _unfold(List.list(seed), f, p).eval();
	}

	private static <T> TailCall<List<T>> _unfold(List<T> list, Function<T, T> nextValue, Function<T, Boolean> isStop) {
		Function<List<T>, Result<TailCall<List<T>>>> matcher = s -> match(
				mcase(() -> (success(ret(s)))),
				mcase(() -> isStop.apply(nextValue.apply(s.head())), () -> success(sus(() -> _unfold(s.cons(nextValue.apply(s.head())), nextValue, isStop))))
		);
		return matcher.apply(list).getContent();
	}

}
