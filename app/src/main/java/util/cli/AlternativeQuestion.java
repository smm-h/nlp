package util.cli;

import java.util.List;

public interface AlternativeQuestion extends Question<Integer> {

    public List<String> getOptions();

    public Runnable getEffect(int cause);

    public default void addOption(String option) {
        addOption(option, null);
    }

    public void addOption(String option, Runnable effect);

}