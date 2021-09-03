package nlp.ngram;

import java.util.Map;

import nlp.Token;

public interface CountTable extends Map<Token, Map<Token, Integer>> {

}
