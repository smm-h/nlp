package nlp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HashCorpusMeasure extends HashMap<Token, Map<Textual, Double>> implements CorpusMeasure {

    private final Corpus corpus;
    private double[] sorted;

    public HashCorpusMeasure(Corpus corpus) {
        this.corpus = corpus;
    }

    @Override
    public Corpus getCorpus() {
        return corpus;
    }

    @Override
    public double[] getSorted() {
        if (sorted == null) {
            sorted = new double[size()];
            Corpus corpus = getCorpus();
            int i = 0;
            for (Map<Textual, Double> rowMap : values()) {
                sorted[i++] = rowMap.get(corpus);
            }
            Arrays.sort(sorted);
            // System.out.println("Range: " + sorted[0] + "~" + sorted[sorted.length - 1]);
        }
        return sorted;
    }
}
