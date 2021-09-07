package nlp;

import java.util.Map;

public interface MapNormalizer extends Map<Character, Character>, SingleCharacterNormalizer {

    @Override
    public default char normalize(char c) {
        return containsKey(c) ? get(c) : c;
    }

}
