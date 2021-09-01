package nlp.dfatokenizer;

import java.util.LinkedList;
import java.util.List;

import automata.dfa.AmbiguousSymbolException;
import automata.dfa.DFA;
import automata.dfa.UnknownSymbolException;
import nlp.Tokenizer;
import nlp.Token;

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
                if (s.isFinal()) {
                    tokens.add(new TypedToken(string.substring(b, i), s));
                }
                b = i;
                s = s0;
            } catch (AmbiguousSymbolException e) {
                System.out.println("Ambiguity encountered.");
                return null;
            }
            // System.out.println("s = " + s + (s.isFinal() ? " (F)" : ""));
            i++;
        }
        if (s.isFinal()) {
            tokens.add(new TypedToken(string.substring(b, i), s));
        }
        return tokens;
    }

}
