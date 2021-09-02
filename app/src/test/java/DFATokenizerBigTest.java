import java.util.HashSet;
import java.util.Set;

import automata.dfa.DFA.ConfigurableTransition.Condition;
import nlp.Tokenizer;

public class DFATokenizerBigTest {

    public static void main(String[] args) {
        System.out.println(getTokenizer().tokenize("hello world"));
    }

    public static Tokenizer getTokenizer() {
        DFATokenizerTest t = new DFATokenizerTest(13) {
            @Override
            public void test() {
                setStart(1);

                setFinal(2, "int number");
                setFinal(4, "Real number");
                setFinal(5, "Punctuation");
                setFinal(6, "F.word");
                setFinal(7, "E.word");
                setFinal(11, "IP address");
                setFinal(13, "Hashtag");

                Set<Character> punctuationSet = new HashSet<Character>();
                for (char c : ".,?;،؛؟!:«»\"'".toCharArray())
                    punctuationSet.add(c);
                Condition<Character> punctuation = (Character c) -> punctuationSet.contains(c);

                Condition<Character> digit = (Character c) -> c >= 48 && c < 58;
                Condition<Character> letter = (Character c) -> (c >= 65 && c < 91) || (c >= 97 && c < 123);
                Condition<Character> delimiter = (Character c) -> c == 32;
                Condition<Character> hashtag = (Character c) -> c == '#';
                Condition<Character> period = (Character c) -> c == '.';

                // https://en.wikipedia.org/wiki/Persian_alphabet
                Condition<Character> f_letter = (Character c) -> c >= '\u0621' && c < '\u06cc';

                // https://en.wikipedia.org/wiki/Zero-width_non-joiner
                char zwnj = '\u0200';

                go(1, 1, delimiter);
                go(1, 2, digit);
                go(2, 2, digit);
                go(2, 3, period);
                // go(1, 3, period); // causes ambiguity
                go(3, 4, digit);
                go(4, 4, digit);
                go(4, 8, period);
                go(8, 9, digit);
                go(9, 9, digit);
                go(9, 10, period);
                go(10, 11, digit);
                go(11, 11, digit);
                go(1, 12, hashtag);
                go(12, 13, letter);
                go(13, 13, letter);
                go(1, 5, punctuation);
                go(1, 6, f_letter);
                go(6, 6, (Character c) -> f_letter.check(c) || c == zwnj);
                go(1, 7, letter);
                go(7, 7, (Character c) -> letter.check(c) || c == ',');

            }
        };
        t.test();
        return t.t;
    }
}
