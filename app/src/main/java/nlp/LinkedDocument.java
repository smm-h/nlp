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
    public String toString() {
        return "D" + index;
    }

    @Override
    public int hashCode() {
        return index << 10;
    }

    @Override
    public boolean isSourceURL() {
        return false;
    }

    @Override
    public String getSource() {
        return "N/A";
    }

}
