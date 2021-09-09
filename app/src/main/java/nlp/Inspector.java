package nlp;

public interface Inspector {

    public String inspect(Tokenized tokenized);

    public static Inspector INSPECTOR_CONTENTS = (Tokenized t) -> t.getContents();

    public static Inspector INSPECTOR_TOKENS = (Tokenized t) -> t.toString() + "</pre><pre>";

    public static Inspector INSPECTOR_VOCABULARY = (Tokenized t) -> t.getVocabulary().toString();

}
