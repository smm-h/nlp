package web.html;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;

import nlp.DocumentElement;
import nlp.Splitter;
import nlp.Tokenizer;
import nlp.Tokenized;
import nlp.TokenizedHeading;
import nlp.TokenizedParagraph;

public abstract class HTMLDocumentElement implements DocumentElement {

    public static Tokenizer DEFAULT_TOKENIZER;
    private static Set<String> SUPPORTED_TAGS;

    static {
        DEFAULT_TOKENIZER = new Splitter(" ");
        SUPPORTED_TAGS = new HashSet<String>();
        SUPPORTED_TAGS.add("p");
        SUPPORTED_TAGS.add("h1");
        SUPPORTED_TAGS.add("h2");
        SUPPORTED_TAGS.add("h3");
        SUPPORTED_TAGS.add("h4");
        SUPPORTED_TAGS.add("h5");
        SUPPORTED_TAGS.add("h6");
    }

    public static boolean supports(String tag) {
        return SUPPORTED_TAGS.contains(tag.toLowerCase());
    }

    private final String contents;

    public HTMLDocumentElement(String contents) {
        this.contents = contents;
    }

    public static DocumentElement make(Element e) {
        return make(e, DEFAULT_TOKENIZER);
    }

    public static DocumentElement make(Element e, Tokenizer tokenizer) {
        return make(e.tagName(), e.text(), tokenizer);
    }

    public static DocumentElement make(String tag, String data) {
        return make(tag, data, DEFAULT_TOKENIZER);
    }

    public static DocumentElement make(String tag, String data, Tokenizer tokenizer) {
        return make(tag, tokenizer.tokenize(data));
    }

    public static DocumentElement make(String tag, Tokenized tokenized) {
        switch (tag.toLowerCase()) {
            case "p":
                return new TokenizedParagraph(tokenized);
            case "h1":
                return new TokenizedHeading(tokenized, 1);
            case "h2":
                return new TokenizedHeading(tokenized, 2);
            case "h3":
                return new TokenizedHeading(tokenized, 3);
            case "h4":
                return new TokenizedHeading(tokenized, 4);
            case "h5":
                return new TokenizedHeading(tokenized, 5);
            case "h6":
                return new TokenizedHeading(tokenized, 6);
            default:
                System.err.println("Unsupported tag: " + tag);
                return null;
        }
    }

    @Override
    public String toHTML() {
        String tag = getTag();
        return "<" + tag + ">" + contents + "</" + tag + ">";
    }

    public abstract String getTag();

    @Override
    public String toPlainText() {
        return contents;
    }

}