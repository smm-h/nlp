package nlp.ngram;

import java.util.Map;
import java.util.HashMap;

import nlp.Term;
import nlp.Token;

public class HashProbabilityTable extends HashMap<Term, Map<Token, Double>> implements ProbabilityTable {
    private final int n;

    public HashProbabilityTable(int n) {
        this.n = n;
    }

    @Override
    public int getN() {
        return n;
    }
}
