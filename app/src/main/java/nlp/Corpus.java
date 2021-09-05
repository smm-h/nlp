package nlp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jile.common.Common;
import nlp.ngram.CountTable;
import nlp.ngram.HashCountTable;
import nlp.ngram.HashProbabilityTable;
import nlp.ngram.ProbabilityTable;
import util.Secretly;
import util.Tuple;

public interface Corpus extends Set<Document>, Textual {

    public static final Token BOF = new SpecialToken("$");
    public static final Token EOF = new SpecialToken("\u03C6");

    static final Map<Integer, Term> pool = new HashMap<Integer, Term>();

    /**
     * Avoid making unnecessary garbage, and take your terms from this pool of
     * reusables.
     */
    public static Term getTermFromTokens(Token... tokens) {
        if (tokens.length == 1) {
            return tokens[0].asTerm();
        } else {
            int h = Tuple.collectiveHash(tokens);
            if (pool.containsKey(h)) {
                Term term = pool.get(h);
                if (term.size() == tokens.length) {
                    for (int i = 0; i < tokens.length; i++) {
                        if (!Objects.equals(term.get(i), tokens[i])) {
                            return forceConstruct(tokens);
                        }
                    }
                    return term;
                } else {
                    return forceConstruct(tokens);
                }
            } else {
                return forceConstruct(tokens);
            }
        }
    }

    private static Term forceConstruct(Token[] tokens) {
        Term term = new LinkedTerm(tokens);
        pool.put(term.hashCode(), term);
        return term;
    }

    public static Term[] getAllCombinations(Vocabulary vocabulary, int length) {

        // convert the vocabulary set into an array of tokens
        Token[] v = new Token[vocabulary.size() + 2];
        int vid = 0;
        v[vid++] = BOF;
        v[vid++] = EOF;
        for (Token t : vocabulary)
            v[vid++] = t;

        // B = number of individual tokens
        int b = v.length;

        // making mods: powers of B
        int[] mod = new int[length + 1];
        for (int i = 0; i <= length; i++) {
            mod[i] = (int) Common.power(b, i);
        }

        // N = the last mod: B ^ L
        int n = mod[length];

        // iterate from 1 to N
        Term[] t = new Term[n];
        for (int i = 0; i < n; i++) {
            Term x = new LinkedTerm();
            for (int j = 1; j <= length; j++) {
                x.add(v[(i % mod[j]) / mod[j - 1]]);
            }
            t[i] = x;
        }
        return t;
    }

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

    public static Term getPrior(int n, List<Token> tokenized, int index) {
        Token[] array = new Token[n];
        for (int i = 0; i < n; i++)
            array[i] = getSafe(tokenized, index - n + i);
        return getTermFromTokens(array);
    }

    public static Token getSafe(List<Token> list, int index) {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException e) {
            return index < 0 ? BOF : EOF;
        }
    }

    public default CountTable getCountTable(int n) {

        final CountTable table = new HashCountTable();

        Term[] combinations = getAllCombinations(getVocabulary(), n);

        for (Term term : combinations) {
            Map<Token, Integer> r = new HashMap<Token, Integer>();
            table.put(term, r);
            for (Token tokenAgain : getVocabulary()) {
                r.put(tokenAgain, 0);
                // System.out.println("> " + token + " x " + tokenAgain);
            }
            r.put(EOF, 0);
        }

        Map<Token, Integer> r = new HashMap<Token, Integer>();
        table.put(getTermFromTokens(BOF), r);
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
                    int size = tokenized.size();
                    for (int i = 0; i < size + n; i++) {
                        increment(table, getPrior(n, tokenized, i), tokenized.getSafe(i));
                    }
                }
            }
        }
        return table;
    }

    private static void increment(CountTable countTable, Term a, Token b) {
        Map<Token, Integer> c = countTable.get(a);
        c.put(b, c.get(b) + 1);
    };

    public default ProbabilityTable getProbabilityTable(int n) {

        final ProbabilityTable table = new HashProbabilityTable(n);

        Term[] combinations = getAllCombinations(getVocabulary(), n);

        for (Term term : combinations) {
            Map<Token, Double> r = new HashMap<Token, Double>();
            table.put(term, r);
            for (Token tokenAgain : getVocabulary()) {
                r.put(tokenAgain, 0.0);
                // System.out.println("> " + token + " x " + tokenAgain);
            }
            r.put(EOF, 0.0);
        }

        Map<Token, Double> r = new HashMap<Token, Double>();
        table.put(getTermFromTokens(BOF), r);
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
                    int size = tokenized.size();
                    for (int i = 0; i < size + n; i++) {
                        increment(table, getPrior(n, tokenized, i), tokenized.getSafe(i));
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
