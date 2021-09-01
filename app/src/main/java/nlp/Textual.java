package nlp;

/**
 * Anything that contains text, but is not necessarily sequential.
 */
public interface Textual {

    public boolean contains(Term term);

    /**
     * @return Number of non-unique {@link Token}s in this text.
     */
    public int sizeNonUnique();

    /**
     * @return Number of unique {@link Token}s in this text.
     */
    public default int sizeUnique() {
        return getVocabulary().size();
    }

    /**
     * @return The set of unique {@link Token}s in this text.
     */
    public Vocabulary getVocabulary();

    /**
     * @return The number of {@link Document}s that contain a given term.
     */
    public int getTermFrequency(Term term);
}
