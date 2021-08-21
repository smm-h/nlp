package nlp;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RandomArticle {
    private final String url;

    public RandomArticle(String url) {
        this.url = url;
    }

    public RandomArticle() {
        this.url = "https://simple.wikipedia.org/wiki/Special:Random";
    }

    public String get() throws IOException {
        Document document = Jsoup.connect(url).followRedirects(true).timeout(60000).get();
        System.out.println(document.baseUri());
        Elements all = document.body().getElementById("content").getAllElements();
        for (Element element : all) {
            // if (isText(element)) {
            if (element.tagName().equals("p")) {
                System.out.println("");
                System.out.println(element.tagName());
                System.out.println(element.text());
            }
        }
        return null;
    }

    private boolean isText(Element element) {
        String tag = element.tagName();
        return tag.equals("p") || tag.charAt(0) == 'h';
    }

    public static void main(String[] args) throws Exception {
        new RandomArticle().get();
    }
}
