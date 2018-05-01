package tjava.base;

public interface Function<T, U> {
	U apply(T arg);
	
	static <T> Function<T, T> identity() {
		return t -> t;
	}
}
