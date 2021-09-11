
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nlp.Corpus;
import nlp.Document;
import nlp.HashCorpus;
import nlp.Inspector;
import nlp.LevenshteinDistance;
import nlp.Lexicon;
import nlp.NilicTokenizer;
import nlp.StoredHashLexicon;
import nlp.Term;
import nlp.Token;
import nlp.TokenMetric;
import nlp.Utilities;
import nlp.languages.NaturalLanguage;
import util.CLI;
import util.ToString;
import util.cli.AlternativeQuestion;
import util.cli.OpenQuestion;
import web.html.HTMLReport;
import web.html.HTMLUtilities;
import web.html.TableMaker;
import web.html.TableMaker.RowMaker;
import web.wikipedia.RandomDocumentGenerator;

// @SuppressWarnings("unused")
public class CLITest extends CLI {
    NaturalLanguage nl = Utilities.FARSI;
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

        start.addOption("Choose language for document generation", new Runnable() {
            @Override
            public void run() {
                AlternativeQuestion q = makeAlternativeQuestion("Choose a natural language:", goTo(start));
                q.addOption("English");
                q.addOption("Farsi");
                switch (q.ask()) {
                    case 1:
                        nl = Utilities.ENGLISH;
                        break;
                    case 2:
                        nl = Utilities.FARSI;
                        break;
                }
                laterAsk(start);
            }
        });

        start.addOption("Open the sample corpus from disk", new Runnable() {
            @Override
            public void run() {
                try {
                    currentCorpus = SampleTokenizerTest.getCorpus();
                    laterAsk(inCorpus);
                } catch (IOException e) {
                    print("Failed to read the sample corpus file(s).");
                    laterAsk(start);
                }
            }
        });

        start.addOption("Open the document-per-line input corpus from disk", new Runnable() {
            @Override
            public void run() {
                try {
                    currentCorpus = new HashCorpus();
                    currentCorpus.add(
                            Utilities.getDocumentFromPath(Path.of("C:/Users/SMM H/Desktop/nlp/res/input-corpus.txt")));
                    laterAsk(inCorpus);
                } catch (IOException e) {
                    print("Failed to read the input corpus file.");
                    laterAsk(start);
                }
            }
        });

        start.addOption("Open the symbolic corpus file from disk", new Runnable() {
            @Override
            public void run() {
                try {
                    currentCorpus = new HashCorpus();
                    for (String s : Files.readAllLines(Path.of("C:/Users/SMM H/Desktop/nlp/res/symbolic-corpus.txt"))) {
                        currentCorpus.add(Utilities.getDocumentFromCharsAsTokens(s));
                    }
                    laterAsk(inCorpus);
                } catch (IOException e) {
                    print("Failed to read the symbolic corpus file.");
                    laterAsk(start);
                }
            }
        });

        start.addOption("Create a new symbolic corpus from input using characters as tokens", new Runnable() {
            @Override
            public void run() {
                currentCorpus = new HashCorpus();
                print("Enter lines as documents of characters, use '-' line to stop.");
                while (true) {
                    String s = WHAT.ask();
                    if (s.equals("-")) {
                        break;
                    } else {
                        currentCorpus.add(Utilities.getDocumentFromCharsAsTokens(s));
                    }
                }
                laterAsk(inCorpus);
            };
        });

        start.addOption("Generate a new corpus from Wikipedia", new Runnable() {
            @Override
            public void run() {
                currentCorpus = CorpusGeneratorTest.generateCorpus(new RandomDocumentGenerator(nl), HOW_MANY.ask());
                laterAsk(inCorpus);
            }
        });

        final ToString<Document> ts_doc = new ToString<Document>() {
            @Override
            public String alternativeToString(Document document) {
                StringBuilder sb = new StringBuilder();
                sb.append("<tr><td>");
                sb.append(document.toString());
                sb.append("</td><td>");
                String src;
                try {
                    src = URLDecoder.decode(document.getSource(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    src = document.getSource();
                }
                if (document.isSourceURL()) {
                    src = HTMLUtilities.makeLink(src);
                }
                sb.append(src);
                sb.append("</td><td><pre>");
                int limit = 64;
                String dt = document.toPlainText();
                if (dt.length() > limit) {
                    sb.append(dt.substring(0, limit));
                    sb.append("...");
                } else {
                    sb.append(dt);
                }
                sb.append("</pre></td></tr>");
                return sb.toString();
            }
        };

        inCorpus.addOption("See a list of all the documents in the corpus", new HTMLReport("Documents in the corpus") {
            @Override
            public boolean beforeShowing() {
                report.addHeading("Documents in the corpus", 1);
                String header = "<tr><th>Document</th><th>Source</th><th>Excerpt</th></tr>";
                report.addTag("table", TableMaker.makeManually(header, currentCorpus, ts_doc));
                return true;
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        inCorpus.addOption("Open a specific document", new Runnable() {
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

        inDocument.addOption("Print source", new Runnable() {
            @Override
            public void run() {
                print(currentDocument.getSource());
                laterAsk(inDocument);
            }
        });

        inDocument.addOption("Read the document", new HTMLReport("Reading a document") {
            @Override
            public boolean beforeShowing() {
                report.addRawHTML(currentDocument.toHTML());
                return true;
            }

            @Override
            public void afterShowing() {
                laterAsk(inDocument);
            }
        });

        // inCorpus.addOption("Add documents to corpus", UNSUPPORTED); // TODO

        inCorpus.addOption("Tokenize corpus", new HTMLReport("Tokenized") {
            @Override
            public boolean beforeShowing() {
                report.addRawCSS("pre {white-space: pre-wrap;}");
                report.addHeading("Tokenized", 1);
                for (Document d : currentCorpus) {
                    report.addHeading(d.toString(), 2);
                    // report.addTag("pre", d.inspect(Inspector.INSPECTOR_TOKENS));
                    report.addTag("table", d.inspect(Inspector.INSPECTOR_TOKENS_TABLE));
                }
                return true;
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        final ToString<Double> ts_0digits = ToString.getDoubleToString(0);
        final ToString<Double> ts_3digits = ToString.getDoubleToString(3);
        final ToString<Boolean> ts_yesOrNo = ToString.getBooleanToString();

        inCorpus.addOption("See the vocabulary of the corpus", new HTMLReport("Vocabulary") {
            @Override
            public boolean beforeShowing() {

                Set<Token> filteredVocabulary = filterVocabulary();

                if (filteredVocabulary == null) {
                    laterAsk(inCorpus);
                    return false;
                }

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
                columns.add("Token type");
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
                        row.add(token.normalize());
                        row.add(token.getType());
                        if (v_tf)
                            row.add(ts_0digits.alternativeToString(tf));
                        if (v_df)
                            row.add(ts_0digits.alternativeToString(df));
                        if (v_tfdf)
                            row.add(ts_0digits.alternativeToString(tfdf));
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
                return true;
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        inCorpus.addOption("See an n-gram analysis of the corpus", new HTMLReport("N-gram") {
            @Override
            public boolean beforeShowing() {

                Integer n = HOW_MANY.ask();

                if (n == null) {
                    laterAsk(inCorpus);
                    return false;
                }

                n--;

                Set<Token> filteredVocabulary = filterVocabulary();

                if (filteredVocabulary == null) {
                    laterAsk(inCorpus);
                    return false;
                }

                report.addHeading(Integer.toString(n + 1) + "-gram", 1);

                report.addHeading("Count Table", 2);
                report.add(HTMLUtilities.toTable(currentCorpus.getCountTable(n, filteredVocabulary)));

                report.addHeading("Probability Table", 2);
                report.add(HTMLUtilities.toTable(currentCorpus.getProbabilityTable(n, filteredVocabulary)));

                return true;
            }

            @Override
            public void afterShowing() {
                laterAsk(inCorpus);
            }
        });

        // ToString<Double> doubleToString = ToString.getDoubleToString(3);

        // RowPredicate<Textual, Double> p = new RowPredicate<Textual, Double>() {
        // @Override
        // public boolean check(Map<Textual, Double> rowMap) {
        // return rowMap.get(currentCorpus) > 1.0;
        // }
        // };

        // YesNoQuestion m = makeYesNoQuestion("Only see tokens with measures more than
        // 1?");

        inCorpus.addOption("See the TF-IDF of the corpus", new HTMLReport("TF, DF, and TF-IDF") {
            @Override
            public boolean beforeShowing() {
                report.addHeading("Measures", 1);
                report.addHeading("Size", 2);
                List<String> list = new LinkedList<String>();
                list.add("Unique tokens: " + currentCorpus.sizeUnique());
                list.add("Total tokens: " + currentCorpus.sizeNonUnique());
                report.add(HTMLUtilities.toList(list, false));
                report.addHeading("TF", 2);
                report.add(HTMLUtilities.toTable("Token", currentCorpus.getTF(), null, ts_0digits));
                report.addHeading("DF", 2);
                report.add(HTMLUtilities.toTable("Token", currentCorpus.getDF(), null, ts_0digits));
                report.addHeading("TF-IDF", 2);
                report.add(HTMLUtilities.toTable("Token", currentCorpus.getTFIDF(), null, ts_3digits));
                // m.ask() ? p : null
                return true;
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
                laterAsk(start);
            }
        });

        inCorpus.addOption("Get the document frequency of a term in the corpus", new Runnable() {
            @Override
            public void run() {
                print(currentCorpus.getDocumentFrequency(WHAT_TERM.ask()));
                laterAsk(start);
            }
        });

        inCorpus.addOption("Calculate the Minimum Edit Distance", new Runnable() {
            @Override
            public void run() {
                print(LevenshteinDistance.lev(WHAT.ask(), WHAT.ask()));
                laterAsk(start);
            }
        });

        inCorpus.addOption("Normalize a input string", new Runnable() {
            @Override
            public void run() {
                print(Utilities.DEFAULT_NORMALIZER.normalize(WHAT.ask()));
                laterAsk(start);
            }
        });

        // inCorpus.addOption("Stem a input string", UNSUPPORTED); // TODO

        vocabularyFiltering = makeAlternativeQuestion("What parts of the vocabulary should be used?", goTo(inCorpus));
        vocabularyFiltering.addOption("The entire vocabulary");
        vocabularyFiltering.addOption("Only those with high TF×DF");
        vocabularyFiltering.addOption("Only those with high TF÷DF");
        vocabularyFiltering.addOption("Only those with high DF÷n");

        // inCorpus.addOption("", new Runnable() {
        // @Override
        // public void run() {

        // }
        // });

        laterAsk(start);
    }

    public Set<Token> filterVocabulary() {
        TokenMetric metric = null;
        switch (vocabularyFiltering.ask()) {
            case 0:
                return null;
            case 1:
                return currentCorpus.getVocabulary();
            case 2:
                metric = currentCorpus.getMetricTFDF();
                break;
            case 3:
                metric = currentCorpus.getMetricTFIDF();
                break;
            case 4:
                metric = currentCorpus.getMetricDFperN();
                break;
        }

        Double L = makeOpenQuestion("What is the lower cut-off?", (String s) -> Double.parseDouble(s)).ask();
        if (L == null)
            return null;

        Set<Token> filtered = new HashSet<Token>();
        for (Token token : currentCorpus.getVocabulary()) {
            if (metric.measure(token) > L) {
                filtered.add(token);
            }
        }

        return filtered;
    }
}
