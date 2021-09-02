package web.wikipedia;

import java.io.IOException;

import web.html.HTMLDocument;

public interface ArticleGenerator {
    public HTMLDocument generate() throws IOException;
}
