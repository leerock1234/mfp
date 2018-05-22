package tjava.base.collection;

import tjava.base.Function;
import tjava.base.List;
import tjava.base.Result;

import java.util.Objects;

public abstract class Tree<A extends Comparable<A>> {
    @SuppressWarnings("rawtypes")
    private static Tree EMPTY = new Empty();
    public abstract A value();
    abstract Tree<A> left();
    abstract Tree<A> right();
    public abstract Tree<A> insert(A a);

    public abstract boolean isEmpty();

    public abstract boolean member(A a);

    public abstract <B> B foldLeft(B identity,
                                   Function<B, Function<A, B>> f,
                                   Function<B, Function<B, B>> g);

    public abstract int size();
    public abstract int height();

    public abstract Result<A> max();
    public abstract Result<A> min();
    protected abstract Tree<A> removeMerge(Tree<A> ta);

    public abstract Tree<A> remove(A a);

    public abstract Tree<A> merge(Tree<A> a);

    public abstract <B> B foldRight(B identity,
                                    Function<A, Function<B, B>> f,
                                    Function<B, Function<B, B>> g);

    public abstract <B> B foldInOrder(B identity,
                                      Function<B, Function<A, Function<B, B>>> f);

    public abstract <B> B foldPreOrder(B identity,
                                       Function<A, Function<B, Function<B, B>>> f);

    public abstract <B> B foldPostOrder(B identity,
                                        Function<B, Function<B, Function<A, B>>> f);


    private static class Empty<A extends Comparable<A>> extends Tree<A> {
        @Override
        public A value() {
            throw new IllegalStateException("value() called on empty");
        }
        @Override
        Tree<A> left() {
            throw new IllegalStateException("left() called on empty");
        }
        @Override
        Tree<A> right() {
            throw new IllegalStateException("right() called on empty");
        }
        @Override
        public String toString() {
            return "E";
        }
        @Override
        public Tree<A> insert(A insertedValue) {
            return new T<>(empty(), insertedValue, empty());
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
        protected Tree<A> removeMerge(Tree<A> ta) {
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
        public Tree<A> remove(A a) {
            return this;
        }

        @Override
        public Tree<A> merge(Tree<A> a) {
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
    }
    private static class T<A extends Comparable<A>> extends Tree<A> {
        private final Tree<A> left;
        private final Tree<A> right;
        private final A value;
        private T(Tree<A> left, A value, Tree<A> right) {
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
        Tree<A> left() {
            return left;
        }
        @Override
        Tree<A> right() {
            return right;
        }
        @Override
        public String toString() {
            return String.format("(T %s %s %s)", left, value, right);
        }
        @Override
        public Tree<A> insert(A insertedValue) {
            return insertedValue.compareTo(this.value) < 0
                    ? new T<>(left.insert(insertedValue), this.value, right)
                    : insertedValue.compareTo(this.value) > 0
                    ? new T<>(left, this.value, right.insert(insertedValue))
                    : new T<>(this.left, insertedValue, this.right);
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
        protected Tree<A> removeMerge(Tree<A> ta) {
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
        public Tree<A> remove(A a) {
            if (a.compareTo(this.value) < 0) {
                return new T<>(left.remove(a), value, right);
            } else if (a.compareTo(this.value) > 0) {
                return new T<>(left, value, right.remove(a));
            } else {
                return left.removeMerge (right);
            }
        }

        @Override
        public Tree<A> merge(Tree<A> a) {
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
    }
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> Tree<A> empty() {
        return EMPTY;
    }

    public static <A extends Comparable<A>> Tree<A> tree(List<A> list) {
        return list.foldLeft(empty(), t -> t::insert);
    }

    @SafeVarargs
    public static <A extends Comparable<A>> Tree<A> tree(A... as) {
        return tree(List.list(as));
    }

    public static <A extends Comparable<A>> boolean lt(A first, A second) {
        return first.compareTo(second) < 0;
    }
    public static <A extends Comparable<A>> boolean lt(A first, A second,
                                                       A third) {
        return lt(first, second) && lt(second, third);
    }

    public static <A extends Comparable<A>> boolean ordered(Tree<A> left,
                                                            A a, Tree<A> right) {
        return left.max().flatMap(lMax -> right.min().map(rMin ->
                lt(lMax, a, rMin))).getOrElse(left.isEmpty() && right.isEmpty())
                || left.min().mapEmpty().flatMap(ignore -> right.min().map(rMin ->
                lt(a, rMin))).getOrElse(false)
                || right.min().mapEmpty().flatMap(ignore -> left.max().map(lMax ->
                lt(lMax, a))).getOrElse(false);
    }

    public static <A extends Comparable<A>> Tree<A> tree(Tree<A> t1,
                                                         A a, Tree<A> t2) {
        return ordered(t1, a, t2)
                ? new T<>(t1, a, t2)
                : ordered(t2, a, t1)
                ? new T<>(t2, a, t1)
                : Tree.<A>empty().insert(a).merge(t1).merge(t2);
    }

    public <B extends Comparable<B>> Tree<B> map(Function<A, B> f) {
        return foldPreOrder(Tree.<B>empty(),
                i -> t1 -> t2 -> Tree.tree(t1, f.apply(i), t2));
    }
}

