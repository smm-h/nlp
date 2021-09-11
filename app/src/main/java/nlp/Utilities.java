package nlp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jile.common.Common;
import nlp.languages.English;
import nlp.languages.Farsi;
import nlp.languages.NaturalLanguage;
import util.Tuple;

public class Utilities {

    public static final NaturalLanguage FARSI = new Farsi();
    public static final NaturalLanguage ENGLISH = new English();

    public static Tokenizer DEFAULT_TOKENIZER;

    public static Normalizer DEFAULT_NORMALIZER;
    public static Normalizer NO_NORMALIZER;

    static {
        NO_NORMALIZER = new Normalizer() {
            @Override
            public String normalize(String original) {
                return original;
            }
        };

        DEFAULT_NORMALIZER = NO_NORMALIZER;
    }

    /**
     * Beginning of file/document
     */
    public static final Token BOF = new SpecialToken("$");

    /**
     * End of file/document
     */
    public static final Token EOF = new SpecialToken("\u03C6");

    /**
     * Beginning of token
     */

    public static final char BOT = '#';
    /**
     * End of token
     */
    public static final char EOT = '$';

    private static final Map<Integer, Term> pool = new HashMap<Integer, Term>();

    /**
     * Avoid making unnecessary garbage, and take your terms from this pool of
     * reusables.
     */
    public static Term getTermFromTokens(Token... tokens) {
        if (tokens.length == 1) {
            return tokens[0].asTerm();
        } else {
            int h = Tuple.collectiveHash(tokens);
            if (pool.containsKey(h)) {
                Term term = pool.get(h);
                if (term.size() == tokens.length) {
                    for (int i = 0; i < tokens.length; i++) {
                        if (!Objects.equals(term.get(i), tokens[i])) {
                            return forceConstruct(tokens);
                        }
                    }
                    return term;
                } else {
                    return forceConstruct(tokens);
                }
            } else {
                return forceConstruct(tokens);
            }
        }
    }

    private static Term forceConstruct(Token[] tokens) {
        Term term = new LinkedTerm(tokens);
        pool.put(term.hashCode(), term);
        return term;
    }

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

    public static Document getDocumentFromPath(Path path) throws IOException {
        return getDocumentFromPath(path, DEFAULT_TOKENIZER);
    }

    public static Document getDocumentFromPath(Path path, Tokenizer tokenizer) throws IOException {
        return getDocumentFromLines(Files.readAllLines(path), DocumentReadingMethod.PLAIN_TEXT, tokenizer);
    }

    public static Document getDocumentFromPath(Path path, DocumentReadingMethod method) throws IOException {
        return getDocumentFromPath(path, method, DEFAULT_TOKENIZER);
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

    public static Term[] getAllCombinations(Set<Token> vocabulary, int length) {

        // convert the vocabulary set into an array of tokens
        Token[] v = new Token[vocabulary.size() + 2];
        int vid = 0;
        v[vid++] = BOF;
        v[vid++] = EOF;
        for (Token t : vocabulary)
            v[vid++] = t;

        // B = number of individual tokens
        int b = v.length;

        // making mods: powers of B
        int[] mod = new int[length + 1];
        for (int i = 0; i <= length; i++) {
            mod[i] = (int) Common.power(b, i);
        }

        // N = the last mod: B ^ L
        int n = mod[length];

        // iterate from 1 to N
        Term[] t = new Term[n];
        for (int i = 0; i < n; i++) {
            Term x = new LinkedTerm();
            for (int j = 1; j <= length; j++) {
                x.add(v[(i % mod[j]) / mod[j - 1]]);
            }
            t[i] = x;
        }
        return t;
    }

    public static Term getPrior(int n, List<Token> tokenized, int index) {
        Token[] array = new Token[n];
        for (int i = 0; i < n; i++)
            array[i] = getSafe(tokenized, index - n + i);
        return getTermFromTokens(array);
    }

    public static Token getSafe(List<Token> list, int index) {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException e) {
            return index < 0 ? BOF : EOF;
        }
    }
}
