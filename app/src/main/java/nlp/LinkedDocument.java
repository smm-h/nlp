package nlp;

import java.util.LinkedList;

public class LinkedDocument extends LinkedList<DocumentElement> implements Document {

    public LinkedDocument(DocumentElement... elements) {
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

}
