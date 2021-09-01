package automata.dfa;

import automata.dfa.DFA.State;

public class UnknownSymbolException extends Exception {

    public <T> UnknownSymbolException(State<T> s, T symbol) {
    }

}
