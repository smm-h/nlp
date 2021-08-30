package nlp;

import java.util.List;

public interface Tokenizer {
    public List<Token> tokenize(String string);
}
