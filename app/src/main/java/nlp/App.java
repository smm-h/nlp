package nlp;

import nl.NaturalLanguage;
import nl.languages.Farsi;
import web.wikipedia.ArticleGenerator;
import web.wikipedia.RandomArticleGenerator;

public class App {
    public static void main(String[] args) throws Exception {
        demo(2);
    }

    public static void demo(int demo) throws Exception {
        switch (demo) {
            case 1:
                NaturalLanguage farsi = new Farsi();
                ArticleGenerator r = new RandomArticleGenerator(farsi);
                // Document article =
                r.generate();
                // article.getContents();
                break;
            case 2:
                Document d1 = charsAsTokens("abcabc");
                Document d2 = charsAsTokens("acacaacc");
                Document d3 = charsAsTokens("addebad");
                Corpus corpus = new HashCorpus(d1, d2, d3);
                corpus.getTFIDF();
                break;
        }
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
