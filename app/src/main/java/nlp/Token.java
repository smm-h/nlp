package nlp;

public interface Token {

    public Term asTerm();

    /**
     * @return Raw
     */
    public String asString();

}
