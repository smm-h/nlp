import java.nio.file.Path;

import nlp.*;

public class NilicTokenizerTest {
    public static void main(String[] args) throws Exception {
        Tokenizer t = new NilicTokenizer(Path.of("C:/Users/SMM H/Desktop/nlp/tokenizer.nlx"), new NilicNormalizer());
        // Utilities.DEFAULT_TOKENIZER = t;

        String s = "['این یک مقالهٔ خرد کشور تونس است. می‌توانید با گسترش آن به ویکی‌پدیا کمک کنید.']";
        // String s = "هنگام تمرین برای رقص، مچ پای نیشا آسیب می‌بیند ودکتر می‌گوید که
        // برای چندماه نمی‌تواند برقصد و گروه به دنبال رقاص جدید است تا اینکه راهول پوجا
        // را می‌بیند و از او درخواست می‌کند تا به عنوان رقصنده در تیم آنها باشد…";

        System.out.println(t.tokenize(s));
    }

}
