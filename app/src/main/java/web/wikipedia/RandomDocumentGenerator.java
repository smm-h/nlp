package web.wikipedia;

import java.io.IOException;

import nlp.languages.NaturalLanguage;
import web.html.HTMLDocument;

public class RandomDocumentGenerator implements DocumentGenerator {
    private final String url;

    public RandomDocumentGenerator(NaturalLanguage nl) {
        this.url = nl.getRandomWikipediaArticleURL();
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
