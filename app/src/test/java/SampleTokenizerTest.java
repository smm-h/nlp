import java.nio.file.Path;

import nlp.*;
import util.ToString;
import web.html.HTMLDocument;
import web.html.HTMLUtilities;
import web.html.TableMaker;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SampleTokenizerTest {
    public static void main(String[] args) throws Exception {

        Utilities.DEFAULT_TOKENIZER = new Splitter();
        Utilities.DEFAULT_NORMALIZER = new BaseFarsiNormalizer();

        Lexicon lexicon = new StoredHashLexicon(Path.of("C:/Users/SMM H/Desktop/nlp/res/ctpr/spellcheck-dict.txt"));

        Corpus corpus = new HashCorpus();

        // Inspector inspector = (Tokenized t) -> t.getContents();
        // Inspector inspector = (Tokenized t) -> t.getVocabulary().toString();
        Inspector inspector = new Inspector() {
            @Override
            public String inspect(Tokenized tokenized) {
                // "<span style=\"color:red;\">"+...+"</span>"

                // start making the table
                TableMaker maker = new TableMaker("Token", "TF", "DF", "TF-DF", "TF-IDF", "Is normal?",
                        "Exists in dict?");

                // create string makers
                ToString ts_integer = ToString.getDoubleToString(0);
                ToString ts_3digits = ToString.getDoubleToString(3);
                ToString ts_yesOrNo = (ToString) ToString.getBooleanToString("Yes", "No");

                // assign string makers
                maker.setToString(1, ts_integer);
                maker.setToString(2, ts_integer);
                maker.setToString(3, ts_integer);
                maker.setToString(4, ts_3digits);
                maker.setToString(5, ts_yesOrNo);
                maker.setToString(6, ts_yesOrNo);

                // add the rows
                for (Token token : tokenized.getVocabulary()) {

                    // prepare data
                    Term term = token.asTerm();
                    double tf = tokenized.getTermFrequency(term);
                    double df = corpus.getDocumentFrequency(term);
                    double tfdf = tf * df;
                    double tfidf = tf / df;
                    boolean isNormal = token.isNormal();
                    boolean isInDict = lexicon.contains(token);

                    // filter and add
                    if (tfidf > 1) {
                        maker.add(token, tf, df, tfdf, tfidf, isNormal, isInDict);
                    }
                }

                // finish making the table and return it
                return maker.finish();
            }
        };

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

        for (int i = 0; i < z; i++) {
            report.addHeading("sample" + i, 2);
            report.addTag("p", documents[i].inspect(inspector));
        }

        HTMLUtilities.show(report);
    }
}