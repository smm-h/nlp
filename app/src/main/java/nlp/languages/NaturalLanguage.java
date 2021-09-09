package nlp.languages;

import nlp.Normalizer;

public interface NaturalLanguage {

    public String getRandomWikipediaArticleURL();

    public Normalizer getDefaultNormalizer();
}
