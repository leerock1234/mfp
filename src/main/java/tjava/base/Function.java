package tjava.base;

/**
 * This is the basic of the functional programming, with one parameters.
 * @param <T>
 * @param <U>
 */
public interface Function<T, U> {
    static <X,Y,Z> Function<Y,Z> partialA(X i, Function<X,Function<Y,Z>> l) {
        return l.apply(i);
    }

    static <X,Y,Z> Function<X,Z> partialB(Y i, Function<X,Function<Y,Z>> l) {
        return x->l.apply(x).apply(i);
    }

    static <X,Y,Z> Function<X,Function<Y,Z>> curry(Function<Tuple<X,Y>,Z> l) {
        return x->y->l.apply(new Tuple<X,Y>(x,y));
    }

    static <X,Y,Z> Function<X,Function<Y,Z>> reverseParameter(Function<Y,Function<X,Z>> l) {
        return x->y->l.apply(y).apply(x);
    }

    static <U> Function<U,U> composeAll(List<Function<U,U>> list) {
        return list.foldLeft(Function.identity(),x->y->Function.compose(x,y));
    }

    U apply(T arg);

	/**
	 * It directly return a function which is trivial. It just return whatever input.
	 * @param <T>
	 * @return
	 */
	static <T> Function<T, T> identity() {
		return t -> t;
	}

	/**
	 * Should be able to higherCompose two function into one function
	 * @return
	 */
	static <X, Y, Z> Function<Function<X, Y>,
                Function<Function<Y, Z>,
                        Function<X, Z>>> higherAndThen() {
                return x -> y -> z -> y.apply(x.apply(z));
    }

    /**
     * It is similar to {@link #higherCompose}
     * @return
     */
    static <X, Y, Z> Function<Function<Y, Z>,
                Function<Function<X, Y>,
                        Function<X, Z>>> higherCompose() {
        return x -> y -> z -> x.apply(y.apply(z));
    }

    static <X, Y, Z> Function<X, Z> composeAndThen(Function<X,Y> f1, Function<Y,Z> f2){
        return Function.<X,Y,Z>higherAndThen().apply(f1).apply(f2);
    }

    static <X, Y, Z> Function<X, Z> compose(Function<Y,Z> f1, Function<X,Y> f2){
        return Function.<X,Y,Z>higherCompose().apply(f1).apply(f2);
    }


}
