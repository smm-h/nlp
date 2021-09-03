package nlp.ngram;

import java.util.Map;
import java.util.HashMap;

import nlp.Token;

public class HashCountTable extends HashMap<Token, Map<Token, Integer>> implements CountTable {

}
