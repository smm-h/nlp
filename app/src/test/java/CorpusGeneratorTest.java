
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
import nlp.CorpusMeasure;
import nlp.Textual;
import nlp.TokenizedHeading;
import web.html.HTMLDocument;
import web.html.HTMLDocumentElement;
import web.html.HTMLUtilities;
import web.html.HTMLUtilities.RowPredicate;
import web.html.HTMLUtilities.ToString;
import web.wikipedia.DocumentGenerator;
import web.wikipedia.RandomDocumentGenerator;

@SuppressWarnings("unused")
public class CorpusGeneratorTest {
    public static void main(String[] args) {

        // HTMLDocumentElement.DEFAULT_TOKENIZER = DFATokenizerBigTest.getTokenizer();
        HTMLDocumentElement.DEFAULT_TOKENIZER = new Splitter(" .,;'\"()[]،؛!?؟");

        Corpus corpus = generateCorpus(new RandomDocumentGenerator(), 50); // new Farsi()

        HTMLDocument report = new HTMLDocument();

        report.add(HTMLUtilities.DEFAULT_STYLE);
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

        CorpusMeasure tfidf = corpus.getTFIDF();
        CorpusMeasure tfdf = corpus.getTFDF();

        ToString<Double> doubleToString = new ToString<Double>() {
            @Override
            public String alternativeToString(Double value) {
                return String.format("%.3f", value);
            }
        };

        RowPredicate<Textual, Double> p1 = new RowPredicate<Textual, Double>() {
            @Override
            public boolean check(Map<Textual, Double> rowMap) {
                return rowMap.get(corpus) > 1.0;
            }
        };

        report.add(new TokenizedHeading(new ArrayTokenized("TF-IDF"), 2));
        report.add(HTMLUtilities.toTable("Token", tfidf, getPredicate(tfidf), doubleToString)); // getPredicate(tfidf)

        report.add(new TokenizedHeading(new ArrayTokenized("TF-DF"), 2));
        report.add(HTMLUtilities.toTable("Token", tfdf, getPredicate(tfdf), doubleToString));

        HTMLUtilities.show(report);
    }

    static RowPredicate<Textual, Double> getPredicate(CorpusMeasure measure) {

        Corpus corpus = measure.getCorpus();

        double threshold = measure.getSorted()[measure.size() - 50];

        System.out.println("Threshold: " + threshold);

        return new RowPredicate<Textual, Double>() {
            @Override
            public boolean check(Map<Textual, Double> rowMap) {
                return rowMap.get(corpus) >= threshold;
            }
        };
    }

    static Corpus generateCorpus(DocumentGenerator generator, int count) {
        Corpus corpus = new HashCorpus();
        int f = 0;
        for (int i = 0; i < count; i++) {
            try {
                corpus.add(generator.generate());
            } catch (IOException e) {
                System.err.println("Failed to load article");
                f++;
                if (f > 3 && i == 0) {
                    System.err.println("Failed more than 3 times");
                    return null;
                }
                i--;
            }
        }
        return corpus;
    }
}
