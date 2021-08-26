package nlp;

import nl.NaturalLanguage;
import nl.languages.Farsi;
import web.wikipedia.*;

public class App {
    public static void main(String[] args) throws Exception {
        NaturalLanguage farsi = new Farsi();
        ArticleGenerator r = new RandomArticleGenerator(farsi);
        Document article = r.generate();
        // article.getContents();
    }
}
