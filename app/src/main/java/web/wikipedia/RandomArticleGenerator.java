package web.wikipedia;

import java.io.IOException;

import nl.NaturalLanguage;
import web.html.HTMLDocument;

public class RandomArticleGenerator implements ArticleGenerator {
    private final String url;

    public RandomArticleGenerator(NaturalLanguage nl) {
        this.url = nl.RANDOM_WIKIPEDIA_ARTICLE_URL;
    }

    public RandomArticleGenerator(String url) {
        this.url = url;
    }

    public RandomArticleGenerator() {
        this("https://simple.wikipedia.org/wiki/Special:Random");
    }

    @Override
    public HTMLDocument generate() throws IOException {
        return new HTMLDocument(url);
    }

    public static void main(String[] args) throws Exception {
        new RandomArticleGenerator().generate();
    }
}
