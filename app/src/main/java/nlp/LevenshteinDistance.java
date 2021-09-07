package nlp;

public class LevenshteinDistance implements EditDistance {

    @Override
    public int calculateDistance(String a, String b) {
        return lev(a, b);
    }

    // https://en.wikipedia.org/wiki/Levenshtein_distance#Definition
    // Very inefficient
    public static int lev(String a, String b) {
        if (a.isEmpty())
            return b.length();
        else if (b.isEmpty())
            return a.length();
        else if (a.charAt(0) == b.charAt(0))
            return lev(tail(a), tail(b));
        else
            return 1 + min(lev(tail(a), b), lev(a, tail(b)), lev(tail(a), tail(b)));
    }

    private static String tail(String string) {
        return string.substring(1);
    }

    private static int min(int a, int b, int c) {
        int min = a;
        if (min > b)
            min = b;
        if (min > c)
            min = c;
        return min;
    }
}
