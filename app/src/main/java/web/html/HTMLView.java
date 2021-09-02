package web.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
}