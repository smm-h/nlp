package nlp;

public interface Paragraph extends DocumentElement, Textual {
    @Override
    public default String toHTML() {
        return "<p>" + toPlainText() + "</p>";
    }

    @Override
    public default String toMarkdown() {
        return toPlainText() + "\n\n";
    }
}
