package nlp;

import jile.common.LinkedTree;

public interface StringTree extends Lexicon {

    public void add(Token token);

    public void remove(Token token);

    public LinkedTree<Character> toLinkedTree();
}