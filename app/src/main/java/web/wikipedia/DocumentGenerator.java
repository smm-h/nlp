package web.wikipedia;

import java.io.IOException;

import web.html.HTMLDocument;

public interface DocumentGenerator {
    public HTMLDocument generate() throws IOException;
}
