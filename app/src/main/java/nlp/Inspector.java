package nlp;

import util.ToString;
import web.html.TableMaker;

@SuppressWarnings({ "unchecked", "rawtypes" })
public interface Inspector {
    public String inspect(Tokenized tokenized);

    public static Inspector INSPECTOR_CONTENTS = (Tokenized t) -> t.getContents();

    public static Inspector INSPECTOR_TOKENS = (Tokenized t) -> t.toString() + "</pre><pre>";

    public static Inspector INSPECTOR_VOCABULARY = (Tokenized t) -> t.getVocabulary().toString();

    public static Inspector makeInspector(Corpus corpus, Lexicon lexicon) {
        return new Inspector() {
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
                    // if (tfidf > 1) {
                    // if (isInDict == false) {
                    if (isInDict == false && tfidf > 1) {
                        maker.add(token, tf, df, tfdf, tfidf, isNormal, isInDict);
                    }
                }

                // finish making the table and return it
                return maker.finish();
            }
        };
    }
}
