package nlp;

import java.util.HashSet;

public class HashCorpus extends HashSet<Document> implements Corpus {

    public HashCorpus(Document... documents) {
        for (Document d : documents) {
            add(d);
        }
    }

    @Override
    public boolean contains(Term term) {

        // iterate through my elements
        for (Document document : this) {

            // delegate containment check to it
            if (document.contains(term))
                return true;
        }
        return false;
    }

    @Override
    public int sizeNonUnique() {

        // keep track of my non-unique size
        int size = 0;

        // iterate through my elements
        for (Document document : this) {

            // delegate size count to it
            size += document.sizeNonUnique();
        }
        return size;
    }

    @Override
    public Vocabulary getVocabulary() {

        // keep track of my unique tokens
        Vocabulary v = new HashVocabulary();

        // iterate through my elements
        for (Document document : this) {

            // union my vocabulary with its
            v.addAll(document.getVocabulary());
        }
        return v;
    }

    @Override
    public int getTermFrequency(Term term) {

        // keep track of the frequency of the term
        int tf = 0;

        // iterate through my elements
        for (Document document : this) {

            // delegate term-frequency to it
            tf += document.getTermFrequency(term);
        }
        return tf;
    }

}
