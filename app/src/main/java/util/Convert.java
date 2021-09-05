package util;

import java.util.Collection;

public class Convert {

    @SuppressWarnings("unchecked")
    public static <T> T[] Collection_to_Array(Collection<T> collection) {
        T[] array = (T[]) new Object[collection.size()];
        int i = 0;
        for (T t : collection)
            array[i++] = t;
        return array;
    }

}
