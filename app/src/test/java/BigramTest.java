import nlp.ArrayTokenized;
import nlp.Corpus;
import nlp.Document;
import nlp.DocumentUtilities;
import nlp.HashCorpus;
import nlp.TokenizedHeading;
import web.html.HTMLDocument;
import web.html.HTMLUtilities;

public class BigramTest {
    public static void main(String[] args) {
        Document d1 = DocumentUtilities.charsAsTokens("acaa.");
        Document d2 = DocumentUtilities.charsAsTokens("abba.");
        Document d3 = DocumentUtilities.charsAsTokens("ccab.");
        Document d4 = DocumentUtilities.charsAsTokens("aacc.");
        Document d5 = DocumentUtilities.charsAsTokens("abac.");
        Corpus corpus = new HashCorpus(d1, d2, d3, d4, d5);

        HTMLDocument report = new HTMLDocument();
        report.add(HTMLUtilities.DEFAULT_STYLE);
        report.add(new TokenizedHeading(new ArrayTokenized("Report"), 1));

        report.add(new TokenizedHeading(new ArrayTokenized("Count Table"), 2));
        report.add(HTMLUtilities.toTable(corpus.getCountTable()));

        report.add(new TokenizedHeading(new ArrayTokenized("Probability Table"), 2));
        report.add(HTMLUtilities.toTable(corpus.getProbabilityTable()));
        HTMLUtilities.show(report);
    }

}
