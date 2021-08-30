package nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrayParagraph extends ArrayList<Token> implements Paragraph {

    private final String contents;
    private final Map<Term, Boolean> containsCache = new HashMap<Term, Boolean>();
    private final Vocabulary v;

    public ArrayParagraph(String... strings) {
        v = new HashVocabulary();
        StringBuilder b = new StringBuilder();
        for (String s : strings) {
            Token t = new StringToken(s);
            add(t);
            v.add(t);
            b.append(s);
            b.append(" ");
        }
        contents = b.toString();
    }

    @Override
    public String toPlainText() {
        return contents;
    }

    @Override
    public boolean contains(Term term) {

        // assume I do not contain the term
        boolean answer = false;

        // if I have checked before,
        if (containsCache.containsKey(term)) {

            // just give me the old answer
            answer = containsCache.get(term);

        }
        // otherwise, try to find out
        else {
            Token head = term.get(0);
            int n = size();
            int m = term.size();

            // iterate through my tokens
            for (int i = 0; i < n; i++) {

                // if any equal the head of the term,
                if (get(i).equals(head)) {

                    // check to see if the rest also matches
                    int j = 1;
                    for (; j < m; j++) {

                        // if even one token does not match,
                        if (!get(i + j).equals(term.get(j))) {

                            // move on
                            break;
                        }
                    }

                    // if the loop finished without breaking;
                    if (j == m) {

                        // a complete match was found
                        answer = true;

                        // search no longer
                        break;
                    }
                }
            }

            // store the answer for future
            containsCache.put(term, answer);
        }
        return answer;
    }

    @Override
    public int sizeNonUnique() {
        return size();
    }

    @Override
    public Vocabulary getVocabulary() {
        return v;
    }

    @Override
    public int getTermFrequency(Term term) {

        // assume I do not contain the term
        int tf = 0;

        Token head = term.get(0);
        int n = size();
        int m = term.size();

        // iterate through my tokens
        for (int i = 0; i < n; i++) {

            // if any equal the head of the term,
            if (get(i).equals(head)) {

                // check to see if the rest also matches
                int j = 1;
                for (; j < m; j++) {

                    // if even one token does not match,
                    if (!get(i + j).equals(term.get(j))) {

                        // move on
                        break;
                    }
                }

                // if the loop finished without breaking;
                if (j == m) {

                    // a complete match was found
                    tf++;
                }
            }
        }

        return tf;
    }

    @Override
    public String toString() {
        return contents;
    }
}
