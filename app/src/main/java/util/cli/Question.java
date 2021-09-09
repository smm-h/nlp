package util.cli;

/**
 * @see https://en.wikipedia.org/wiki/Question#Semantic_classification
 * @see AlternativeQuestion
 * @see OpenQuestion
 */
public interface Question<T> {

    public String getBody();

    public T ask();

    public boolean isCancellable();
}