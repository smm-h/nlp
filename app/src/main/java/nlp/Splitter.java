package nlp;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Tokenizer} that splits the string on a set of specific characters,
 * e.g. whitespace.
 */
public class Splitter implements Tokenizer {

    private static final char[] WHITESPACE = { 9, 10, 11, 12, 13, 32 };
    private final Set<Character> splitOn;

    public Splitter() {
        this(WHITESPACE);
    }

    public Splitter(String s) {
        this(s.toCharArray());
    }

    public Splitter(char[] splitOn) {
        this.splitOn = new HashSet<Character>();
        for (char c : splitOn) {
            this.splitOn.add(c);
        }
    }

    public Splitter(Set<Character> splitOn) {
        this.splitOn = splitOn;
    }

    @Override
    public Tokenized tokenize(String string) {

        // keep a list of tokenized tokens
        ArrayTokenized tokens = new ArrayTokenized();

        // keep track of two flags for the start and end of token
        int backward = 0;

        char[] chars = string.toCharArray();

        // iterate over all the characters in the string
        for (int i = 0; i < chars.length; i++) {

            // if the current character is one to split on
            if (splitOn.contains(chars[i])) {

                // if a token has been made,
                if (i > backward) {

                    // add that token
                    tokens.add(new StringToken(string.substring(backward, i)));
                }

                // synchronize flags
                backward = i;
                backward++;

            }
        }

        // add the last token
        tokens.add(new StringToken(string.substring(backward)));

        // return the list of tokens
        return tokens;
    }

}
