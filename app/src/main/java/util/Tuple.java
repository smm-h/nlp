package util;

public interface Tuple<T> extends Iterable<T> {

    public int size();

    public T get(int index);

    public T set(int index, T element);

    public static <T> int collectiveHash(T[] array) {
        int h = 0;
        for (int i = 0; i < array.length; i++)
            h ^= array[i].hashCode();
        return h;
    }

    public static <T> int collectiveHash(Iterable<T> iterable) {
        int h = 0;
        for (T item : iterable)
            h ^= item.hashCode();
        return h;
    }
}
