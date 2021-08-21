package web.wikipedia;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jile.common.LinkedTree;
import jile.common.MutableTree;
import jile.vis.Viewer;

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
        Element e = document.body().getElementById("content");
        // String p = null;
        MutableTree<String> tree = new LinkedTree<String>();
        tree.addAndGoTo(source);
        populate(tree, e.children());
        tree.goBack();
        Viewer.singleton().show(tree.visualize());
        return null;
        // return new Article(source, p);
    }

    private void populate(MutableTree<String> tree, Elements elements) {
        for (Element e : elements) {
            String tag = e.tagName();
            if (tag.charAt(0) == 'h') {
                String s = Element_to_String(e);
                if (s.length() > 4) {
                    tree.addAndGoTo(s);
                    populate(tree, e.children());
                    tree.goBack();
                }
            } else if (tag.equals("p")) {
                String s = Element_to_String(e);
                if (s.length() > 4) {
                    System.out.println("");
                    System.out.println(e.tagName());
                    System.out.println(e.text());
                    tree.add(s);
                }
            } else {
                populate(tree, e.children());
            }
        }
    }

    private String Element_to_String(Element e) {
        String t = e.text().trim();
        return e.tagName() + ": " + (t.length() > 16 ? t.substring(0, 16) : t);
    }

    public static void main(String[] args) throws Exception {
        new RandomArticleGenerator().generate();
    }
}
