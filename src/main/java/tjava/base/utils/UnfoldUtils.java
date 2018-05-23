package tjava.base.utils;

import tjava.base.Function;
import tjava.base.Result;
import tjava.base.TailCall;
import tjava.base.Tuple;

public class UnfoldUtils {

    public static <A> A unfold(A a, Function<A, Result<A>> f) {
        Result<A> ra = Result.success(a);
        return unfold(new Tuple<>(ra, ra), f).eval()._2.getOrElse(a);
    }
    private static <A> TailCall<Tuple<Result<A>, Result<A>>> unfold(Tuple<Result<A>,
            Result<A>> a, Function<A, Result<A>> f) {
        Result<A> x = a._2.flatMap(f::apply);
        return x.isSuccess()
                ? TailCall.sus(() -> unfold(new Tuple<>(a._2, x), f))
                : TailCall.ret(a);
    }

}
