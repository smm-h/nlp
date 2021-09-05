package nlp.ngram;

import java.util.Map;

import nlp.Term;
import nlp.Token;

public interface CountTable extends Map<Term, Map<Token, Integer>> {

}
