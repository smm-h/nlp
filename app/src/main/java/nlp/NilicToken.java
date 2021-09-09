package nlp;

import jile.lingu.IndividualTokenType.IndividualToken;

public class NilicToken extends StringToken {

    private final IndividualToken token;

    public NilicToken(IndividualToken token) {
        this(token, null);
    }

    public NilicToken(IndividualToken token, Normalizer normalizer) {
        super(token.data, normalizer);
        this.token = token;
    }

    @Override
    public String toString() {
        return "[" + token.data.toString() + "] [" + token.getTypeString() + "]";
    }

}
