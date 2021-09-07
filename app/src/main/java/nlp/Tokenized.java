package nlp;

import java.util.List;

public interface Tokenized extends List<Token>, Textual {

    public default Token getSafe(int index) {
        return Utilities.getSafe(this, index);
    }

    public default String getContents() {
        StringBuilder builder = new StringBuilder();
        for (Token token : this) {
            builder.append(token.getUnnormalized());
            builder.append(" ");
        }
        return builder.toString();
    }

    @Override
    public default boolean contains(Term term) {

        Token head = term.get(0);
        int n = size();
        int m = term.size();

        // iterate through my tokens
        for (int i = 0; i < n; i++) {

            // if any equal the head of the term,
            if (get(i).equals(head)) {

                // check to see if the rest also matches
                int j = 1;
                for (; j < m; j++) {

                    // if even one token does not match,
                    if (!get(i + j).equals(term.get(j))) {

                        // move on
                        break;
                    }
                }

                // if the loop finished without breaking;
                if (j == m) {

                    // a complete match was found, search no longer
                    return true;
                }
            }
        }

        // search was finished, and a match was not found
        return false;
    }

    @Override
    public default int sizeNonUnique() {
        return size();
    }

    @Override
    public default int getTermFrequency(Term term) {

        // assume I do not contain the term
        int tf = 0;

        Token head = term.get(0);
        int n = size();
        int m = term.size();

        // iterate through my tokens
        for (int i = 0; i < n; i++) {

            // if any equal the head of the term,
            if (get(i).equals(head)) {

                // check to see if the rest also matches
                int j = 1;
                for (; j < m; j++) {

                    // if even one token does not match,
                    if (!get(i + j).equals(term.get(j))) {

                        // move on
                        break;
                    }
                }

                // if the loop finished without breaking;
                if (j == m) {

                    // a complete match was found
                    tf++;
                }
            }
        }

        return tf;
    }
}
