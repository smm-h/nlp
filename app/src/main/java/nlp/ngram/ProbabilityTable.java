package nlp.ngram;

import java.util.Map;

import nlp.Token;
import nlp.Term;
import nlp.Corpus;

public interface ProbabilityTable extends Map<Term, Map<Token, Double>> {

    public int getN();

    public default double getProbabilityOf(Term term) {
        int n = getN();
        int s = term.size();

        double p = 1;
        for (int i = 0; i < s + n; i++)
            p *= getProbabilityOf(Corpus.getPrior(n, term, i), term.getSafe(i));
        return p;
    }

    public default double getProbabilityOf(Term a, Token b) {
        System.out.println("p(" + a + "|" + b + ") = " + String.format("%.4f", get(a).get(b)));
        return get(a).get(b);
    }
}
