package web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JEditorPane;

import jile.common.Resource;
import vis.InFrame;

public class HTMLView extends JEditorPane {

    public HTMLView(String body) {
        setEditable(false);
        Resource r = Resource.of(body, "html");
        try {
            URL u = r.dump().toUri().toURL();
            if (u != null) {
                try {
                    setPage(u);
                } catch (IOException e) {
                    System.err.println("Attempted to read a bad URL.");
                }
            } else {
                System.err.println("Couldn't find file.");
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL.");
        }
    }

    public static void main(String[] args) {
        InFrame.show(new HTMLView("<html>Hello, world!</html>"));
    }

    public static <R, C, D> String toTable(Map<R, Map<C, D>> map) {

        // gather an ordered set of all available columns
        Set<C> columns = new TreeSet<C>();
        System.out.println("SIZE: " + map.size());
        for (R row : map.keySet()) {
            for (C column : map.get(row).keySet()) {
                columns.add(column);
            }
        }

        // make a string builder to accumulate HTML contents
        StringBuilder b = new StringBuilder(60);

        // start the table
        b.append("<table>\n");

        // open the table header row, and a dummy empty header cell
        b.append("<tr><th></th>");

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
        for (R row : map.keySet()) {

            // open a table row
            b.append("<tr>");

            // add an HTML table cell
            b.append("<td>");
            b.append(row.toString());
            b.append("</td>");

            // iterate over columns
            Map<C, D> rowData = map.get(row);
            for (C column : columns) {

                // get the cell data
                D data = rowData.get(column);

                // add the cell data into an HTML table cell
                b.append("<td>");
                b.append(data.toString());
                b.append("</td>");
            }

            // end the row
            b.append("</tr>\n");
        }

        // end the table
        b.append("</table>\n");

        // return the built string
        return b.toString();
    }
}