package nlp;

public class DocumentUtilities {

    public static Document charsAsTokens(String s) {
        String[] a = new String[s.length()];
        int i = 0;
        for (char c : s.toCharArray()) {
            a[i++] = Character.toString(c);
        }
        return new LinkedDocument(new TokenizedParagraph(new ArrayTokenized(a)));
    }
}
