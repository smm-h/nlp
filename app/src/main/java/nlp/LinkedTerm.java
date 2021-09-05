package nlp;

import java.util.LinkedList;

import util.Tuple;

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

    @Override
    public int hashCode() {
        return Tuple.collectiveHash(this);
    }
}
