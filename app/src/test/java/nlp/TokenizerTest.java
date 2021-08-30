package nlp;

public class TokenizerTest {
    public static void main(String[] args) {
        Tokenizer tokenizer = new Splitter();
        String s1 = "مادر علی او را دعا میکند.";
        System.out.println(tokenizer.tokenize(s1));
    }
}
