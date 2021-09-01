package nlp;

import java.util.List;

public interface Paragraph extends DocumentElement, Textual, List<Token> {
    @Override
    public default String toHTML() {
        return "<p>" + toPlainText() + "</p>";
    }

    @Override
    public default String toMarkdown() {
        return toPlainText() + "\n";
    }
}
