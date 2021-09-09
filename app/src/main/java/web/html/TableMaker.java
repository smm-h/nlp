package web.html;

import java.util.List;

public class TableMaker {
    private final int columns;
    private int rows;
    private final StringBuilder builder;

    public TableMaker(String... headers) {
        rows = 0;
        columns = headers.length;
        builder = new StringBuilder();
        builder.append("<table>");
        builder.append("<tr>");
        for (int i = 0; i < columns; i++) {
            builder.append("<th>");
            builder.append(headers[i]);
            builder.append("</th>");
        }
        builder.append("</tr>");
    }

    public TableMaker(List<String> headers) {
        rows = 0;
        columns = headers.size();
        builder = new StringBuilder();
        builder.append("<table>");
        builder.append("<tr>");
        for (int i = 0; i < columns; i++) {
            builder.append("<th>");
            builder.append(headers.get(i));
            builder.append("</th>");
        }
        builder.append("</tr>");
    }

    public void add(String... data) {
        if (data.length != columns)
            throw new IllegalArgumentException("incorrect length of array");
        builder.append("<tr>");
        for (int i = 0; i < columns; i++) {
            builder.append("<td>");
            builder.append(data[i]);
            builder.append("</td>");
        }
        builder.append("</tr>");
        rows++;
    }

    public void add(List<String> data) {
        if (data.size() != columns)
            throw new IllegalArgumentException("incorrect length of array");
        builder.append("<tr>");
        for (int i = 0; i < columns; i++) {
            builder.append("<td>");
            builder.append(data.get(i));
            builder.append("</td>");
        }
        builder.append("</tr>");
        rows++;
    }

    public String finish() {
        if (rows == 0) {
            return "";
        } else {
            builder.append("</table>");
            return builder.toString();
        }
    }

    public interface RowMaker<T> {
        public List<String> make(T thing);
    }

    public <T> String addRows(Iterable<T> rows, RowMaker<T> rowMaker) {

        for (T thing : rows)
            add(rowMaker.make(thing));

        return finish();
    }
}
