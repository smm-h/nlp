package nlp;

import java.util.Map;

public interface TFIDF extends Map<Token, Map<Textual, Double>> {

    public Corpus getCorpus();

    public double[] getSorted();
}
