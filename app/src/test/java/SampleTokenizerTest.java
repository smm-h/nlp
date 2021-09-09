import java.nio.file.Path;

import nlp.BaseFarsiNormalizer;
import nlp.Corpus;
import nlp.Document;
import nlp.HashCorpus;
import nlp.Inspector;
import nlp.Lexicon;
import nlp.Splitter;
import nlp.StoredHashLexicon;
import nlp.Utilities;
import web.html.HTMLDocument;
import web.html.HTMLUtilities;

public class SampleTokenizerTest {
    public static void main(String[] args) throws Exception {

        Utilities.DEFAULT_TOKENIZER = new Splitter();
        Utilities.DEFAULT_NORMALIZER = new BaseFarsiNormalizer();

        Lexicon lexicon = new StoredHashLexicon(Path.of("C:/Users/SMM H/Desktop/nlp/res/ctpr/spellcheck-dict.txt"));

        Corpus corpus = new HashCorpus();

        int z = 20;

        Document[] documents = new Document[z];

        HTMLDocument report = new HTMLDocument();

        report.add(HTMLUtilities.DEFAULT_STYLE);
        // report.addRawCSS("pre {white-space: pre-wrap;}");
        report.addHeading("Tokenization Samples", 1);

        for (int i = 0; i < z; i++) {
            Path path = Path.of("C:/Users/SMM H/Desktop/nlp/res/ctpr/tokenization-samples/sample" + i);
            documents[i] = Utilities.getDocumentFromPath(path);
            corpus.add(documents[i]);
        }

        Inspector inspector = Inspector.makeInspector(corpus, lexicon);

        for (int i = 0; i < z; i++) {
            report.addHeading("sample" + i, 2);
            report.addTag("p", documents[i].inspect(inspector));
        }

        HTMLUtilities.show(report);
    }
}