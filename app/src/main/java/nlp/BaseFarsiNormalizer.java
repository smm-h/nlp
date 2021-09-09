package nlp;

public class BaseFarsiNormalizer extends SingleCharacterHashMapNormalizer {

    public BaseFarsiNormalizer() {

        // https://en.wikipedia.org/wiki/Persian_alphabet#Deviations_from_the_Arabic_script

        // these 12 normalizations account for the 10 digits, ی, and ک, in that order

        put('\u0660', '\u06F0');
        put('\u0661', '\u06F1');
        put('\u0662', '\u06F2');
        put('\u0663', '\u06F3');
        put('\u0664', '\u06F4');
        put('\u0665', '\u06F5');
        put('\u0666', '\u06F6');
        put('\u0667', '\u06F7');
        put('\u0668', '\u06F8');
        put('\u0669', '\u06F9');
        put('\u064A', '\u06CC');
        put('\u0643', '\u06A9');
    }
}
