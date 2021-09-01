package nlp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Corpus extends Set<Document>, Text {

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
        TFIDF m = new TFIDF();
        for (Token token : getVocabulary()) {
            Term term = token.asTerm();
            double df = getDocumentFrequency(term);
            Map<Document, Double> r = new HashMap<Document, Double>();
            m.put(token, r);
            for (Document document : this) {
                double tfidf = (df == 0) ? 0 : (document.getTermFrequency(term) / df);
                r.put(document, tfidf);
            }
        }
        return m;
    }
}
