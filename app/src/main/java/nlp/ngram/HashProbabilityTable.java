package nlp.ngram;

import java.util.Map;
import java.util.HashMap;

import nlp.Token;

public class HashProbabilityTable extends HashMap<Token, Map<Token, Double>> implements ProbabilityTable {

}
