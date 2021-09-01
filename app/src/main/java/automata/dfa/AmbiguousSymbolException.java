package automata.dfa;

import automata.dfa.DFA.State;

public class AmbiguousSymbolException extends Exception {

    public <T> AmbiguousSymbolException(State<T> s, T symbol) {
    }
}
