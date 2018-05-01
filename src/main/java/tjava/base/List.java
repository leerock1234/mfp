package tjava.base;

import static tjava.base.TailCall.ret;
import static tjava.base.TailCall.sus;

public abstract class List<A> {

	public abstract A head();

	public abstract List<A> tail();

	public abstract boolean isEmpty();
	
	public List<A> con(A head){
		return new Cons(head, this);
	}

	@SuppressWarnings("rawtypes")
	public static final List NIL = new Nil();

	private List() {
	}

	private static class Nil<A> extends List<A> {

		private Nil() {
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
		for (int i = a.length - 1; i >= 0; i--) {
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
		return reverse_(list(list.head()), list.tail());
	}

	public static <A> List<A> reverse_(List<A> result, List<A> list){
		if (list.isEmpty()) return result;
		return reverse_(result.con(list.head()), list.tail());
	}

	public static <A> List<A> flattenResult(List<Result<A>> list) {
		return list.foldRight((List<A>)list(), y->x->y.isSuccess()?x.con(y.getContent()):x);
	}
}
