package nlp;

public class DocumentUtilities {

    public static Document getDocumentFromCharsAsTokens(String s) {
        String[] a = new String[s.length()];
        int i = 0;
        for (char c : s.toCharArray()) {
            a[i++] = Character.toString(c);
        }
        return new LinkedDocument(new TokenizedParagraph(new ArrayTokenized(a)));
    }

    public static Term getTermFromCharsAsTokens(String s) {
        String[] a = new String[s.length()];
        int i = 0;
        for (char c : s.toCharArray()) {
            a[i++] = Character.toString(c);
        }
        return new LinkedTerm(new ArrayTokenized(a));
    }
}
