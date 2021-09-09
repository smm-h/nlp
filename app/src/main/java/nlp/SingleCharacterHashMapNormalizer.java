package nlp;

import java.util.HashMap;

public class SingleCharacterHashMapNormalizer extends HashMap<Character, Character>
        implements SingleCharacterMapNormalizer {
    public void put(String chars, char c) {
        for (char x : chars.toCharArray()) {
            put(x, c);
        }
    }
}
