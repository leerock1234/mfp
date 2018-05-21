package tjava.base;

import static tjava.base.TailCall.ret;
import static tjava.base.TailCall.sus;
import static tjava.base.Case.*;
import static tjava.base.Result.*;

public abstract class List<A> {

    public abstract A head();

    public abstract List<A> tail();

    public abstract boolean isEmpty();

    public abstract <U> List<U> map(Function<A, U> f);

    public abstract <U> List<U> flatMap(Function<A, List<U>> f);

    public List<A> cons(A head) {
        return new Cons(head, this);
    }

    public List<A> cons(List<A> list) {
        return list.foldRight(this, x -> l -> l.cons(x));
    }

    public boolean exists(Function<A, Boolean> p) {
        //TODO: still have some problem
        return foldLeft(false, x -> y -> x || p.apply(y));
    }

    public Result<A> getAt(int index) {
        return index < 0 || index >= size()
                ? Result.failure("Index out of bound")
                : getAt_(this, index).eval();
    }

    private static <A> TailCall<Result<A>> getAt_(List<A> list, int index) {
        return index == 0
                ? TailCall.ret(Result.success(list.head()))
                : TailCall.sus(() -> getAt_(list.tail(), index - 1));
    }

    public Tuple<List<A>, List<A>> splitAt(int index) {
        return index < 0
                ? splitAt(0)
                : index > size()
                ? splitAt(size())
                : splitAt(list(), List.reverse(this), this.size() - index).eval();
    }

    private TailCall<Tuple<List<A>, List<A>>> splitAt(List<A> acc,
                                                      List<A> list, int i) {
        return i == 0 || list.isEmpty()
                ? ret(new Tuple<>(List.reverse(list), acc))
                : sus(() -> splitAt(acc.cons(list.head()), list.tail(), i - 1));
    }

    @SuppressWarnings("rawtypes")
    public static final List NIL = new Nil();

    private List() {
    }

    public abstract int size();

    public abstract Result<A> headOption();

    public Result<A> lastOption() {
        return foldLeft(Result.empty(), result -> a -> Result.success(a));
    }

    public List<A> first(Integer i) {
        return this.splitAt(i)._1;
    }

    private static class Nil<A> extends List<A> {

        @Override
        public <U> List<U> map(Function<A, U> f) {
            return new Nil<U>();
        }

        @Override
        public <U> List<U> flatMap(Function<A, List<U>> f) {
            return NIL;
        }

        private Nil() {
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Result<A> headOption() {
            return Result.empty();
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

        @Override
        public String toString() {
            return "Nil";
        }
    }

    private static class Cons<A> extends List<A> {

        int size;

        @Override
        public String toString() {
            return head + ", " + this.tail().toString();
        }

        @Override
        public <U> List<U> map(Function<A, U> f) {
            Function<A, Function<List<U>, List<U>>> transformF = a -> u -> u.cons(f.apply(a));
            return foldRight(new Nil<U>(), transformF);
        }

        @Override
        public <U> List<U> flatMap(Function<A, List<U>> f) {
            Function<A, Function<List<U>, List<U>>> transformF = a -> u -> u.cons(f.apply(a));
            return foldRight(new Nil<U>(), transformF);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Result<A> headOption() {
            return Result.success(head());
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

    public static <A> List<A> reverse(List<A> list) {
        if (list.isEmpty()) return list;
        return reverse_(list(list.head()), list.tail()).eval();
    }

    public static <A> TailCall<List<A>> reverse_(List<A> result, List<A> list) {
        if (list.isEmpty()) return ret(result);
        return sus(() -> reverse_(result.cons(list.head()), list.tail()));
    }

    public static <A> List<A> flattenResult(List<Result<A>> list) {
        return list.foldRight(list(), y -> x -> y.isSuccess() ? x.cons(y.getContent()) : x);
    }

    public static <A> Result<List<A>> sequence(List<Result<A>> input) {
        return input.foldRight(Result.success(List.list()), oneResult -> resultList -> ResultUtils.map2(resultList, oneResult, l -> o -> l.cons(o)));
    }

/*	public static <A> Option<List<A>> sequenceOption(List<Option<A>> input){
		return input.foldRight(Option.some(List.list()),
				x -> y -> Option.map2(x, y, a -> b -> b.cons(a)));
	}*/

    public List<A> filter(Function<A, Boolean> fun) {
        return foldRight(list(), y -> x -> fun.apply(y) ? x.cons(y) : x);
    }

    public static <T> List<T> unfold(T seed, Function<T, T> f, Function<T, Boolean> p) {
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
