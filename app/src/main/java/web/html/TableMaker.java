package web.html;

import util.ToString;

public class TableMaker {
    private final int columns;
    private final StringBuilder builder;

    private ToString<Object>[] toStrings;

    @SuppressWarnings("unchecked")
    public TableMaker(String... headers) {
        columns = headers.length;
        toStrings = new ToString[columns];
        builder = new StringBuilder();
        builder.append("<table>");
        builder.append("<tr>");
        for (int i = 0; i < columns; i++) {
            builder.append("<th>");
            builder.append(headers[i]);
            builder.append("</th>");
            toStrings[i] = ToString.DEFAULT;
        }
        builder.append("</tr>");
    }

    public void setToString(int index, ToString<Object> ts) {
        toStrings[index] = ts;
    }

    public void add(Object... data) {
        if (data.length != columns)
            throw new IllegalArgumentException("incorrect length of array");
        builder.append("<tr>");
        for (int i = 0; i < columns; i++) {
            builder.append("<td>");
            builder.append(toStrings[i].alternativeToString(data[i]));
            builder.append("</td>");
        }
        builder.append("</tr>");
    }

    public String finish() {
        builder.append("</table>");
        return builder.toString();
    }
}
