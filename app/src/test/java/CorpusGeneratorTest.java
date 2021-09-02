
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import nl.NaturalLanguage;
import nl.languages.Farsi;
import nlp.ArrayTokenized;
import nlp.Corpus;
import nlp.Document;
import nlp.HashCorpus;
import nlp.Splitter;
import nlp.TFIDF;
import nlp.Textual;
import nlp.TokenizedHeading;
import web.html.HTMLDocument;
import web.html.HTMLDocumentElement;
import web.html.HTMLUtilities;
import web.html.HTMLUtilities.RowPredicate;
import web.html.HTMLUtilities.ToString;
import web.wikipedia.DocumentGenerator;
import web.wikipedia.RandomDocumentGenerator;

public class CorpusGeneratorTest {
    public static void main(String[] args) {

        // HTMLDocumentElement.DEFAULT_TOKENIZER = DFATokenizerBigTest.getTokenizer();
        HTMLDocumentElement.DEFAULT_TOKENIZER = new Splitter(" .,;'\"()[]،؛!?؟");

        NaturalLanguage farsi = new Farsi();

        Corpus corpus = generateCorpus(farsi, 5);

        HTMLDocument report = new HTMLDocument();

        report.add(new TokenizedHeading(new ArrayTokenized("Report"), 1));

        ToString<Document> documentToString = new ToString<Document>() {
            @Override
            public String alternativeToString(Document document) {
                try {
                    return document.toString() + ": " + URLDecoder.decode(document.getSource(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return document.toString() + ": " + document.getSource();
                }
            }
        };

        report.add(new TokenizedHeading(new ArrayTokenized("Articles used in the corpus"), 2));
        report.add(HTMLUtilities.toList(corpus, false, documentToString));

        TFIDF tfidf = corpus.getTFIDF();

        ToString<Double> doubleToString = new ToString<Double>() {
            @Override
            public String alternativeToString(Double value) {
                return String.format("%.3f", value);
            }
        };

        report.add(new TokenizedHeading(new ArrayTokenized("TF-IDF"), 2));
        report.add(HTMLUtilities.toTable("Token", tfidf, null, doubleToString)); // getPredicate(tfidf)

        HTMLUtilities.show(report);
    }

    static RowPredicate<Textual, Double> getPredicate(TFIDF tfidf) {

        Corpus corpus = tfidf.getCorpus();

        double threshold = tfidf.getSorted()[tfidf.size() * 90 / 100];

        System.out.println("Threshold: " + threshold);

        return new RowPredicate<Textual, Double>() {
            @Override
            public boolean check(Map<Textual, Double> rowMap) {
                return rowMap.get(corpus) >= threshold;
            }
        };
    }

    static Corpus generateCorpus(NaturalLanguage nl, int count) {
        Corpus corpus = new HashCorpus();
        DocumentGenerator r = new RandomDocumentGenerator(nl);
        int f = 0;
        for (int i = 0; i < count; i++) {
            try {
                corpus.add(r.generate());
            } catch (IOException e) {
                i--;
                System.err.println("Failed to load article");
                f++;
                if (f > 3 && i == 0) {
                    System.err.println("Failed more than 3 times");
                    return null;
                }
            }
        }
        return corpus;
    }
}
