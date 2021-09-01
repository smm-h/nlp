
import java.io.IOException;

import nl.NaturalLanguage;
import nl.languages.Farsi;
import web.wikipedia.ArticleGenerator;
import web.wikipedia.RandomArticleGenerator;

public class ArticleGeneratorTest {
    public static void main(String[] args) {

        NaturalLanguage farsi = new Farsi();
        ArticleGenerator r = new RandomArticleGenerator(farsi);
        // Document article =
        try {
            r.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // article.getContents();
    }
}
