package nlp;

import java.util.Map;

public interface CorpusMeasure extends Map<Token, Map<Textual, Double>> {

    public Corpus getCorpus();

    public double[] getSorted();
}
