package nlp;

import java.util.HashMap;

public class HashMapNormalizer extends HashMap<Character, Character> implements MapNormalizer {
    public void put(String chars, char c) {
        for (char x : chars.toCharArray()) {
            put(x, c);
        }
    }
}
