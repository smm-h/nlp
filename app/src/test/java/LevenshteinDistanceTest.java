import nlp.LevenshteinDistance;

public class LevenshteinDistanceTest {
    public static void main(String[] args) {
        System.out.println(LevenshteinDistance.lev("kitten", "sitten")); // 1
        System.out.println(LevenshteinDistance.lev("sitten", "sittin")); // 1
        System.out.println(LevenshteinDistance.lev("sittin", "sitting")); // 1
        System.out.println(LevenshteinDistance.lev("kitten", "sitting")); // 1+1+1
    }
}
