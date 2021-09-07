package nlp;

import java.util.HashMap;

import jile.common.LinkedTree;

public class HashStringTree implements StringTree {

    private final Node root;
    private Node pointer;

    public HashStringTree() {
        root = new Node(Utilities.BOT);
        pointer = root;
    }

    @Override
    public boolean contains(Token token) {
        pointer = root;
        for (char c : token.getUnnormalized().toCharArray()) {
            if (!goToCharacter(c))
                return false;
        }
        return goToCharacter(Utilities.EOT);
    }

    public boolean goToCharacter(char c) {
        if (pointer.containsKey(c)) {
            pointer = pointer.get(c);
            return true;
        } else {
            return false;
        }
    }

    public void goToRoot() {
        pointer = root;
    }

    @Override
    public void add(Token token) {
        pointer = root;
        for (char c : token.normalize().toCharArray()) {
            addAndGoTo(c);
        }
        addAndGoTo(Utilities.EOT);
    }

    public void addAndGoTo(char c) {
        if (pointer.containsKey(c)) {
            pointer = pointer.get(c);
        } else {
            Node node = new Node(c);
            pointer.put(c, node);
            pointer = node;
        }
    }

    @Override
    public void remove(Token token) {
        throw new UnsupportedOperationException("removing is not supported yet");
    }

    @Override
    public LinkedTree<Character> toLinkedTree() {
        LinkedTree<Character> tree = new LinkedTree<Character>();
        root.populate(tree);
        return tree;
    }

    private class Node extends HashMap<Character, Node> {
        final char data;

        Node(char data) {
            this.data = data;
        }

        void populate(LinkedTree<Character> tree) {
            tree.addAndGoTo(data);
            for (char c : keySet()) {
                get(c).populate(tree);
            }
            tree.goBack();
        }
    }
}
