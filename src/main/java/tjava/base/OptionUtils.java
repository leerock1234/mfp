package tjava.base;

public class OptionUtils {
    public static <A,B> Option<B> map(Option<A> option, Function<A, B> f){
        return flatMap(option, Function.composeAndThen(f, x->Option.some(x)));
    }

    public static <A,B> Option<B> flatMap(Option<A> option, Function<A, Option<B>> f){
        try{
            return f.apply(option.getOrThrow());
        }catch(RuntimeException e){
            return Option.none();
        }
    }

    public static <A,B> Function<Option<A>, Option<B>> lift(Function<A, B> f){
        return x->OptionUtils.map(x, f);
    }

    public static <A,B> Function<A, Option<B>> hLift(Function<A, B> f){
        return x->OptionUtils.map(Option.some(x), f);
    }

    public static <A, B, C> Option<C> map2(Option<A> a,
                                    Option<B> b,
                                    Function<A, Function<B, C>> f) {
        return appMap(b, map(a, f));
    }

    public static <A,B> Option<B> appMap(Option<A> option, Option<Function<A, B>> f){
        try {
            return map(option, f.getOrThrow());
        }catch(RuntimeException e){
            return Option.none();
        }
    }

    public static <A, B, C, D> Option<D> map3(Option<A> a,
                                           Option<B> b,
                                           Option<C> c,
                                           Function<A, Function<B, Function<C,D>>> f) {
        return appMap(c, appMap(b, map(a, f)));
    }
}
