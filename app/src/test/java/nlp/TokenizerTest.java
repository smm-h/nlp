package nlp;

import javax.swing.*;

import vis.InFrame;
import web.HTMLView;

public class TokenizerTest {
    public static void main(String[] args) {

        // do the tokenizing
        Tokenizer tokenizer = new Splitter();
        String s1 = "مادر علی او را دعا میکند.";
        String s2 = tokenizer.tokenize(s1).toString();

        // visualize it
        HTMLView v = new HTMLView(s2);

        // JTextArea v = new JTextArea(s2);
        // v.setLineWrap(true);

        // show it
        JComponent c = v;
        InFrame.setFontSize(c, 40);
        InFrame.pad(c, 32);
        InFrame.setSize(c, 640, 480);
        InFrame.show(c);
    }
}
