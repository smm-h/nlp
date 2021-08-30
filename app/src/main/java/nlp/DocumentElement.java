package nlp;

/**
 * A {@link Document} is a sequential collection of Document Elements, which
 * include headings, paragraphs and other objects like lists, tables, trees,
 * graphs, charts, pictures, formulas, code, etc.
 */
public interface DocumentElement {

    public String toHTML();

    public String toMarkdown();

    public String toPlainText();
}
