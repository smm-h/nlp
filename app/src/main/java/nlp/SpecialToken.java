package nlp;

public class SpecialToken extends StringToken {

    private final int hashCode;

    public SpecialToken(String contents) {
        super(contents);
        this.hashCode = Integer.MAX_VALUE;
    }

    public SpecialToken(String contents, int hashCode) {
        super(contents);
        this.hashCode = hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return asString();
    }

}
