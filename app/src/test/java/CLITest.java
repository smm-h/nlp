
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import nlp.Corpus;
import nlp.Document;
import nlp.HashCorpus;
import nlp.Inspector;
import nlp.LevenshteinDistance;
import nlp.Lexicon;
import nlp.Splitter;
import nlp.StoredHashLexicon;
import nlp.Term;
import nlp.Textual;
import nlp.Utilities;
import nlp.languages.Farsi;
import nlp.languages.NaturalLanguage;
import nlp.ngram.CountTable;
import nlp.ngram.ProbabilityTable;
import util.CLI;
import util.ToString;
import util.cli.AlternativeQuestion;
import util.cli.OpenQuestion;
import util.cli.YesNoQuestion;
import web.html.HTMLReport;
import web.html.HTMLUtilities;
import web.html.HTMLUtilities.RowPredicate;
import web.wikipedia.RandomDocumentGenerator;

public class CLITest extends CLI {

    NaturalLanguage nl = new Farsi();

    Corpus corpus;

    Document document;

    public static void main(String[] args) throws Exception {
        new CLITest();
    }

    public CLITest() {

        // HTMLDocumentElement.DEFAULT_TOKENIZER = DFATokenizerBigTest.getTokenizer();
        Utilities.DEFAULT_TOKENIZER = new Splitter(" .,;'\"()[]{}،؛!?؟");
        // Utilities.DEFAULT_TOKENIZER = new Splitter();

        Utilities.DEFAULT_NORMALIZER = nl.getDefaultNormalizer();

        Lexicon lexicon = null;
        try {
            lexicon = new StoredHashLexicon(Path.of("C:/Users/SMM H/Desktop/nlp/res/ctpr/spellcheck-dict.txt"));
        } catch (IOException e) {
            System.out.println("could not load lexicon");
        }

        Inspector inspector = Inspector.makeInspector(corpus, lexicon);

        AlternativeQuestion start = makeAlternativeQuestion("Provide a corpus:", true, DO_NOTHING);

        AlternativeQuestion inCorpus = makeAlternativeQuestion("You are inside a corpus:", true, goTo(start));

        AlternativeQuestion inDocument = makeAlternativeQuestion("You are inside a document:", true, goTo(inCorpus));

        start.addOption("Find a corpus file from disk", UNSUPPORTED); // TODO

        start.addOption("Create a new corpus from input", new Runnable() {
            @Override
            public void run() {
                corpus = new HashCorpus();
                print("Enter lines as documents of characters, use blank line to stop.");
                while (true) {
                    String s = WHAT.ask();
                    if (s.isBlank()) {
                        break;
                    } else {
                        corpus.add(Utilities.getDocumentFromCharsAsTokens(s));
                    }
                }
                laterAsk(inCorpus);
            };
        });

        start.addOption("Generate a new corpus from Wikipedia", new HTMLReport("Articles in Corpus") {
            @Override
            public void beforeShowing() {
                corpus = CorpusGeneratorTest.generateCorpus(new RandomDocumentGenerator(nl), HOW_MANY.ask());
                addHeading("Corpus generated from Wikipedia articles", 1);
                add(HTMLUtilities.toList(corpus, false, CorpusGeneratorTest.DOCUMENT_TO_STRING));
            };

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        inCorpus.addOption("See the list of documents", new Runnable() {
            @Override
            public void run() {
                AlternativeQuestion q = makeAlternativeQuestion("Here is a list of documents in the corpus:", true,
                        goTo(inCorpus));
                for (Document d : corpus) {
                    q.addOption(d.toString(), new Runnable() {
                        @Override
                        public void run() {
                            document = d;
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
                addRawCSS("pre {white-space: pre-wrap;}");
                addHeading("Tokenized", 1);
                for (Document d : corpus) {
                    addTag("pre", d.inspect(Inspector.INSPECTOR_TOKENS));
                }
            }

            @Override
            public void afterShowing() {
            }
        });

        inCorpus.addOption("Inspect corpus", new HTMLReport("Tokenized") {
            @Override
            public void beforeShowing() {
                addHeading("Inspected", 1);
                for (Document d : corpus) {
                    addTag("p", d.inspect(inspector));
                }
            }

            @Override
            public void afterShowing() {
            }
        });

        inCorpus.addOption("See vocabulary of the corpus", new HTMLReport("Vocabulary") {
            @Override
            public void beforeShowing() {
                addHeading("Vocabulary", 1);
                // TODO
            }

            @Override
            public void afterShowing() {
            }
        });

        inCorpus.addOption("See an n-gram analysis of the corpus", new HTMLReport("N-gram") {
            @Override
            public void beforeShowing() {

                int n = HOW_MANY.ask() - 1;

                addHeading(Integer.toString(n + 1) + "-gram", 1);

                addHeading("Count Table", 2);
                CountTable ct = corpus.getCountTable(n);
                add(HTMLUtilities.toTable(ct));

                addHeading("Probability Table", 2);
                ProbabilityTable pt = corpus.getProbabilityTable(n);
                add(HTMLUtilities.toTable(pt));

            }

            @Override
            public void afterShowing() {
            }
        });

        ToString<Double> doubleToString = ToString.getDoubleToString(3);

        RowPredicate<Textual, Double> p = new RowPredicate<Textual, Double>() {
            @Override
            public boolean check(Map<Textual, Double> rowMap) {
                return rowMap.get(corpus) > 1.0;
            }
        };

        YesNoQuestion m = makeYesNoQuestion("See tokens with measures more than 1?", true, goTo(inCorpus));

        inCorpus.addOption("See the important words of the corpus using high TFIDF", new HTMLReport("TF-IDF") {
            @Override
            public void beforeShowing() {
                addHeading("TF-IDF", 1);
                add(HTMLUtilities.toTable("Token", corpus.getTFIDF(), m.ask() ? p : null, doubleToString));
            }

            @Override
            public void afterShowing() {
            }
        });

        inCorpus.addOption("See the stop-words of the corpus using high TFDF", new HTMLReport("TF-DF") {
            @Override
            public void beforeShowing() {
                addHeading("TF-DF", 1);
                add(HTMLUtilities.toTable("Token", corpus.getTFDF(), m.ask() ? p : null, doubleToString));
            }

            @Override
            public void afterShowing() {
            }
        });

        OpenQuestion<Term> WHAT_TERM = makeOpenQuestion("Enter term:",
                (String s) -> Utilities.getTermFromCharsAsTokens(s));

        inCorpus.addOption("Get the term-frequency of a term in the corpus", new Runnable() {
            @Override
            public void run() {
                print(corpus.getTermFrequency(WHAT_TERM.ask()));
            }
        });

        inCorpus.addOption("Get the document frequency of a term in the corpus", new Runnable() {
            @Override
            public void run() {
                print(corpus.getDocumentFrequency(WHAT_TERM.ask()));
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

        // inCorpus.addOption("", new Runnable() {
        // @Override
        // public void run() {

        // }
        // });

        laterAsk(start);
    }
}
