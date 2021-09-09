package nlp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jile.common.Resource;
import jile.lingu.Code;
import jile.lingu.DefaultTokenizer;
import jile.lingu.Language;
import jile.lingu.TokenizerMaker;
import jile.lingu.IndividualTokenType.IndividualToken;

public class NilicTokenizer implements Tokenizer {

    private final DefaultTokenizer nt;
    private final Normalizer preprocessor;

    private static final String EXT = "nlpt";

    public NilicTokenizer(Path path) throws IOException {
        this(path, null);
    }

    public NilicTokenizer(Path path, Normalizer preprocessor) throws IOException {
        nt = TokenizerMaker.singleton().maker.make(new Code(Files.readString(path), TokenizerMaker.singleton()));
        this.preprocessor = preprocessor;
        new Language("Nilic Tokenization used for NLP", EXT, nt) {
        };
    }

    @Override
    public Tokenized tokenize(String string) {
        if (preprocessor != null)
            string = preprocessor.normalize(string);
        Tokenized t = new ArrayTokenized();
        Code code = new Code(Resource.of(string, EXT));
        for (IndividualToken token : jile.lingu.Tokenizer.tokenized.read(code)) {
            t.add(new NilicToken(token));
        }
        return t;
    }
}
