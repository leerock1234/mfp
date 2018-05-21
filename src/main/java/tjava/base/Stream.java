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

    public List<A> toList() {
        return List.reverse(toList(this, List.list()).eval());
    }
    private TailCall<List<A>> toList(Stream<A> s, List<A> acc) {
        return s.isEmpty()
                ? ret(acc)
                : sus(() -> toList(s.tail(), List.list(s.head()).cons(acc)));
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
    }

    static <A> Stream<A> cons(Supplier<A> hd, Supplier<Stream<A>> tl) {
        return new Cons<>(hd, tl);
    }

    static <A> Stream<A> cons(Supplier<A> hd, Stream<A> tl) {
        return new Cons<>(hd, () -> tl);
    }

    @SuppressWarnings("unchecked")
    public static <A> Stream<A> empty() {
        return EMPTY;
    }

    public static Stream<Integer> from(int i) {
        return cons(() -> i, () -> from(i + 1));
    }
}

