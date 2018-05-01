package tjava.base;

public class Utils {

	public static <U, V, M> Function<U, V> compose(Function<M, V> f1, Function<U, M> f2) {
		return new Function<U, V>() {
			@Override
			public V apply(U arg) {
				return f1.apply(f2.apply(arg));
			}
		};
	}

	public static <U, M, V> Function<Function<M, V>, Function<Function<U, M>, Function<U, V>>> higerCompose() {
		return mvf -> umf -> u -> mvf.apply(umf.apply(u));
	}

	public static <U, M, V> Function<Function<U, M>, Function<Function<M, V>, Function<U, V>>> higerAndThen() {
		return umf -> mvf -> u -> mvf.apply(umf.apply(u));
	}
	
	public static <A, B, C> Function<B, C> partialA(A a, Function<A, Function<B, C>> f) {
		return f.apply(a);
	}

	public static <A, B, C> Function<A, C> partialB(B b, Function<A, Function<B, C>> f) {
		return a -> f.apply(a).apply(b);
	}
	
	public static <A, B, C> Function<A, Function<B, C>> curry(Function<Tuple<A, B>, C> f) {
		return a->b->f.apply(new Tuple<>(a,b));
	}

	public static <A, B, C> Function<A, Function<B, C>> reverseArgs(Function<B, Function<A, C>> f) {
		return a->b->f.apply(b).apply(a);
	}

}
