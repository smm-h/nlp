package nlp;

public interface Token extends Comparable<Token> {

    public Term asTerm();

    /**
     * @return Raw
     */
    public String asString();

    @Override
    public default int compareTo(Token other) {
        return Integer.compare(hashCode(), other.hashCode());
    }
}
