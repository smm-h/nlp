package nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrayTokenized extends ArrayList<Token> implements Tokenized {

    private final Map<Term, Boolean> containsCache = new HashMap<Term, Boolean>();
    private Vocabulary v;

    public ArrayTokenized(String... strings) {
        for (String s : strings) {
            add(new StringToken(s));
        }
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

            // use the default implementation to get the answer
            answer = Tokenized.super.contains(term);

            // store the answer for future
            containsCache.put(term, answer);
        }
        return answer;
    }

    @Override
    public Vocabulary getVocabulary() {
        if (v == null) {
            v = new HashVocabulary();
            for (Token t : this) {
                v.add(t);
            }
        }
        return v;
    }
}
