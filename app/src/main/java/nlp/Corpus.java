package nlp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Corpus extends Set<Document>, Textual {

    /**
     * @return The number of {@link Document}s that contain a given term, in this
     *         corpus.
     */
    public default int getDocumentFrequency(Term term) {
        int df = 0;
        for (Document d : this) {
            if (d.contains(term))
                df++;
        }
        return df;
    }

    public default TFIDF getTFIDF() {
        TFIDF m = new HashTFIDF(this);
        for (Token token : getVocabulary()) {
            Term term = token.asTerm();
            double df = getDocumentFrequency(term);
            Map<Textual, Double> r = new HashMap<Textual, Double>();
            m.put(token, r);
            double total = 0;
            for (Document document : this) {
                double tfidf = (df == 0) ? 0 : (document.getTermFrequency(term) / df);
                total += tfidf;
                r.put(document, tfidf);
            }
            r.put(this, total);
        }
        return m;
    }
}
