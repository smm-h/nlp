package vis;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

public class InFrame {
    /**
     * Create padding by adding an empty border.
     */
    public static void pad(JComponent c, int w) {
        c.setBorder(new EmptyBorder(w, w, w, w));
    }

    public static void show(JComponent c) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(c);
        f.pack();
        f.setVisible(true);
    }

    public static void setSize(JComponent c, int w, int h) {
        c.setPreferredSize(new Dimension(w, h));
    }

    public static void setFontSize(JComponent c, int size) {
        c.setFont(c.getFont().deriveFont((float) size));
    }
}
