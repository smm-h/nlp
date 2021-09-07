package nlp;

public class HTMLOnlyDocumentElement implements DocumentElement {

    private final String html;

    public HTMLOnlyDocumentElement(String html) {
        this.html = html;
    }

    @Override
    public String inspect(Inspector inspector) {
        System.out.println("Could not inspect: " + html);
        return html;
    }

    @Override
    public String toHTML() {
        return html;
    }

    @Override
    public String toMarkdown() {
        return toHTML();
    }

    @Override
    public String toPlainText() {
        return toHTML();
    }

}