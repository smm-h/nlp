package nlp;

import java.util.List;

public interface Paragraph extends DocumentElement, Text, List<Token> {
    @Override
    public default String toHTML() {
        return "<p>" + toPlainText() + "</p>";
    }

    @Override
    public default String toMarkdown() {
        return toPlainText() + "\n";
    }
}
