package nlp;

public class TokenizedParagraph implements Paragraph {

    private final Tokenized tokenized;

    public TokenizedParagraph(Tokenized tokenized) {
        this.tokenized = tokenized;
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

}
