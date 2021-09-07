package nlp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Utilities {

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

    public enum DocumentReadingMethod {
        PLAIN_TEXT, MARKDOWN, EXTERNAL_HTML, INTERNAL_HTML
    }

    public static Document getDocumentFromPath(Path path, Tokenizer tokenizer) throws IOException {
        return getDocumentFromLines(Files.readAllLines(path), DocumentReadingMethod.PLAIN_TEXT, tokenizer);
    }

    public static Document getDocumentFromPath(Path path, DocumentReadingMethod method, Tokenizer tokenizer)
            throws IOException {
        return getDocumentFromLines(Files.readAllLines(path), method, tokenizer);
    }

    public static Document getDocumentFromLines(List<String> lines, DocumentReadingMethod method, Tokenizer tokenizer) {
        switch (method) {
            case PLAIN_TEXT:
                int last = 0;
                Document d = new LinkedDocument();
                int n = lines.size();
                for (int i = 0; i <= n; i++) {
                    if (i == n || lines.get(i).isEmpty()) {
                        DocumentElement e = getParagraphFromLines(lines, last, i, tokenizer);
                        if (e != null)
                            d.add(e);
                        last = i + 1;
                    }
                }
                return d;
            default:
                System.out.println("Unsupported method: " + method);
                return null;
        }
    }

    public static Paragraph getParagraphFromLines(List<String> lines, int start, int end, Tokenizer tokenizer) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            builder.append(lines.get(i).trim());
            builder.append(' ');
        }
        return getParagraphFromString(builder.toString(), tokenizer);
    }

    public static Paragraph getParagraphFromString(String string, Tokenizer tokenizer) {
        string = string.trim();
        return (string.isEmpty()) ? null : new TokenizedParagraph(tokenizer.tokenize(string));
    }

    public static Tokenizer DEFAULT_TOKENIZER;
}
