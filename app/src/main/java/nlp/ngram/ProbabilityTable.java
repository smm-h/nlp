package nlp.ngram;

import java.util.Map;

import nlp.Token;
import nlp.Term;
import nlp.Corpus;

public interface ProbabilityTable extends Map<Token, Map<Token, Double>> {
    public default double getProbabilityOf(Term term) {
        double p = 1;
        Token last = Corpus.BOF;
        for (Token token : term) {
            p *= getProbabilityOf(last, token);
            last = token;
        }
        p *= getProbabilityOf(last, Corpus.EOF);
        return p;
    }

    public default double getProbabilityOf(Token a, Token b) {
        System.out.println("p(" + a + "|" + b + ") = " + String.format("%.4f", get(a).get(b)));
        return get(a).get(b);
    }
}
