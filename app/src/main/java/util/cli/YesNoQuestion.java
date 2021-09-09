package util.cli;

public interface YesNoQuestion extends Question<Boolean> {

    /**
     * @return Effectless, two-option alternate question
     */
    public AlternativeQuestion getAlternativeQuestion();

    @Override
    default String getBody() {
        return getAlternativeQuestion().getBody();
    }

    @Override
    default Boolean ask() {
        return getAlternativeQuestion().ask() == 0;
    }

    @Override
    default boolean isCancellable() {
        return getAlternativeQuestion().isCancellable();
    }
}