package nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrayTokenized extends ArrayList<Token> implements Tokenized {

    private final Map<Term, Boolean> containsCache = new HashMap<Term, Boolean>();
    private final Vocabulary v;

    public ArrayTokenized(String... strings) {
        v = new HashVocabulary();
        for (String s : strings) {
            Token t = new StringToken(s);
            add(t);
            v.add(t);
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
        return v;
    }

    @Override
    public String toString() {
        return getContents();
    }
}
