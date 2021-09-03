package nlp;

import java.util.Objects;

import util.Secretly;

public class TokenizedHeading implements Heading, Secretly<Tokenized> {

    private final Tokenized tokenized;
    private final int level;

    public TokenizedHeading(Tokenized tokenized, int level) {
        this.tokenized = Objects.requireNonNull(tokenized);
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
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
