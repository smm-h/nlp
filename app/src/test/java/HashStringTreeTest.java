import jile.vis.Viewer;
import nlp.*;

public class HashStringTreeTest {
    public static void main(String[] args) {

        StringTree tree = new HashStringTree();
        tree.add(new StringToken("this"));
        tree.add(new StringToken("the"));
        tree.add(new StringToken("bad"));
        tree.add(new StringToken("bord"));
        tree.add(new StringToken("thesis"));
        tree.add(new StringToken("those"));
        tree.add(new StringToken("bath"));

        Viewer.singleton().show(tree.toLinkedTree().visualize());
    }
}
