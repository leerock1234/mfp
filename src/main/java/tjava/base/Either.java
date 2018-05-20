package tjava.base;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class Either<T, U> {

    public abstract <B> Either<T, B> map(Function<U, B> f);

    public abstract <B> Either<T, B> flatMap(Function<U, Either<T,B>> f);

    public abstract U getOrElse(Supplier<U> defaultValue);

    private static class Left<T, U> extends Either<T, U> {
        private final T value;

        private Left(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("Left(%s)", value);
        }

        @Override
        public <B> Either<T, B> map(Function<U, B> f) {
            return Either.left(value);
        }

        @Override
        public <B> Either<T, B> flatMap(Function<U, Either<T, B>> f) {
            return Either.left(value);
        }

        @Override
        public U getOrElse(Supplier<U> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Left<?, ?> left = (Left<?, ?>) o;
            return Objects.equals(value, left.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    private static class Right<T, U> extends Either<T, U> {
        private final U value;

        private Right(U value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("Right(%s)", value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Right<?, ?> right = (Right<?, ?>) o;
            return Objects.equals(value, right.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public <B> Either<T, B> map(Function<U, B> f) {
            return Either.right(f.apply(value));
        }

        @Override
        public <B> Either<T, B> flatMap(Function<U, Either<T, B>> f) {
            return f.apply(value);
        }

        @Override
        public U getOrElse(Supplier<U> defaultValue) {
            return value;
        }
    }

    public static <T, U> Either<T, U> left(T value) {
        return new Left<>(value);
    }

    public static <T, U> Either<T, U> right(U value) {
        return new Right<>(value);
    }
}

