package nlp.ngram;

import java.util.Map;
import java.util.HashMap;

import nlp.Term;
import nlp.Token;

public class HashCountTable extends HashMap<Term, Map<Token, Integer>> implements CountTable {

}
