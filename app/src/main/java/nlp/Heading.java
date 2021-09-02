package nlp;

public interface Heading extends DocumentElement, Textual {
    @Override
    public default String toHTML() {
        String tag = "h" + getLevel();
        return "<" + tag + ">" + toPlainText() + "</" + tag + ">";
    }

    /**
     * @return [1, ..., 6]
     */
    public int getLevel();

    @Override
    public default String toMarkdown() {
        return "#".repeat(getLevel()) + " " + toPlainText() + "\n\n";
    }
}
