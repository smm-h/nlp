package nlp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nlp.ngram.CountTable;
import nlp.ngram.HashCountTable;
import nlp.ngram.HashProbabilityTable;
import nlp.ngram.ProbabilityTable;
import util.Secretly;

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

    public default CorpusMeasure getTFIDF() {
        final CorpusMeasure measure = new HashCorpusMeasure(this);
        for (Token token : getVocabulary()) {
            Term term = token.asTerm();
            double df = getDocumentFrequency(term);
            Map<Textual, Double> r = new HashMap<Textual, Double>();
            measure.put(token, r);
            double total = 0;
            double count = 0;
            for (Document document : this) {
                double tfidf = (df == 0) ? 0 : (document.getTermFrequency(term) / df);
                total += tfidf;
                count++;
                r.put(document, tfidf);
            }
            r.put(this, total / count);
        }
        return measure;
    }

    public default CorpusMeasure getTFDF() {
        final CorpusMeasure measure = new HashCorpusMeasure(this);
        for (Token token : getVocabulary()) {
            Term term = token.asTerm();
            double df = getDocumentFrequency(term);
            Map<Textual, Double> r = new HashMap<Textual, Double>();
            measure.put(token, r);
            double total = 0;
            double count = 0;
            for (Document document : this) {
                double tfdf = document.getTermFrequency(term) * df;
                total += tfdf;
                count++;
                r.put(document, tfdf);
            }
            r.put(this, total / count);
        }
        return measure;
    }

    static final Token BOF = new SpecialToken("BOL");
    static final Token EOF = new SpecialToken("EOL");

    public default CountTable getCountTable() {

        final CountTable countTable = new HashCountTable();

        for (Token token : getVocabulary()) {
            Map<Token, Integer> r = new HashMap<Token, Integer>();
            countTable.put(token, r);
            for (Token tokenAgain : getVocabulary()) {
                r.put(tokenAgain, 0);
                System.out.println("> " + token + " x " + tokenAgain);
            }
            r.put(EOF, 0);
        }

        Map<Token, Integer> r = new HashMap<Token, Integer>();
        countTable.put(BOF, r);
        for (Token token : getVocabulary()) {
            r.put(token, 0);
        }
        r.put(EOF, 0);

        for (Document document : this) {
            for (DocumentElement element : document) {

                Tokenized tokenized = null;

                // search for a tokenized aspect within this document
                if (element instanceof Tokenized) {
                    tokenized = (Tokenized) element;
                } else if (element instanceof Secretly<?>) {
                    Object secretSide = ((Secretly<?>) element).getSecretSide();
                    if (secretSide instanceof Tokenized) {
                        tokenized = (Tokenized) secretSide;
                    }
                }

                // now that we have obtained the tokenized, begin the counting
                if (tokenized != null) {
                    int n = tokenized.size();
                    increment(countTable, BOF, tokenized.get(0));
                    for (int i = 0; i < n - 1; i++) {
                        increment(countTable, tokenized.get(i), tokenized.get(i + 1));
                    }
                    increment(countTable, tokenized.get(n - 1), EOF);
                }
            }
        }
        return countTable;
    }

    private static void increment(CountTable countTable, Token a, Token b) {
        Map<Token, Integer> c = countTable.get(a);
        c.put(b, c.get(b) + 1);
    };

    public default ProbabilityTable getProbabilityTable() {

        final ProbabilityTable probabilityTable = new HashProbabilityTable();

        for (Token token : getVocabulary()) {
            Map<Token, Double> r = new HashMap<Token, Double>();
            probabilityTable.put(token, r);
            for (Token tokenAgain : getVocabulary()) {
                r.put(tokenAgain, 0.0);
                System.out.println("> " + token + " x " + tokenAgain);
            }
            r.put(EOF, 0.0);
        }

        Map<Token, Double> r = new HashMap<Token, Double>();
        probabilityTable.put(BOF, r);
        for (Token token : getVocabulary()) {
            r.put(token, 0.0);
        }
        r.put(EOF, 0.0);

        for (Document document : this) {
            for (DocumentElement element : document) {

                Tokenized tokenized = null;

                // search for a tokenized aspect within this document
                if (element instanceof Tokenized) {
                    tokenized = (Tokenized) element;
                } else if (element instanceof Secretly<?>) {
                    Object secretSide = ((Secretly<?>) element).getSecretSide();
                    if (secretSide instanceof Tokenized) {
                        tokenized = (Tokenized) secretSide;
                    }
                }

                // now that we have obtained the tokenized, begin the counting
                if (tokenized != null) {
                    int n = tokenized.size();
                    increment(probabilityTable, BOF, tokenized.get(0));
                    for (int i = 0; i < n - 1; i++) {
                        increment(probabilityTable, tokenized.get(i), tokenized.get(i + 1));
                    }
                    increment(probabilityTable, tokenized.get(n - 1), EOF);
                }
            }
        }

        for (Token a : probabilityTable.keySet()) {
            Map<Token, Double> map = probabilityTable.get(a);
            double total = 0;
            for (double d : map.values()) {
                total += d;
            }
            for (Token b : map.keySet()) {
                divide(probabilityTable, a, b, total);
            }
        }

        return probabilityTable;
    }

    private static void increment(ProbabilityTable probabilityTable, Token a, Token b) {
        Map<Token, Double> c = probabilityTable.get(a);
        c.put(b, c.get(b) + 1);
    };

    private static void divide(ProbabilityTable probabilityTable, Token a, Token b, double total) {
        Map<Token, Double> c = probabilityTable.get(a);
        c.put(b, c.get(b) / total);
    };
}
