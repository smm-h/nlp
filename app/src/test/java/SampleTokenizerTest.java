import java.nio.file.Path;

import nlp.*;
import util.ToString;
import web.html.HTMLDocument;
import web.html.HTMLUtilities;
import web.html.TableMaker;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SampleTokenizerTest {
    public static void main(String[] args) throws Exception {

        Lexicon lexicon = new HashLexicon();

        Tokenizer tokenizer = new Splitter();

        Corpus corpus = new HashCorpus();

        // Inspector inspector = (Tokenized t) -> t.getContents();
        // Inspector inspector = (Tokenized t) -> t.getVocabulary().toString();
        Inspector inspector = new Inspector() {
            @Override
            public String inspect(Tokenized tokenized) {
                // "<span style=\"color:red;\">"+...+"</span>"
                TableMaker maker = new TableMaker("Token", "TF", "DF", "TF-DF", "TF-IDF", "Exists in dict");
                ToString n0 = ToString.getDoubleToString(0);
                ToString n3 = ToString.getDoubleToString(3);
                maker.setToString(1, n0);
                maker.setToString(2, n0);
                maker.setToString(3, n0);
                maker.setToString(4, n3);
                maker.setToString(5, (ToString) ToString.getBooleanToString("Yes", "No"));
                for (Token token : tokenized.getVocabulary()) {
                    Term term = token.asTerm();
                    double tf = tokenized.getTermFrequency(term);
                    double df = corpus.getDocumentFrequency(term);
                    maker.add(token, tf, df, tf * df, tf / df, lexicon.contains(token));
                }
                return maker.finish();
            }
        };

        int z = 20;

        Document[] documents = new Document[z];

        HTMLDocument report = new HTMLDocument();

        report.add(HTMLUtilities.DEFAULT_STYLE);
        // report.addRawCSS("pre {white-space: pre-wrap;}");
        report.addHeading("Tokenization Samples", 1);

        for (int i = 0; i < 20; i++) {
            Path path = Path.of("C:/Users/SMM H/Desktop/nlp/res/ctpr/tokenization-samples/sample" + i);
            documents[i] = Utilities.getDocumentFromPath(path, tokenizer);
            corpus.add(documents[i]);
        }

        for (int i = 0; i < 20; i++) {
            report.addHeading("sample" + i, 2);
            report.addTag("p", documents[i].inspect(inspector));
        }

        HTMLUtilities.show(report);
    }
}