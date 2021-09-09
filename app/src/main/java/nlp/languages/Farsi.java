package nlp.languages;

import nlp.NilicNormalizer;

public class Farsi extends BaseNaturalLanguage {
    public Farsi() {
        super("https://fa.wikipedia.org/wiki/%D9%88%DB%8C%DA%98%D9%87:%D8%B5%D9%81%D8%AD%D9%87%D9%94_%D8%AA%D8%B5%D8%A7%D8%AF%D9%81%DB%8C",
                new NilicNormalizer());
    }
}
