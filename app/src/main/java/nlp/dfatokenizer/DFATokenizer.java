package nlp.dfatokenizer;

import java.util.LinkedList;
import java.util.List;

import automata.dfa.DFA;
import automata.dfa.UnknownSymbolException;
import automata.dfa.AmbiguousSymbolException;
import nlp.Token;
import nlp.Tokenizer;

public class DFATokenizer extends DFA<Character> implements Tokenizer {

    @Override
    public List<Token> tokenize(String string) {
        List<Token> tokens = new LinkedList<Token>();
        char[] c = string.toCharArray();
        int b = 0;
        int i = 0;
        int n = c.length;
        State<Character> s0 = getStart();
        State<Character> s = s0;
        while (i < n) {
            try {
                char symbol = c[i];
                s = process(s, symbol);
            } catch (UnknownSymbolException e) {
                // System.out.println("Resetting.");
                if (s.isFinal()) {
                    tokens.add(new TypedToken(string.substring(b, i), s));
                } else {
                    System.out.println("LOST: " + string.substring(b, i));
                }
                b = i;
                s = s0;
            } catch (AmbiguousSymbolException e) {
                // System.out.println("Ambiguity encountered.");
                return null;
            }
            System.out.println(s);
            i++;
        }
        if (s.isFinal()) {
            tokens.add(new TypedToken(string.substring(b, i), s));
        } else {
            System.out.println("LOST: " + string.substring(b, i));
        }
        return tokens;
    }

}
