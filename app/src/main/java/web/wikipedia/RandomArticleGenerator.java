package web.wikipedia;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RandomArticleGenerator implements ArticleGenerator {
    private final String url;

    public RandomArticleGenerator(String url) {
        this.url = url;
    }

    public RandomArticleGenerator() {
        this("https://simple.wikipedia.org/wiki/Special:Random");
    }

    @Override
    public Article generate() throws IOException {
        Document document = Jsoup.connect(url).followRedirects(true).timeout(60000).get();
        String source = document.baseUri();
        System.out.println(source);
        Elements all = document.body().getElementById("content").getAllElements();
        String p = null;
        for (Element element : all) {
            // if (isText(element)) {
            if (element.tagName().equals("p")) {
                System.out.println("");
                // System.out.println(element.tagName());
                System.out.println(element.text());
                p = element.text();
                break;
            }
        }
        return new Article(source, p);
    }

    private boolean isText(Element element) {
        String tag = element.tagName();
        return tag.equals("p") || tag.charAt(0) == 'h';
    }

    public static void main(String[] args) throws Exception {
        new RandomArticleGenerator().generate();
    }
}
