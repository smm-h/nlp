import java.io.IOException;
import java.nio.file.Path;

import nlp.Corpus;
import nlp.HashCorpus;
import nlp.Utilities;

public class SampleTokenizerTest {
    public static Corpus main(String[] args) throws IOException {
        Corpus corpus = new HashCorpus();
        for (int i = 0; i < 20; i++) {
            Path path = Path.of("C:/Users/SMM H/Desktop/nlp/res/ctpr/tokenization-samples/sample" + i);
            corpus.add(Utilities.getDocumentFromPath(path));
        }
        return corpus;
    }
}