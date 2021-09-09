package nlp.languages;

import nlp.Normalizer;

public class BaseNaturalLanguage implements NaturalLanguage {

    private final String rwau;
    private final Normalizer normalizer;

    public BaseNaturalLanguage(String rwau, Normalizer normalizer) {
        this.rwau = rwau;
        this.normalizer = normalizer;
    }

    @Override
    public String getRandomWikipediaArticleURL() {
        return rwau;
    }

    @Override
    public Normalizer getDefaultNormalizer() {
        return normalizer;
    }

}
