import nlp.LevenshteinDistance;

public class LevenshteinDistanceTest {
    public static void main(String[] args) {
        print("kitten", "sitten");
        print("sitten", "sittin");
        print("sittin", "sitting");
        print("kitten", "sitting");
    }

    static void print(String a, String b) {
        System.out.println("lev(" + a + ", " + b + ") = " + LevenshteinDistance.lev(a, b));
    }
}
