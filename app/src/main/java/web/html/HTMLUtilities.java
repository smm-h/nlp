package web.html;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jile.common.Resource;
import nlp.DocumentElement;
import nlp.HTMLOnlyDocumentElement;
import util.ToString;

import java.awt.Desktop;

public class HTMLUtilities {

    // "<span style=\"color:red;\">"+...+"</span>" TODO

    public static final DocumentElement DEFAULT_STYLE = new HTMLOnlyDocumentElement(
            "<link rel=\"stylesheet\" href=\"C:\\Android\\sdk\\docs\\assets\\css\\default.css\">");

    // "<link
    // rel=\"stylesheet\"href=\"C:\\Gradle\\gradle-7.0\\docs\\dsl\\base.css\">""

    public static boolean show(HTMLDocument d) {
        Resource r = Resource.of(d.toHTML(), "html");
        try {
            Desktop.getDesktop().browse(r.dump().toUri());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> DocumentElement toList(Iterable<T> iterable, boolean ordered) {
        return toList(iterable, ordered, (ToString<T>) ToString.DEFAULT);
    }

    public static <T> DocumentElement toList(Iterable<T> iterable, boolean ordered, ToString<T> alt) {

        // decide on the list tag
        String tag = ordered ? "ol" : "ul";

        // make a string builder to accumulate HTML contents
        StringBuilder b = new StringBuilder();

        b.append("<" + tag + ">");

        for (T item : iterable) {

            // add the item as an HTML list item
            b.append("<li>");
            b.append(alt.alternativeToString(item));
            b.append("</li>\n");
        }

        b.append("</" + tag + ">\n");

        // return the built string as an HTML-only element
        return new HTMLOnlyDocumentElement(b.toString());
    }

    public interface RowPredicate<C, D> {
        public boolean check(Map<C, D> rowMap);
    }

    public static <R, C, D> DocumentElement toTable(Map<R, Map<C, D>> map) {
        return toTable("", map, null);
    }

    @SuppressWarnings("unchecked")
    public static <R, C, D> DocumentElement toTable(Map<R, Map<C, D>> map, RowPredicate<C, D> predicate) {
        return toTable("", map, predicate, (ToString<D>) ToString.DEFAULT);
    }

    public static <R, C, D> DocumentElement toTable(Map<R, Map<C, D>> map, RowPredicate<C, D> predicate,
            ToString<D> alt) {
        return toTable("", map, predicate, alt);
    }

    public static <R, C, D> DocumentElement toTable(String firstCell, Map<R, Map<C, D>> map) {
        return toTable(map, null);
    }

    @SuppressWarnings("unchecked")
    public static <R, C, D> DocumentElement toTable(String firstCell, Map<R, Map<C, D>> map,
            RowPredicate<C, D> predicate) {
        return toTable(map, predicate, (ToString<D>) ToString.DEFAULT);
    }

    public static <R, C, D> DocumentElement toTable(String firstCell, Map<R, Map<C, D>> map,
            RowPredicate<C, D> predicate, ToString<D> alt) {

        // gather an ordered set of all available columns
        Set<C> columns = new TreeSet<C>();
        for (R row : map.keySet()) {
            for (C column : map.get(row).keySet()) {
                columns.add(column);
            }
        }

        // gather an ordered set of all rows
        Set<R> rows = new TreeSet<R>(map.keySet());

        // make a string builder to accumulate HTML contents
        StringBuilder b = new StringBuilder();

        // start the table
        b.append("<table>\n");

        // open the table header row, and a dummy empty header cell
        b.append("<tr><th>");
        b.append(firstCell);
        b.append("</th>");

        // iterate over columns
        for (C column : columns) {

            // add the column as an HTML table cell
            b.append("<th>");
            b.append(column.toString());
            b.append("</th>");
        }

        // end the table header row
        b.append("</tr>\n");

        // iterate over rows
        for (R row : rows) {

            // get the row map
            Map<C, D> rowMap = map.get(row);

            // if it passes the predicate check, or there is no predicate specified,
            if (predicate == null || predicate.check(rowMap)) {

                // open a table row
                b.append("<tr>");

                // add an HTML table cell
                b.append("<td>");
                b.append(row.toString());
                b.append("</td>");

                // iterate over columns
                for (C column : columns) {

                    // get the cell data
                    D data = rowMap.get(column);

                    // add the cell data into an HTML table cell
                    b.append("<td>");
                    b.append(alt.alternativeToString(data));
                    b.append("</td>");
                }

                // end the row
                b.append("</tr>\n");
            }
        }

        // end the table
        b.append("</table>\n");

        // return the built string as an HTML-only element
        return new HTMLOnlyDocumentElement(b.toString());
    }
}
