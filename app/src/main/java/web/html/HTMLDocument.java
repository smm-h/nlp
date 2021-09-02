package web.html;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nlp.LinkedDocument;

public class HTMLDocument extends LinkedDocument {

    private final String source;

    public HTMLDocument(String url) throws IOException {
        this.source = url;

        Document document = Jsoup.connect(url).followRedirects(true).timeout(60000).get();
        String source = document.baseUri();
        System.out.println(source);
        Elements elements = document.body().getElementById("content").getAllElements();
        for (Element e : elements) {
            if (HTMLDocumentElement.supports(e.tagName())) {
                add(HTMLDocumentElement.make(e));
            }
        }
    }

    public String getSource() {
        return source;
    }
}
