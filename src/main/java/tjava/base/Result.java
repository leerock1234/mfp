package tjava.base;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class Result<T> {

	public abstract boolean isSuccess();
	
	public abstract T getContent();

	public abstract T getOrElse(final T defaultValue);
	public abstract T getOrElse(final Supplier<T> defaultValue);
    public abstract Result<T> orElse(final Supplier<Result<T>> defaultResult);
	public abstract <U> Result<U> map(Function<T, U> f);
	public abstract <U> Result<U> flatMap(Function<T, Result<U>> f);
	/*public Result<T> orElse(Supplier<Result<T>> defaultValue);*/

    @SuppressWarnings("rawtypes")
    private static Result empty = new Empty();

    public abstract void forEach(Effect<T> ef);

    public abstract boolean isEmpty();

    public abstract void forEachOrThrow(Effect<T> ef);

    public abstract Result<RuntimeException> forEachOrException(Effect<T> ef);

    public abstract Result<T> mapEmpty();

    private static class Empty<V> extends Result<V> {

        @Override
        public boolean isEmpty(){
            return true;
        }

        @Override
        public void forEachOrThrow(Effect<V> ef) {
            throw new IllegalStateException();
        }

        @Override
        public Result<RuntimeException> forEachOrException(Effect<V> ef) {
            return empty();
        }

        @Override
        public Result<V> mapEmpty() {
            return Result.success(null);
        }

        public Empty() {
            super();
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public V getContent() {
            throw new IllegalAccessError();
        }

        @Override
        public V getOrElse(final V defaultValue) {
            return defaultValue;
        }
        @Override
        public <U> Result<U> map(Function<V, U> f) {
            return empty;
        }
        @Override
        public <U> Result<U> flatMap(Function<V, Result<U>> f) {
            return empty;
        }

        @Override
        public void forEach(Effect<V> ef) {
            //Just do nothing
        }

        @Override
        boolean isFailure() {
            return true;
        }

        @Override
        public String toString() {
            return "Empty()";
        }
        @Override
        public V getOrElse(Supplier<V> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public Result<V> orElse(Supplier<Result<V>> defaultResult) {
            return defaultResult.get();
        }
    }

	public static class Success<T> extends Result<T> {
		private final T value;

        @Override
        public Result<RuntimeException> forEachOrException(Effect<T> ef) {
            ef.apply(value);
            return empty();
        }

        @Override
        public Result<T> mapEmpty() {
            return Result.failure("is not empty");
        }

        public void forEachOrThrow(Effect<T> ef) {
            forEach(ef);
        }

        public void forEach(Effect<T> ef) {
            ef.apply(value);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			Success other = (Success) obj;
			return getClass().equals(obj.getClass()) 
					&& this.value.equals(other.value);
		}

		private Success(T t) {
			value = t;
		}

		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public T getContent() {
			return value;
		}

		@Override
		public T getOrElse(T defaultValue) {
			return value;
		}

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return value;
        }

        @Override
        public Result<T> orElse(Supplier<Result<T>> defaultResult) {
            return Result.success(value);
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            try {
                return Result.success(f.apply(value));
            }catch(RuntimeException e){
                return Result.failure(e);
            }
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return f.apply(value);
        }

        @Override
		public boolean isFailure() {
			return false;
		}

		@Override
		public String toString() {
			return String.format("Success(%s)", value.toString());
		}
	}

	public static class Failure<T> extends Empty<T> {
		private final RuntimeException exception;

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Result<RuntimeException> forEachOrException(Effect<T> ef) {
            return success(exception);
        }

        @Override
        public void forEachOrThrow(Effect<T> ef) {
            throw exception;
        }

		private Failure(String message) {
			this.exception = new IllegalStateException(message);
		}

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            return Result.failure(exception);
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return Result.failure(exception);
        }

        @Override
        public Result<T> mapEmpty() {
            return Result.failure("is not empty");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Failure<?> failure = (Failure<?>) o;
            return Objects.equals(toString(), failure.toString());
        }

        @Override
        public int hashCode() {
            return Objects.hash(exception);
        }

		private Failure(RuntimeException e) {
			this.exception = e;
		}
		private Failure(Exception e) {
			this.exception = new IllegalStateException(e.getMessage(), e);
		}

		@Override
		public String toString() {
			return String.format("Failure(%s)", exception.getMessage());
		}
	}

	abstract boolean isFailure();

	public static <V> Result<V> failure(Exception e) {
		return new Failure<V>(e);
	}
	public static <V> Result<V> failure(RuntimeException e) {
		return new Failure<V>(e);
	}
	public static <T> Result<T> failure(String message) {
		return new Failure<>(message);
	}

	public static <T> Result<T> success(T value) {
		return new Success<>(value);
	}

    public static <T> Result<T> empty() {
        return empty;
    }

    public static <T> Result<T> of(T value){
	    return of(value, "Null value");
    }

    public static <T> Result<T> of(T value, String message) {
        return value==null
                ?Result.failure(message)
                :Result.success(value);
    }

    public static <T> Result<T> of(Function<T, Boolean> f, T value, String message) {
	    return ResultUtils.filterEmpty(of(value, message), f);
    }

    public static <T> Result<T> of(Function<T, Boolean> f, T value) {
        return ResultUtils.filterEmpty(of(value), f);
    }

}
