package nlp;

public class NilicNormalizer extends BaseMultiCharacterNormalizer {
    public NilicNormalizer() {

        put("-", "-");
        put("–", "-");

        // digits
        put("\u0660", "\u06F0");
        put("\u0661", "\u06F1");
        put("\u0662", "\u06F2");
        put("\u0663", "\u06F3");
        put("\u0664", "\u06F4");
        put("\u0665", "\u06F5");
        put("\u0666", "\u06F6");
        put("\u0667", "\u06F7");
        put("\u0668", "\u06F8");
        put("\u0669", "\u06F9");

        // K and Y
        put("\u064A", "\u06CC");
        put("\u0643", "\u06A9");

        // Hatted A
        put("آ", "ا");

        // Hamze
        put("أ", "ا");
        put("ئ", "ی");
        put("ي", "ی");
        put("ة", "ه");
        put("ۀ", "ه");
        put("ؤ", "و");
        put("ؤ", "و");

        put("\u0654", "ء");
        put("\u002e", ".");

        // Half-spacess
        put("\u200b", " ");
        put("\u200d", " ");
        put("\u200e", " ");
        put("\u200c", " ");

        // Deletes
        put("ً", "");
        put("ٌ", "");
        put("ٍ", "");
        put("َ", "");
        put("ُ", "");
        put("ِ", "");
        put("ّ", "");
        put("\u0653", "");
        put("\u0655", "");
        put("\u0654", "");
        put("\u0674", "");
        put("\u06E4", "");
        put("ـ", "");

        // Some punctuations
        put("٪", "%");
        put("،", ",");
        put("٬", ",");
        put("٫", ",");
        put("٭", "*");
    }
}
