package nlp;

public class StringToken implements Token {

    private final String contents;
    private final Term term;

    public StringToken(String contents) {
        this.contents = contents;
        this.term = new LinkedTerm(this);
    }

    @Override
    public Term asTerm() {
        return term;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringToken) {
            StringToken token = (StringToken) obj;
            return token.contents.equals(contents);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return contents.hashCode();
    }

    @Override
    public String asString() {
        return contents;
    }

    @Override
    public String toString() {
        return "'" + contents + "'";
    }

}
