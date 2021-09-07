package nlp;

public interface SingleCharacterNormalizer extends Normalizer {

    public char normalize(char c);

    @Override
    public default String normalize(String original) {
        StringBuilder builder = new StringBuilder(original.length());
        for (char c : original.toCharArray()) {
            builder.append(normalize(c));
        }
        return builder.toString();
    }

}
