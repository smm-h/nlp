package nlp;

public class StringToken implements Token {

    private final String unnormalized, normalized;
    private final Term term;

    public StringToken(String contents) {
        this(contents, null);
    }

    public StringToken(String contents, Normalizer normalizer) {
        unnormalized = contents;
        normalized = normalizer == null ? Token.super.normalize() : Token.super.normalize(normalizer);
        term = new LinkedTerm(this);
    }

    @Override
    public String normalize() {
        return normalized;
    }

    @Override
    public boolean isNormal() {
        return normalized.equals(unnormalized);
    }

    @Override
    public Term asTerm() {
        return term;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringToken) {
            StringToken token = (StringToken) obj;
            return token.normalized.equals(normalized);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return normalized.hashCode();
    }

    @Override
    public String getUnnormalized() {
        return unnormalized;
    }

    @Override
    public String toString() {
        return "'" + normalized + "'";
    }

}
