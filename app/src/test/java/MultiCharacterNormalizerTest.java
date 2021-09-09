import nlp.BaseMultiCharacterNormalizer;
import nlp.MultiCharacterNormalizer;
import util.CLI;

public class MultiCharacterNormalizerTest extends CLI {
    public static void main(String[] args) {
        new MultiCharacterNormalizerTest();
    }

    public MultiCharacterNormalizerTest() {

        MultiCharacterNormalizer n = new BaseMultiCharacterNormalizer();
        n.put("->", "→");
        n.put("-->", "⟶");
        n.put("<->", "⟷");
        n.put("...>", "⭬");

        while (true)
            print(n.normalize(WHAT.ask()));

    }
}
