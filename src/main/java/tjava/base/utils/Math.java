package tjava.base.utils;

public class Math {

    public static int log2nlz(int n) {
        return n == 0
                ? 0
                : 31 - Integer.numberOfLeadingZeros(n);
    }
}
