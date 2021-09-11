package nlp.languages;

import nlp.NilicNormalizer;

public class English extends BaseNaturalLanguage {
    public English() {
        super("https://simple.wikipedia.org/wiki/Special:Random", new NilicNormalizer());
    }
}
