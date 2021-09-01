
import nlp.ArrayParagraph;
import nlp.Corpus;
import nlp.Document;
import nlp.HashCorpus;
import nlp.LinkedDocument;
import vis.InFrame;
import web.HTMLView;

public class TFIDFTest {
    public static void main(String[] args) {
        Document d1 = charsAsTokens("abcabc");
        Document d2 = charsAsTokens("acacaacc");
        Document d3 = charsAsTokens("addebad");
        Corpus corpus = new HashCorpus(d1, d2, d3);
        InFrame.show(new HTMLView(HTMLView.toTable(corpus.getTFIDF())));
    }

    private static Document charsAsTokens(String s) {
        String[] a = new String[s.length()];
        int i = 0;
        for (char c : s.toCharArray()) {
            a[i++] = Character.toString(c);
        }
        return new LinkedDocument(new ArrayParagraph(a));
    }
}
