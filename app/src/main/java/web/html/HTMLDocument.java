package web.html;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nlp.ArrayTokenized;
import nlp.HTMLOnlyDocumentElement;
import nlp.LinkedDocument;
import nlp.TokenizedHeading;
import nlp.TokenizedParagraph;

public class HTMLDocument extends LinkedDocument {

    private final String source;

    public HTMLDocument(HTMLDocumentElement... elements) {
        source = null;
        for (HTMLDocumentElement e : elements) {
            add(e);
        }
    }

    public HTMLDocument(String url) throws IOException {
        Document document = Jsoup.connect(url).followRedirects(true).timeout(60000).get();
        source = document.baseUri();
        System.out.println(source);
        Elements elements = document.body().getElementById("content").getAllElements();
        for (Element e : elements) {
            if (HTMLDocumentElement.supports(e.tagName())) {
                add(HTMLDocumentElement.make(e));
            }
        }
    }

    @Override
    public boolean isSourceURL() {
        return true;
    }

    @Override
    public String getSource() {
        return source;
    }

    public void addHeading(String text, int level) {
        add(new TokenizedHeading(new ArrayTokenized(text), level));
    }

    public void addParagraph(String text) {
        add(new TokenizedParagraph(new ArrayTokenized(text)));
    }

    public void addTag(String tag, String contents) {
        addTag(tag, contents, null);
    }

    public void addTag(String tag, String contents, String options) {
        options = options == null || options.isEmpty() || options.isBlank() ? "" : " " + options;
        addRawHTML("<" + tag + options + ">" + contents + "</" + tag + ">");
    }

    public void addRawCSS(String css) {
        addRawHTML("<style>" + css + "</style>");
    }

    public void addRawHTML(String html) {
        add(new HTMLOnlyDocumentElement(html));
    }
}
