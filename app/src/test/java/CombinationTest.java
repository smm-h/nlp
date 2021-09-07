
import nlp.HashVocabulary;
import nlp.StringToken;
import nlp.Term;
import nlp.Utilities;
import nlp.Vocabulary;

public class CombinationTest {
    public static void main(String[] args) {
        Vocabulary v = new HashVocabulary();

        v.add(new StringToken("a"));
        v.add(new StringToken("b"));
        v.add(new StringToken("c"));
        // v.add(new StringToken("d"));
        Term[] a = Utilities.getAllCombinations(v, 3);
        for (Term t : a) {
            System.out.println(t);
        }
    }
}
