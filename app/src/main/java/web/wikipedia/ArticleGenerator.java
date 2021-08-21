package web.wikipedia;

import java.io.IOException;

public interface ArticleGenerator {
    public Article generate() throws IOException;
}
