import nlp.ArrayTokenized;
import nlp.Corpus;
import nlp.Document;
import nlp.DocumentUtilities;
import nlp.HashCorpus;
import nlp.TokenizedHeading;
import nlp.TokenizedParagraph;
import nlp.ngram.CountTable;
import nlp.ngram.ProbabilityTable;
import web.html.HTMLDocument;
import web.html.HTMLUtilities;

public class NgramTest {
    public static void main(String[] args) {
        Document d1 = DocumentUtilities.getDocumentFromCharsAsTokens("acaa.");
        Document d2 = DocumentUtilities.getDocumentFromCharsAsTokens("abba.");
        Document d3 = DocumentUtilities.getDocumentFromCharsAsTokens("ccab.");
        Document d4 = DocumentUtilities.getDocumentFromCharsAsTokens("aacc.");
        Document d5 = DocumentUtilities.getDocumentFromCharsAsTokens("abac.");
        Corpus corpus = new HashCorpus(d1, d2, d3, d4, d5);

        int n = 2; // bi-gram
        CountTable ct = corpus.getCountTable(n);
        ProbabilityTable pt = corpus.getProbabilityTable(n);

        System.out.println(pt.getProbabilityOf(DocumentUtilities.getTermFromCharsAsTokens("acc.")));

        HTMLDocument report = new HTMLDocument();
        report.add(HTMLUtilities.DEFAULT_STYLE);
        report.add(new TokenizedHeading(new ArrayTokenized("Report"), 1));

        report.add(new TokenizedHeading(new ArrayTokenized("Count Table"), 2));
        report.add(HTMLUtilities.toTable(ct));

        report.add(new TokenizedHeading(new ArrayTokenized("Probability Table"), 2));
        report.add(HTMLUtilities.toTable(pt));

        report.add(new TokenizedHeading(new ArrayTokenized("Notes"), 2));
        report.add(new TokenizedParagraph(new ArrayTokenized("Generated by https://github.com/smm-h/nlp")));

        HTMLUtilities.show(report);
    }

}