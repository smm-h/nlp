package nlp;

public interface Token extends Comparable<Token> {

    public Term asTerm();

    public String getUnnormalized();

    public default String normalize() {
        return normalize(Utilities.DEFAULT_NORMALIZER);
    }

    public default String normalize(Normalizer normalizer) {
        return normalizer.normalize(getUnnormalized());
    }

    public default boolean isNormal() {
        return isNormal(Utilities.DEFAULT_NORMALIZER);
    }

    public default boolean isNormal(Normalizer normalizer) {
        return normalize(normalizer).equals(getUnnormalized());
    }

    @Override
    public default int compareTo(Token other) {
        return Integer.compare(hashCode(), other.hashCode());
    }
}
