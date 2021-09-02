package nlp;

import java.util.List;

public interface Document extends List<DocumentElement>, Textual, Comparable<Document> {

    public default String toHTML() {
        StringBuilder builder = new StringBuilder();
        for (DocumentElement element : this)
            builder.append(element.toHTML());
        return builder.toString();
    }

    public default String toMarkdown() {
        StringBuilder builder = new StringBuilder();
        for (DocumentElement element : this)
            builder.append(element.toMarkdown());
        return builder.toString();
    }

    public default String toPlainText() {
        StringBuilder builder = new StringBuilder();
        for (DocumentElement element : this)
            builder.append(element.toPlainText());
        return builder.toString();
    }

    @Override
    public default boolean contains(Term term) {

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Textual) {
                Textual text = (Textual) element;

                // delegate containment check to it
                if (text.contains(term))
                    return true;
            }
        }
        return false;
    }

    @Override
    public default int sizeNonUnique() {

        // keep track of my non-unique size
        int size = 0;

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Textual) {
                Textual text = (Textual) element;

                // delegate size count to it
                size += text.sizeNonUnique();
            }
        }
        return size;
    }

    @Override
    public default Vocabulary getVocabulary() {

        // keep track of my unique tokens
        Vocabulary v = new HashVocabulary();

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Textual) {
                Textual text = (Textual) element;

                // union my vocabulary with its
                v.addAll(text.getVocabulary());
            }
        }
        return v;
    }

    @Override
    public default int getTermFrequency(Term term) {

        // keep track of the frequency of the term
        int tf = 0;

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Textual) {
                Textual text = (Textual) element;

                // delegate term-frequency to it
                tf += text.getTermFrequency(term);
            }
        }
        return tf;
    }
}
