package util;

import java.util.Iterator;

import jile.common.Common;
import jile.common.Convert;

public class ArrayTuple<T> implements Tuple<T> {

    private final T[] array;

    public ArrayTuple(T[] array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return Common.makeArrayIterator(array);
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        return array[index] = element;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < array.length; i++)
            h ^= array[i].hashCode();
        return h;
    }

    @Override
    public String toString() {
        return Convert.Array_to_String(array);
    }

}
