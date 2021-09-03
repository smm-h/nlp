package nlp.ngram;

import java.util.Map;

import nlp.Token;

public interface ProbabilityTable extends Map<Token, Map<Token, Double>> {

}
