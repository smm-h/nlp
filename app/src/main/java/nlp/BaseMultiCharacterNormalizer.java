package nlp;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BaseMultiCharacterNormalizer implements MultiCharacterNormalizer {

    private final Map<String, String> map;
    private final Map<Character, Set<String>> starters;

    public BaseMultiCharacterNormalizer() {
        map = new HashMap<String, String>();
        starters = new HashMap<Character, Set<String>>();
    }

    @Override
    public Iterable<String> getUnnormalPieces(char c) {
        return starters.get(c);
    }

    @Override
    public String normalizePiece(String unnormalPiece) {
        return map.get(unnormalPiece);
    }

    @Override
    public void put(String unnormal, String normal) {
        map.put(unnormal, normal);
        char c = unnormal.charAt(0);
        if (!starters.containsKey(c))
            starters.put(c, new TreeSet<String>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.compare(o2.length(), o1.length());
                }
            }));
        starters.get(c).add(unnormal);
    }

}
