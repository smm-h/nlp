package nlp;

import java.util.LinkedList;

public class LinkedDocument extends LinkedList<DocumentElement> implements Document {

    private static int indexCounter = 0;
    private int index;

    public LinkedDocument(DocumentElement... elements) {
        index = ++indexCounter;
        for (DocumentElement element : elements) {
            add(element);
        }
    }

    @Override
    public boolean contains(Term term) {

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Text) {
                Text text = (Text) element;

                // delegate containment check to it
                if (text.contains(term))
                    return true;
            }
        }
        return false;
    }

    @Override
    public int sizeNonUnique() {

        // keep track of my non-unique size
        int size = 0;

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Text) {
                Text text = (Text) element;

                // delegate size count to it
                size += text.sizeNonUnique();
            }
        }
        return size;
    }

    @Override
    public Vocabulary getVocabulary() {

        // keep track of my unique tokens
        Vocabulary v = new HashVocabulary();

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Text) {
                Text text = (Text) element;

                // union my vocabulary with its
                v.addAll(text.getVocabulary());
            }
        }
        return v;
    }

    @Override
    public int getTermFrequency(Term term) {

        // keep track of the frequency of the term
        int tf = 0;

        // iterate through my elements
        for (DocumentElement element : this) {

            // if any is textual,
            if (element instanceof Text) {
                Text text = (Text) element;

                // delegate term-frequency to it
                tf += text.getTermFrequency(term);
            }
        }
        return tf;
    }

    @Override
    public String toString() {
        return "D" + index;
    }

    @Override
    public int hashCode() {
        return index << 10;
    }

    @Override
    public int compareTo(Document o) {
        return Integer.compare(hashCode(), o.hashCode());
    }

}
