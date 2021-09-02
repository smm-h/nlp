package web.wikipedia;

import java.io.IOException;

import nl.NaturalLanguage;
import web.html.HTMLDocument;

public class RandomDocumentGenerator implements DocumentGenerator {
    private final String url;

    public RandomDocumentGenerator(NaturalLanguage nl) {
        this.url = nl.RANDOM_WIKIPEDIA_ARTICLE_URL;
    }

    public RandomDocumentGenerator(String url) {
        this.url = url;
    }

    public RandomDocumentGenerator() {
        this("https://simple.wikipedia.org/wiki/Special:Random");
    }

    @Override
    public HTMLDocument generate() throws IOException {
        return new HTMLDocument(url);
    }

    public static void main(String[] args) throws Exception {
        new RandomDocumentGenerator().generate();
    }
}
