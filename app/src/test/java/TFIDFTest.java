
import nlp.Corpus;
import nlp.Document;
import nlp.DocumentUtilities;
import nlp.HashCorpus;
import web.html.HTMLDocument;
import web.html.HTMLUtilities;

public class TFIDFTest {
    public static void main(String[] args) {
        Document d1 = DocumentUtilities.getDocumentFromCharsAsTokens("abcabc");
        Document d2 = DocumentUtilities.getDocumentFromCharsAsTokens("acacaacc");
        Document d3 = DocumentUtilities.getDocumentFromCharsAsTokens("addebad");
        Corpus corpus = new HashCorpus(d1, d2, d3);

        HTMLDocument report = new HTMLDocument();
        report.add(HTMLUtilities.DEFAULT_STYLE);
        report.add(HTMLUtilities.toTable(corpus.getTFIDF()));
        HTMLUtilities.show(report);
    }
}
