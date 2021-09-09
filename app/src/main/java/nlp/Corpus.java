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
        return getTFIDF(getVocabulary());
    }

    public default CorpusMeasure getTFIDF(Set<Token> vocab) {
        final CorpusMeasure measure = new HashCorpusMeasure(this);
        for (Token token : vocab) {
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
        return getTFDF(getVocabulary());
    }

    public default CorpusMeasure getTFDF(Set<Token> vocab) {
        final CorpusMeasure measure = new HashCorpusMeasure(this);
        for (Token token : vocab) {
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

    public default CountTable getCountTable(int n) {
        return getCountTable(n, getVocabulary());
    }

    public default CountTable getCountTable(int n, Set<Token> vocab) {

        final CountTable table = new HashCountTable();

        Term[] combinations = Utilities.getAllCombinations(vocab, n);

        for (Term term : combinations) {
            Map<Token, Integer> r = new HashMap<Token, Integer>();
            table.put(term, r);
            for (Token tokenAgain : vocab) {
                r.put(tokenAgain, 0);
                // System.out.println("> " + token + " x " + tokenAgain);
            }
            r.put(Utilities.EOF, 0);
        }

        Map<Token, Integer> r = new HashMap<Token, Integer>();
        table.put(Utilities.getTermFromTokens(Utilities.BOF), r);
        for (Token token : vocab) {
            r.put(token, 0);
        }
        r.put(Utilities.EOF, 0);

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
                    int size = tokenized.size();
                    for (int i = 0; i < size + n; i++) {
                        increment(table, Utilities.getPrior(n, tokenized, i), tokenized.getSafe(i));
                    }
                }
            }
        }
        return table;
    }

    private static void increment(CountTable countTable, Term a, Token b) {
        Map<Token, Integer> c = countTable.get(a);
        c.put(b, c.get(b) + 1);
    }

    public default ProbabilityTable getProbabilityTable(int n) {
        return getProbabilityTable(n, getVocabulary());
    }

    public default ProbabilityTable getProbabilityTable(int n, Set<Token> vocab) {

        final ProbabilityTable table = new HashProbabilityTable(n);

        Term[] combinations = Utilities.getAllCombinations(vocab, n);

        for (Term term : combinations) {
            Map<Token, Double> r = new HashMap<Token, Double>();
            table.put(term, r);
            for (Token tokenAgain : vocab) {
                r.put(tokenAgain, 0.0);
                // System.out.println("> " + token + " x " + tokenAgain);
            }
            r.put(Utilities.EOF, 0.0);
        }

        Map<Token, Double> r = new HashMap<Token, Double>();
        table.put(Utilities.getTermFromTokens(Utilities.BOF), r);
        for (Token token : vocab) {
            r.put(token, 0.0);
        }
        r.put(Utilities.EOF, 0.0);

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
                    int size = tokenized.size();
                    for (int i = 0; i < size + n; i++) {
                        increment(table, Utilities.getPrior(n, tokenized, i), tokenized.getSafe(i));
                    }
                }
            }
        }

        for (Term a : table.keySet()) {
            Map<Token, Double> map = table.get(a);
            double total = 0;
            for (double d : map.values()) {
                total += d;
            }
            for (Token b : map.keySet()) {
                divide(table, a, b, total);
            }
        }

        return table;
    }

    private static void increment(ProbabilityTable probabilityTable, Term a, Token b) {
        Map<Token, Double> c = probabilityTable.get(a);
        c.put(b, c.get(b) + 1);
    };

    private static void divide(ProbabilityTable probabilityTable, Term a, Token b, double total) {
        Map<Token, Double> c = probabilityTable.get(a);
        c.put(b, c.get(b) / total);
    };
}
