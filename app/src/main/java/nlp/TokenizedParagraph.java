package nlp;

import java.util.Objects;

import util.Secretly;

public class TokenizedParagraph implements Paragraph, Secretly<Tokenized> {

    private final Tokenized tokenized;

    public TokenizedParagraph(Tokenized tokenized) {
        this.tokenized = Objects.requireNonNull(tokenized);
    }

    @Override
    public String inspect(Inspector inspector) {
        return inspector.inspect(tokenized);
    }

    @Override
    public String toPlainText() {
        return tokenized.getContents();
    }

    @Override
    public boolean contains(Term term) {
        return tokenized.contains(term);
    }

    @Override
    public int sizeNonUnique() {
        return tokenized.sizeNonUnique();
    }

    @Override
    public Vocabulary getVocabulary() {
        return tokenized.getVocabulary();
    }

    @Override
    public int getTermFrequency(Term term) {
        return tokenized.getTermFrequency(term);
    }

    @Override
    public Tokenized getSecretSide() {
        return tokenized;
    }
}
