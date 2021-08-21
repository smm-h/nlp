package web.wikipedia;

public class Article {

    private final String source;
    private final String contents;

    public Article(String url, String contents) {
        this.source = url;
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public String getSource() {
        return source;
    }
}
