package tjava.base;

import java.util.function.Supplier;

import static tjava.base.TailCall.ret;
import static tjava.base.TailCall.sus;

public abstract class Stream<A> {
    private static Stream EMPTY = new Empty();

    public abstract A head();

    public abstract Stream<A> tail();

    public abstract Boolean isEmpty();

    public abstract Stream<A> take(int n);

    public abstract Stream<A> drop(int n);

    public abstract Stream<A> takeWhile(Function<A, Boolean> f);

    public Stream<A> dropWhile(Function<A, Boolean> f) {
        return _dropWhile(f).eval();
    }

    public Result<A> headOption() {
        return foldRight(Result::empty, a -> ignore -> Result.success(a));
    }

    public boolean exists(Function<A, Boolean> p) {
        return _exists(p).eval();
    }

    protected abstract TailCall<Stream<A>> _dropWhile(Function<A, Boolean> f);

    protected abstract TailCall<Boolean> _exists(Function<A, Boolean> p);

    public List<A> toList() {
        return List.reverse(toList(this, List.list()).eval());
    }

    public <B> Stream<B> map(Function<A, B> f) {
        return foldRight(Stream::empty, a -> b -> cons(() -> f.apply(a), b));
    }

    public Stream<A> filter(Function<A, Boolean> f) {
        //return foldRight(Stream::empty, a -> b -> f.apply(a)?cons(()->a, b):b.get());
        return _filter(f).eval();
    }

    private TailCall<Stream<A>> _filter(Function<A, Boolean> f) {
        TailCall<Stream<A>> sa = ret(Stream.empty());
        return foldRight(()->sa , a -> b ->
            f.apply(a)?ret(cons(()->a, ()->b.get().eval())):sus(()->b.get())
        );
    }

    public Stream<A> append(Supplier<Stream<A>> s) {
        return foldRight(s, a -> b -> cons(() -> a, b));
    }

    public <B> Stream<B> flatMap(Function<A, Stream<B>> f) {
        return foldRight(Stream::empty, a -> b -> f.apply(a).append(b));
    }

    private TailCall<List<A>> toList(Stream<A> s, List<A> acc) {
        return s.isEmpty()
                ? ret(acc)
                : sus(() -> toList(s.tail(), List.list(s.head()).cons(acc)));
    }

    public <B> B strictFoldRight(Supplier<B> z,
                                 Function<A, Function<Supplier<B>, B>> f){
        return _strictFoldRight(z,f).eval();
    }

    protected abstract <B> TailCall<B> _strictFoldRight(Supplier<B> z,
                                                        Function<A, Function<Supplier<B>, B>> f);

    public abstract <B> B foldRight(Supplier<B> z,
                                    Function<A, Function<Supplier<B>, B>> f);

    public static <A, S> Stream<A> unfold(S z,
                                          Function<S, Result<Tuple<A, S>>> f) {
        return f.apply(z).map(x -> cons(() -> x._1,
                () -> unfold(x._2, f))).getOrElse(empty());
    }

    private Stream() {
    }

    private static class Empty<A> extends Stream<A> {
        @Override
        public Stream<A> tail() {
            throw new IllegalStateException("tail called on empty");
        }

        @Override
        public A head() {
            throw new IllegalStateException("head called on empty");
        }

        @Override
        public Boolean isEmpty() {
            return true;
        }

        public Stream<A> take(int n) {
            return this;
        }

        @Override
        public Stream<A> drop(int n) {
            return this;
        }

        @Override
        public Stream<A> takeWhile(Function<A, Boolean> f) {
            return this;
        }

        @Override
        public Stream<A> dropWhile(Function<A, Boolean> f) {
            return this;
        }

        @Override
        protected TailCall<Stream<A>> _dropWhile(Function<A, Boolean> f) {
            return ret(this);
        }

        protected TailCall<Boolean> _exists(Function<A, Boolean> p) {
            return ret(false);
        }

        @Override
        protected <B> TailCall<B> _strictFoldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {
            return ret(z.get());
        }

        @Override
        public <B> B foldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {
            return z.get();
        }

        @Override
        public String toString() {
            return "Stream: Empty";
        }
    }

    private static class Cons<A> extends Stream<A> {

        private final Supplier<A> head;
        private A h;
        private final Supplier<Stream<A>> tail;
        private Stream<A> t;

        private Cons(Supplier<A> h, Supplier<Stream<A>> t) {
            head = h;
            tail = t;
        }

        @Override
        public A head() {
            if (h == null) {
                h = head.get();
            }
            return h;
        }

        @Override
        public Stream<A> tail() {
            if (t == null) {
                t = tail.get();
            }
            return t;
        }

        @Override
        public String toString() {
            return "Stream: " + head().toString();
        }

        @Override
        public Boolean isEmpty() {
            return false;
        }

        public Stream<A> take(int n) {
            return n <= 0
                    ? empty()
                    : cons(head, () -> tail().take(n - 1));
        }

        @Override
        public Stream<A> drop(int n) {
            return drop(this, n).eval();
        }

        public TailCall<Stream<A>> drop(Stream<A> acc, int n) {
            return n <= 0
                    ? ret(acc)
                    : sus(() -> drop(acc.tail(), n - 1));
        }

        @Override
        public Stream<A> takeWhile(Function<A, Boolean> f) {
            return foldRight(Stream::empty, a ->b->f.apply(a)
                    ? cons(() -> a, b)
                    : empty());
        }

        @Override
        protected TailCall<Stream<A>> _dropWhile(Function<A, Boolean> f) {
            return !f.apply(head.get())
                    ? ret(this)
                    : sus(() -> ((Cons<A>) this.tail())._dropWhile(f))
                    ;
        }

        protected TailCall<Boolean> _exists(Function<A, Boolean> p) {
            return p.apply(head())
                    ? ret(true)
                    : sus(() -> tail()._exists(p))
                    ;
        }

        @Override
        protected <B> TailCall<B> _strictFoldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {
            B b = f.apply(head()).apply(z);
            return sus(()->this.tail()._strictFoldRight(()->b, f));
        }

        @Override
        public <B> B foldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {
            return f.apply(head()).apply(() -> tail().foldRight(z, f));
        }
    }

    public static <A> Stream<A> cons(Supplier<A> hd, Supplier<Stream<A>> tl) {
        return new Cons<>(hd, tl);
    }

    public static <A> Stream<A> cons(Supplier<A> hd, Stream<A> tl) {
        return new Cons<>(hd, () -> tl);
    }

    @SuppressWarnings("unchecked")
    public static <A> Stream<A> empty() {
        return EMPTY;
    }

    public static Stream<Integer> from(int i) {
        return iterate(i, x->x + 1);
    }

    public static <A> Stream<A> repeat(A a) {
        return iterate(a, x->a);
    }

    public static <A> Stream<A> iterate(A seed, Function<A, A> f){
        return unfold(seed, x->Result.success(new Tuple<>(x,f.apply(x))));
    }
}

