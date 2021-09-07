package util;

public class Common {
    public static int min(int... x) {
        int min = x[0];
        for (int i = 1; i < x.length; i++) {
            if (min > x[i])
                min = x[i];
        }
        return min;
    }
}
