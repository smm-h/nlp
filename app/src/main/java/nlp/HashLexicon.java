package nlp;

import java.util.HashSet;

public class HashLexicon extends HashSet<Token> implements Lexicon {

    @Override
    public boolean contains(Token token) {
        return super.contains(token);
    }

}
