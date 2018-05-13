package tjava.base;

public interface Executable {
    void exec();

    Function<Executable, Function<Executable, Executable>> compose =
            x -> y -> () -> {
                x.exec();
                y.exec();
            };

    static Executable ez = () -> {
    };
}
