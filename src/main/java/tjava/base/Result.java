package tjava.base;

public interface Result<T> {
	void bind(Effect<T> success, Effect<String> failure);

	public static <T> Result<T> failure(String message) {
		return new Failure<>(message);
	}

	public static <T> Result<T> success(T value) {
		return new Success<>(value);
	}
	
	public abstract boolean isSuccess();
	
	public abstract T getContent();

	public class Success<T> implements Result<T> {
		private final T value;

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
		public void bind(Effect<T> success, Effect<String> failure) {
			success.apply(value);
		}

		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public T getContent() {
			return value;
		}
	}

	public class Failure<T> implements Result<T> {
		private final String errorMessage;

		private Failure(String s) {
			this.errorMessage = s;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			Failure other = (Failure) obj;
			return getClass().equals(obj.getClass()) 
					&& this.errorMessage.equals(other.errorMessage);
		}

		@Override
		public void bind(Effect<T> success, Effect<String> failure) {
			failure.apply(errorMessage);
		}

		@Override
		public boolean isSuccess() {
			return false;
		}

		@Override
		public T getContent() {
			throw new IllegalAccessError();
		}
	}
}
