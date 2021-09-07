package nlp;

import java.util.List;

public interface Term extends List<Token>, Comparable<Term> {

    public default Token getSafe(int index) {
        return Utilities.getSafe(this, index);
    }

    @Override
    public default int compareTo(Term other) {
        return Integer.compare(hashCode(), other.hashCode());
    }
}
