package tjava.base;

public class ResultUtils {

    public static <T> Result<T> filter(Result<T> result, Function<T, Boolean> p, String message) {
        return result.flatMap(x -> p.apply(x)
                ? result
                : Result.failure(message));
    }

    public static <T> Result<T> filter(Result<T> result, Function<T, Boolean> p) {
        return result.flatMap(x -> p.apply(x)
                ? result
                : Result.failure("Condition not matched"));
    }

    public static <T> Result<T> filterEmpty(Result<T> result, Function<T, Boolean> p) {
        return result.flatMap(x -> p.apply(x)
                ? result
                : Result.empty());
    }

    public static <A, B> Function<Result<A>, Result<B>> lift(final Function<A, B> f) {
        return x -> x.map(f);
    }

    public static <A, B, C> Function<Result<A>, Function<Result<B>, Result<C>>> lift2(Function<A, Function<B, C>> f) {
        return x -> y -> x.map(f).flatMap(y::map);
    }

    public static <A, B, C, D> Function<Result<A>,
            Function<Result<B>, Function<Result<C>,
                    Result<D>>>> lift3(Function<A, Function<B, Function<C, D>>> f) {
        return a -> b -> c -> a.map(f).flatMap(b::map).flatMap(c::map);
    }

    public static <A, B, C> Result<C> map2(Result<A> a,
                                           Result<B> b,
                                           Function<A, Function<B, C>> f) {
        return lift2(f).apply(a).apply(b);
    }
}
