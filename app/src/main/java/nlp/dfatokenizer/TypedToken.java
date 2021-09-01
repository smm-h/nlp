package nlp.dfatokenizer;

import automata.dfa.DFA.State;
import nlp.StringToken;

public class TypedToken extends StringToken {

    private final State<Character> state;

    public TypedToken(String contents, State<Character> type) {
        super(contents);
        state = type;
    }

    @Override
    public String toString() {
        return super.toString() + " <" + state.toString() + ">";
    }

}
