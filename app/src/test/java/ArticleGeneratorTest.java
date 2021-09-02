
import java.io.IOException;

import nl.NaturalLanguage;
import nl.languages.Farsi;
import vis.InFrame;
import web.html.HTMLView;
import web.wikipedia.ArticleGenerator;
import web.wikipedia.RandomArticleGenerator;

public class ArticleGeneratorTest {
    public static void main(String[] args) {

        NaturalLanguage farsi = new Farsi();
        ArticleGenerator r = new RandomArticleGenerator(farsi);
        try {
            InFrame.show(new HTMLView(r.generate().toHTML()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
