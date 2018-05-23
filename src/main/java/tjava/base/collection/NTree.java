package tjava.base.collection;

import tjava.base.Function;
import tjava.base.List;
import tjava.base.Result;
import tjava.base.TailCall;

import java.util.Objects;

import static tjava.base.utils.Math.log2nlz;
import static tjava.base.utils.UnfoldUtils.unfold;

public abstract class NTree<A extends Comparable<A>> {
    @SuppressWarnings("rawtypes")
    private static NTree EMPTY = new Empty();
    public abstract A value();
    abstract NTree<A> left();
    abstract NTree<A> right();
    public abstract NTree<A> insert(A a);

    public abstract boolean isEmpty();

    public abstract boolean member(A a);

    public abstract <B> B foldLeft(B identity,
                                   Function<B, Function<A, B>> f,
                                   Function<B, Function<B, B>> g);

    public abstract int size();
    public abstract int height();

    public abstract Result<A> max();
    public abstract Result<A> min();
    protected abstract NTree<A> removeMerge(NTree<A> ta);

    public abstract NTree<A> remove(A a);

    public abstract NTree<A> merge(NTree<A> a);

    public abstract <B> B foldRight(B identity,
                                    Function<A, Function<B, B>> f,
                                    Function<B, Function<B, B>> g);

    public abstract <B> B foldInOrder(B identity,
                                      Function<B, Function<A, Function<B, B>>> f);

    public abstract <B> B foldPreOrder(B identity,
                                       Function<A, Function<B, Function<B, B>>> f);

    public abstract <B> B foldPostOrder(B identity,
                                        Function<B, Function<B, Function<A, B>>> f);

    protected abstract NTree<A> rotateLeft();
    protected abstract NTree<A> rotateRight();

    public List<A> toListInOrderRight() {
        return unBalanceRight(List.list(), this).eval();
    }
    private TailCall<List<A>> unBalanceRight(List<A> acc, NTree<A> tree) {
        return tree.isEmpty()
                ? TailCall.ret(acc)
                : tree.left().isEmpty()
                ? TailCall.sus(() ->
                unBalanceRight(acc.cons(tree.value()), tree.right()))
                : TailCall.sus(() -> unBalanceRight(acc, tree.rotateRight()));
    }

    protected abstract NTree<A> ins(A a);

    private static class Empty<A extends Comparable<A>> extends NTree<A> {
        @Override
        public A value() {
            throw new IllegalStateException("value() called on empty");
        }
        @Override
        NTree<A> left() {
            throw new IllegalStateException("left() called on empty");
        }
        @Override
        NTree<A> right() {
            throw new IllegalStateException("right() called on empty");
        }
        @Override
        public String toString() {
            return "E";
        }
        @Override
        public NTree<A> insert(A insertedValue) {
            return ins(insertedValue);
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean member(A a) {
            return false;
        }

        @Override
        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f, Function<B, Function<B, B>> g) {
            return identity;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public Result<A> max() {
            return Result.empty();
        }

        @Override
        public Result<A> min() {
            return Result.empty();
        }

        @Override
        protected NTree<A> removeMerge(NTree<A> ta) {
            return ta;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj!=null && obj instanceof Empty;
        }

        @Override
        public NTree<A> remove(A a) {
            return this;
        }

        @Override
        public NTree<A> merge(NTree<A> a) {
            return a;
        }

        @Override
        public <B> B foldRight(B identity, Function<A, Function<B, B>> f, Function<B, Function<B, B>> g) {
            return identity;
        }

        @Override
        public <B> B foldInOrder(B identity, Function<B, Function<A, Function<B, B>>> f) {
            return identity;
        }

        @Override
        public <B> B foldPreOrder(B identity, Function<A, Function<B, Function<B, B>>> f) {
            return identity;
        }

        @Override
        public <B> B foldPostOrder(B identity, Function<B, Function<B, Function<A, B>>> f) {
            return identity;
        }

        @Override
        protected NTree<A> rotateLeft() {
            return this;
        }

        @Override
        protected NTree<A> rotateRight() {
            return this;
        }

        @Override
        protected NTree<A> ins(A a) {
            return new T<>(empty(), a, empty());
        }
    }
    private static class T<A extends Comparable<A>> extends NTree<A> {
        private final NTree<A> left;
        private final NTree<A> right;
        private final A value;
        private T(NTree<A> left, A value, NTree<A> right) {
            this.left = left;
            this.right = right;
            this.value = value;
        }
        @Override
        public boolean isEmpty() {
            return false;
        }
        @Override
        public A value() {
            return value;
        }
        @Override
        NTree<A> left() {
            return left;
        }
        @Override
        NTree<A> right() {
            return right;
        }
        @Override
        public String toString() {
            return String.format("(T %s %s %s)", left, value, right);
        }
        @Override
        public NTree<A> insert(A insertedValue) {
            NTree<A> t = ins(insertedValue);
            return t.height() > log2nlz(t.size()) * 20 ? balance(t) : t;
        }

        @Override
        protected NTree<A> ins(A a) {
            return a.compareTo(this.value) < 0
                    ? new T<>(left.ins(a), this.value, right)
                    : a.compareTo(this.value) > 0
                    ? new T<>(left, this.value, right.ins(a))
                    : new T<>(this.left, value, this.right);
        }

        @Override
        public boolean member(A value) {
            return value.compareTo(this.value) < 0
                    ? left.member(value)
                    : value.compareTo(this.value) == 0 || right.member(value);
        }
        @Override
        public <B> B foldLeft(B identity,
                              Function<B, Function<A, B>> f,
                              Function<B, Function<B, B>> g) {
            return g.apply(right.foldLeft(identity, f, g))
                    .apply(f.apply(left.foldLeft(identity, f, g)).apply(this.value));
        }

        @Override
        public int size() {
            return 1 + left.size() + right.size();
        }
        @Override
        public int height() {
            return 1 + Math.max(left.height(), right.height());
        }

        @Override
        public Result<A> max() {
            return right.max().orElse(() -> Result.success(value));
        }

        @Override
        public Result<A> min() {
            return left.min().orElse(() -> Result.success(value));
        }

        @Override
        protected NTree<A> removeMerge(NTree<A> ta) {
            if (ta.isEmpty()) {
                return this;
            }
            if (ta.value().compareTo(value) < 0) {
                return new T<>(left.removeMerge(ta), value, right);
            } else if (ta.value().compareTo(value) > 0) {
                return new T<>(left, value, right.removeMerge(ta));
            }
            throw new IllegalStateException("We shouldn't be here");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            T<?> t = (T<?>) o;
            return Objects.equals(left, t.left) &&
                    Objects.equals(right, t.right) &&
                    Objects.equals(value, t.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right, value);
        }

        @Override
        public NTree<A> remove(A a) {
            if (a.compareTo(this.value) < 0) {
                return new T<>(left.remove(a), value, right);
            } else if (a.compareTo(this.value) > 0) {
                return new T<>(left, value, right.remove(a));
            } else {
                return left.removeMerge (right);
            }
        }

        @Override
        public NTree<A> merge(NTree<A> a) {
            if (a.isEmpty()) {
                return this;
            }
            if (a.value().compareTo(this.value) > 0) {
                return new T<>(left, value, right.merge(new T<>(empty(),
                        a.value(), a.right()))).merge(a.left());
            }
            if (a.value().compareTo(this.value) < 0) {
                return new T<>(left.merge(new T<>(a.left(), a.value(),
                        empty())), value, right).merge(a.right());
            }
            return new T<>(left.merge(a.left()), value, right.merge(a.right()));
        }

        @Override
        public <B> B foldRight(B identity,
                               Function<A, Function<B, B>> f,
                               Function<B, Function<B, B>> g) {
            return g.apply(f.apply(this.value).apply(left.foldRight(identity, f, g)))
                    .apply(right.foldRight(identity, f, g));
        }

        @Override
        public <B> B foldInOrder(B identity,
                                 Function<B, Function<A, Function<B, B>>> f) {
            return f.apply(left.foldInOrder(identity, f))
                    .apply(value).apply(right.foldInOrder(identity, f));
        }
        @Override
        public <B> B foldPreOrder(B identity,
                                  Function<A, Function<B, Function<B, B>>> f) {
            return f.apply(value).apply(left.foldPreOrder(identity, f))
                    .apply(right.foldPreOrder(identity, f));
        }
        @Override
        public <B> B foldPostOrder(B identity,
                                   Function<B, Function<B, Function<A, B>>> f) {
            return f.apply(left.foldPostOrder(identity, f))
                    .apply(right.foldPostOrder(identity, f)).apply(value);
        }

        @Override
        protected NTree<A> rotateLeft() {
            return right.isEmpty()
                    ? this
                    : new T<>(new T<>(left, value, right.left()),
                    right.value(), right.right());
        }

        @Override
        protected NTree<A> rotateRight() {
            return left.isEmpty()
                    ? this
                    : new T<>(left.left(), left.value(),
                    new T<>(left.right(), value, right));
        }

    }
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> NTree<A> empty() {
        return EMPTY;
    }

    public static <A extends Comparable<A>> NTree<A> tree(List<A> list) {
        return list.foldLeft(empty(), t -> t::insert);
    }

    @SafeVarargs
    public static <A extends Comparable<A>> NTree<A> tree(A... as) {
        return tree(List.list(as));
    }

    public static <A extends Comparable<A>> boolean lt(A first, A second) {
        return first.compareTo(second) < 0;
    }
    public static <A extends Comparable<A>> boolean lt(A first, A second,
                                                       A third) {
        return lt(first, second) && lt(second, third);
    }

    public static <A extends Comparable<A>> boolean ordered(NTree<A> left,
                                                            A a, NTree<A> right) {
        return left.max().flatMap(lMax -> right.min().map(rMin ->
                lt(lMax, a, rMin))).getOrElse(left.isEmpty() && right.isEmpty())
                || left.min().mapEmpty().flatMap(ignore -> right.min().map(rMin ->
                lt(a, rMin))).getOrElse(false)
                || right.min().mapEmpty().flatMap(ignore -> left.max().map(lMax ->
                lt(lMax, a))).getOrElse(false);
    }

    public static <A extends Comparable<A>> NTree<A> tree(NTree<A> t1,
                                                          A a, NTree<A> t2) {
        return ordered(t1, a, t2)
                ? new T<>(t1, a, t2)
                : ordered(t2, a, t1)
                ? new T<>(t2, a, t1)
                : NTree.<A>empty().insert(a).merge(t1).merge(t2);
    }

    public <B extends Comparable<B>> NTree<B> map(Function<A, B> f) {
        return foldPreOrder(NTree.<B>empty(),
                i -> t1 -> t2 -> NTree.tree(t1, f.apply(i), t2));
    }

    public static <A extends Comparable<A>> boolean isUnBalanced(NTree<A> tree) {
        return Math.abs(tree.left().height() - tree.right().height())
                > (tree.size() - 1) % 2;
    }

    public static <A extends Comparable<A>> NTree<A> balance(NTree<A> tree) {
        return balance_(tree.toListInOrderRight().foldLeft(NTree.<A>empty(),
                t -> a -> new T<>(empty(), a, t)));
    }
    public static <A extends Comparable<A>> NTree<A> balance_(NTree<A> tree) {
        return !tree.isEmpty() && tree.height() > log2nlz(tree.size())
                ? Math.abs(tree.left().height() - tree.right().height()) > 1
                ? balance_(balanceFirstLevel(tree))
                : new T<>(balance_(tree.left()), tree.value(),
                balance_(tree.right()))
                : tree;
    }
    private static <A extends Comparable<A>> NTree<A>
    balanceFirstLevel(NTree<A> tree) {
        return unfold(tree, t -> isUnBalanced(t)
                ? tree.right().height() > tree.left().height()
                ? Result.success(t.rotateLeft())
                : Result.success(t.rotateRight())
                : Result.empty());
    }
}

