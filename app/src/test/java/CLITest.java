
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.util.*;

import nlp.Corpus;
import nlp.*;
import nlp.HashCorpus;
import nlp.Inspector;
import nlp.LevenshteinDistance;
import nlp.Lexicon;
import nlp.NilicTokenizer;
import nlp.StoredHashLexicon;
import nlp.Term;
import nlp.Textual;
import nlp.Token;
import nlp.Utilities;
import nlp.languages.Farsi;
import nlp.languages.NaturalLanguage;
import nlp.ngram.CountTable;
import nlp.ngram.ProbabilityTable;
import util.*;
import util.ToString;
import util.cli.AlternativeQuestion;
import util.cli.OpenQuestion;
import util.cli.YesNoQuestion;
import web.html.HTMLReport;
import web.html.HTMLUtilities;
import web.html.TableMaker;
import web.html.HTMLUtilities.RowPredicate;
import web.html.TableMaker.RowMaker;
import web.wikipedia.RandomDocumentGenerator;

public class CLITest extends CLI {

    NaturalLanguage nl = new Farsi();
    Corpus currentCorpus;
    Document currentDocument;
    Lexicon lexicon;

    AlternativeQuestion start, inCorpus, inDocument, vocabularyFiltering;

    public static void main(String[] args) throws Exception {
        new CLITest();
    }

    public CLITest() {

        Utilities.DEFAULT_NORMALIZER = nl.getDefaultNormalizer();

        try {
            Utilities.DEFAULT_TOKENIZER = new NilicTokenizer(Path.of("C:/Users/SMM H/Desktop/nlp/tokenizer.nlx"),
                    Utilities.DEFAULT_NORMALIZER);

        } catch (IOException e1) {
            // HTMLDocumentElement.DEFAULT_TOKENIZER =
            DFATokenizerBigTest.getTokenizer();
            // Utilities.DEFAULT_TOKENIZER = new Splitter(" .,;'\"()[]{}،؛!?؟");
            // Utilities.DEFAULT_TOKENIZER = new Splitter();
            System.out.println("could not load tokenizer");
        }

        try {
            lexicon = new StoredHashLexicon(Path.of("C:/Users/SMM H/Desktop/nlp/res/ctpr/spellcheck-dict.txt"));
        } catch (IOException e) {
            System.out.println("could not load lexicon");
        }

        start = makeAlternativeQuestion("Provide a corpus:", DO_NOTHING);
        inCorpus = makeAlternativeQuestion("You are inside a corpus:", goTo(start));
        inDocument = makeAlternativeQuestion("You are inside a document:", goTo(inCorpus));

        start.addOption("Find a corpus file from disk", UNSUPPORTED); // TODO

        start.addOption("Create a new corpus from input", new Runnable() {
            @Override
            public void run() {
                currentCorpus = new HashCorpus();
                print("Enter lines as documents of characters, use blank line to stop.");
                while (true) {
                    String s = WHAT.ask();
                    if (s.isBlank()) {
                        break;
                    } else {
                        currentCorpus.add(Utilities.getDocumentFromCharsAsTokens(s));
                    }
                }
                laterAsk(inCorpus);
            };
        });

        start.addOption("Generate a new corpus from Wikipedia", new HTMLReport("Articles in Corpus") {
            @Override
            public void beforeShowing() {
                currentCorpus = CorpusGeneratorTest.generateCorpus(new RandomDocumentGenerator(nl), HOW_MANY.ask());
                report.addHeading("Corpus generated from Wikipedia articles", 1);
                report.add(HTMLUtilities.toList(currentCorpus, false, CorpusGeneratorTest.DOCUMENT_TO_STRING));
            };

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        inCorpus.addOption("See the list of documents", new Runnable() {
            @Override
            public void run() {
                AlternativeQuestion q = makeAlternativeQuestion("Here is a list of documents in the corpus:",
                        goTo(inCorpus));
                for (Document d : currentCorpus) {
                    q.addOption(d.toString(), new Runnable() {
                        @Override
                        public void run() {
                            currentDocument = d;
                            laterAsk(inDocument);
                        }
                    });
                }
                laterAsk(q);
            }
        });

        inCorpus.addOption("Add documents to corpus", UNSUPPORTED); // TODO

        inCorpus.addOption("Tokenize corpus", new HTMLReport("Tokenized") {
            @Override
            public void beforeShowing() {
                report.addRawCSS("pre {white-space: pre-wrap;}");
                report.addHeading("Tokenized", 1);
                for (Document d : currentCorpus) {
                    report.addTag("pre", d.inspect(Inspector.INSPECTOR_TOKENS));
                }
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        inCorpus.addOption("See the sources of the documents in the corpus", new HTMLReport("Sources") {
            @Override
            public void beforeShowing() {
                report.addHeading("Sources", 1);
                String src;
                for (Document d : currentCorpus) {
                    try {
                        src = URLDecoder.decode(d.getSource(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        src = d.getSource();
                    }
                    report.addHeading(d.toString(), 2);
                    report.addParagraph("Source: " + src);
                }
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        ToString<Double> ts_integer = ToString.getDoubleToString(0);
        ToString<Double> ts_3digits = ToString.getDoubleToString(3);
        ToString<Boolean> ts_yesOrNo = ToString.getBooleanToString("Yes", "No");

        inCorpus.addOption("See the vocabulary of the corpus", new HTMLReport("Vocabulary") {
            @Override
            public void beforeShowing() {

                Set<Token> filteredVocabulary = filterVocabulary();

                // ask the user which columns to add
                List<String> columns = new LinkedList<String>();
                boolean v_tf = makeYesNoQuestion("Add token frequency?").ask();
                boolean v_df = makeYesNoQuestion("Add document frequency?").ask();
                boolean v_tfdf = makeYesNoQuestion("Add TF-DF?").ask();
                boolean v_tfidf = makeYesNoQuestion("Add TF-IDF?").ask();
                boolean v_isNormal = makeYesNoQuestion("Add whether or not it is normal?").ask();
                boolean v_isCorrect = lexicon == null ? false
                        : makeYesNoQuestion("Add whether or not it is correct?").ask();

                // add the columns
                columns.add("Token");
                if (v_tf)
                    columns.add("TF");
                if (v_df)
                    columns.add("DF");
                if (v_tfdf)
                    columns.add("TF-DF");
                if (v_tfidf)
                    columns.add("TF-IDF");
                if (v_isNormal)
                    columns.add("Is normal?");
                if (v_isCorrect)
                    columns.add("Is correct?");

                // create the table maker
                TableMaker tableMaker = new TableMaker(columns);

                // add the rows
                tableMaker.addRows(filteredVocabulary, new RowMaker<Token>() {
                    @Override
                    public List<String> make(Token token) {

                        // prepare data
                        Term term = token.asTerm();
                        double tf = currentCorpus.getTermFrequency(term);
                        double df = currentCorpus.getDocumentFrequency(term);
                        double tfdf = tf * df;
                        double tfidf = tf / df;

                        // create row
                        List<String> row = new LinkedList<String>();

                        // add strings to the row
                        row.add(token.toString());
                        if (v_tf)
                            row.add(ts_integer.alternativeToString(tf));
                        if (v_df)
                            row.add(ts_integer.alternativeToString(df));
                        if (v_tfdf)
                            row.add(ts_integer.alternativeToString(tfdf));
                        if (v_tfidf)
                            row.add(ts_3digits.alternativeToString(tfidf));
                        if (v_isNormal)
                            row.add(ts_yesOrNo.alternativeToString(token.isNormal()));
                        if (v_isCorrect)
                            row.add(ts_yesOrNo.alternativeToString(lexicon.contains(token)));

                        // return the row
                        return row;
                    }
                });

                // add the table itself
                report.addHeading("Vocabulary", 1);
                report.addRawHTML(tableMaker.finish());
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        inCorpus.addOption("See an n-gram analysis of the corpus", new HTMLReport("N-gram") {
            @Override
            public void beforeShowing() {

                int n = HOW_MANY.ask() - 1;

                Set<Token> filteredVocabulary = filterVocabulary();

                report.addHeading(Integer.toString(n + 1) + "-gram", 1);

                report.addHeading("Count Table", 2);
                CountTable ct = currentCorpus.getCountTable(n, filteredVocabulary);
                report.add(HTMLUtilities.toTable(ct));

                report.addHeading("Probability Table", 2);
                ProbabilityTable pt = currentCorpus.getProbabilityTable(n, filteredVocabulary);
                report.add(HTMLUtilities.toTable(pt));

            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        ToString<Double> doubleToString = ToString.getDoubleToString(3);

        RowPredicate<Textual, Double> p = new RowPredicate<Textual, Double>() {
            @Override
            public boolean check(Map<Textual, Double> rowMap) {
                return rowMap.get(currentCorpus) > 1.0;
            }
        };

        YesNoQuestion m = makeYesNoQuestion("Only see tokens with measures more than 1?", goTo(inCorpus));

        inCorpus.addOption("See the important words of the corpus using high TFIDF", new HTMLReport("TF-IDF") {
            @Override
            public void beforeShowing() {
                report.addHeading("TF-IDF", 1);
                report.add(
                        HTMLUtilities.toTable("Token", currentCorpus.getTFIDF(), m.ask() ? p : null, doubleToString));
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        inCorpus.addOption("See the stop-words of the corpus using high TFDF", new HTMLReport("TF-DF") {
            @Override
            public void beforeShowing() {
                report.addHeading("TF-DF", 1);
                report.add(HTMLUtilities.toTable("Token", currentCorpus.getTFDF(), m.ask() ? p : null, doubleToString));
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        OpenQuestion<Term> WHAT_TERM = makeOpenQuestion("Enter term:",
                (String s) -> Utilities.getTermFromCharsAsTokens(s));

        inCorpus.addOption("Get the term-frequency of a term in the corpus", new Runnable() {
            @Override
            public void run() {
                print(currentCorpus.getTermFrequency(WHAT_TERM.ask()));
            }
        });

        inCorpus.addOption("Get the document frequency of a term in the corpus", new Runnable() {
            @Override
            public void run() {
                print(currentCorpus.getDocumentFrequency(WHAT_TERM.ask()));
            }
        });

        inCorpus.addOption("Calculate the Minimum Edit Distance", new Runnable() {
            @Override
            public void run() {
                print(LevenshteinDistance.lev(WHAT.ask(), WHAT.ask()));
            }
        });

        inCorpus.addOption("Normalize a input string", new Runnable() {
            @Override
            public void run() {
                print(Utilities.DEFAULT_NORMALIZER.normalize(WHAT.ask()));
            }
        });

        inCorpus.addOption("Stem a input string", UNSUPPORTED); // TODO

        vocabularyFiltering = makeAlternativeQuestion("What parts of the vocabulary should be used?", goTo(inCorpus));
        vocabularyFiltering.addOption("The entire vocabulary");
        vocabularyFiltering.addOption("Only those with the higher in TF-DF");
        vocabularyFiltering.addOption("Only those with the higher in TF-IDF");

        // inCorpus.addOption("", new Runnable() {
        // @Override
        // public void run() {

        // }
        // });

        laterAsk(start);
    }

    public Set<Token> filterVocabulary() {
        boolean highInTFDF = false;
        switch (vocabularyFiltering.ask()) {
            case 1:
                return currentCorpus.getVocabulary();
            case 2:
                highInTFDF = true;
                break;
            case 3:
                highInTFDF = false;
                break;
        }
        double lowerBound = makeOpenQuestion("What is the lower bound?", (String s) -> Double.parseDouble(s)).ask();
        CorpusMeasure measure = highInTFDF ? currentCorpus.getTFDF() : currentCorpus.getTFIDF();
        // double[] sortedMeasure = measure.getSorted();
        Set<Token> filtered = new HashSet<Token>();
        for (Token token : currentCorpus.getVocabulary()) {
            if (measure.get(token).get(currentCorpus) > lowerBound) {
                filtered.add(token);
            }
        }
        return filtered;
    }
}
