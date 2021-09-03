package nlp;

import java.util.LinkedList;

public class LinkedTerm extends LinkedList<Token> implements Term {
    public LinkedTerm(Token... tokens) {
        for (Token t : tokens) {
            add(t);
        }
    }

    public LinkedTerm(Iterable<Token> tokens) {
        for (Token t : tokens) {
            add(t);
        }
    }
}
