package nlp;

import web.wikipedia.*;

public class App {
    public static void main(String[] args) throws Exception {
        Article article = new RandomArticleGenerator().generate();
        // article.getContents();
    }
}
