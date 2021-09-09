package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import jile.common.Convertor;
import util.cli.AlternativeQuestion;
import util.cli.OpenQuestion;
import util.cli.Question;
import util.cli.YesNoQuestion;

public class CLI {

    protected final Scanner scanner;

    public final Runnable DO_NOTHING = new Runnable() {
        @Override
        public void run() {
            // do nothing
        }
    };

    public final Runnable UNSUPPORTED = new Runnable() {
        @Override
        public void run() {
            print("Not supported yet.");
        };
    };

    public final Question<String> WHAT = makeOpenQuestion("Enter input:", (String s) -> s);

    public final Question<Integer> HOW_MANY = makeOpenQuestion("How many?", (String s) -> Integer.parseInt(s));

    public final Question<Double> HOW_MUCH = makeOpenQuestion("How much?", (String s) -> Double.parseDouble(s));

    private final Stack<AlternativeQuestion> stack;

    public CLI() {
        scanner = new Scanner(System.in);
        stack = new Stack<AlternativeQuestion>();
    }

    public void laterAsk(AlternativeQuestion q) {
        stack.push(q);
        if (stack.size() == 1) {
            start();
        }
    }

    public void start() {
        AlternativeQuestion q;
        while (!stack.isEmpty()) {
            q = stack.pop();
            Runnable effect = q.getEffect(q.ask());
            if (effect != null)
                effect.run();
        }
    }

    public Runnable goTo(AlternativeQuestion q) {
        return new Runnable() {
            @Override
            public void run() {
                laterAsk(q);
            }
        };
    }

    public void print(Object object) {
        System.out.println(object.toString());
    }

    public YesNoQuestion makeYesNoQuestion(String body) {
        return makeYesNoQuestion(body, false, null);
    }

    public YesNoQuestion makeYesNoQuestion(String body, boolean cancellable, Runnable onCancel) {
        AlternativeQuestion q = new AlternativeQuestionImplementation(body, cancellable, onCancel);
        q.addOption("Yes");
        q.addOption("No");
        return new YesNoQuestion() {
            @Override
            public AlternativeQuestion getAlternativeQuestion() {
                return q;
            }
        };
    }

    public AlternativeQuestion makeAlternativeQuestion(String body) {
        return makeAlternativeQuestion(body, false, null);
    }

    public AlternativeQuestion makeAlternativeQuestion(String body, boolean cancellable, Runnable onCancel) {
        return new AlternativeQuestionImplementation(body, cancellable, onCancel);
    }

    public <T> OpenQuestion<T> makeOpenQuestion(String body, Convertor<String, T> convertor) {
        return makeOpenQuestion(body, convertor, false, null);
    }

    public <T> OpenQuestion<T> makeOpenQuestion(String body, Convertor<String, T> convertor, boolean cancellable,
            Runnable onCancel) {
        return new OpenQuestionImplementation<T>(body, convertor, cancellable, onCancel);
    }

    // Based on this: https://en.wikipedia.org/wiki/Letter_frequency
    private static final char[] LETTERS_BY_FREQUENCY = "ZQJXKVBPGYFMWCULDRHSNIOATE".toCharArray();

    private static boolean isUppercaseAlphabetic(char c) {
        return c >= 65 && c <= 90;
    }

    private abstract class QuestionImplementation<T> implements Question<T> {

        private final String body;
        private final boolean cancellable;

        public QuestionImplementation(String body, boolean cancellable) {
            this.body = body;
            this.cancellable = cancellable;
        }

        @Override
        public String getBody() {
            return body;
        }

        @Override
        public boolean isCancellable() {
            return cancellable;
        }
    }

    private class AlternativeQuestionImplementation extends QuestionImplementation<Integer>
            implements AlternativeQuestion {

        private final Map<Integer, Runnable> effects;
        private final List<String> options;
        private final boolean[] symbols;
        private final Map<Character, Integer> keymap;

        public AlternativeQuestionImplementation(String body, boolean cancellable, Runnable onCancel) {
            super(body, cancellable);
            effects = new HashMap<Integer, Runnable>();
            options = new ArrayList<String>();
            symbols = new boolean[26];
            keymap = new HashMap<Character, Integer>();
            if (cancellable) {
                symbols[23] = true;
                keymap.put('X', 0);
                effects.put(0, onCancel);
                options.add("[X] Cancel");
            }
        }

        @Override
        public List<String> getOptions() {
            return options;
        }

        @Override
        public Integer ask() {
            print(getBody());
            int i = 0;
            for (String option : options) {
                print("> " + ++i + ": " + option);
            }
            while (true) {
                System.out.print("> ");
                String s = scanner.next();
                try {
                    i = Integer.parseInt(s);
                    if (i >= 1 && i <= options.size())
                        return i - 1;
                } catch (NumberFormatException nfe) {
                    if (s.length() == 1) {
                        char c = s.toUpperCase().charAt(0);
                        if (isUppercaseAlphabetic(c)) {
                            if (keymap.containsKey(c)) {
                                return keymap.get(c);
                            } else {
                                print("Unassigned key: " + c);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void addOption(String option, Runnable effect) {
            char symbol = 0;
            for (char c : option.toUpperCase().toCharArray()) {
                if (isUppercaseAlphabetic(c)) {
                    if (!symbols[c - 65]) {
                        symbol = c;
                        break;
                    }
                }
            }
            if (symbol == 0) {
                for (int i = 0; i < LETTERS_BY_FREQUENCY.length; i++) {
                    char L = LETTERS_BY_FREQUENCY[i];
                    if (!symbols[L - 65]) {
                        symbol = L;
                        break;
                    }
                }
            }
            int index = options.size();
            if (symbol != 0) {
                symbols[symbol - 65] = true;
                keymap.put(symbol, index);
                option = "[" + symbol + "] " + option;
            }
            if (effect != null)
                effects.put(index, effect);
            options.add(option);
        }

        @Override
        public Runnable getEffect(int cause) {
            return effects.get(cause);
        }
    }

    private class OpenQuestionImplementation<T> extends QuestionImplementation<T> implements OpenQuestion<T> {

        private final Convertor<String, T> convertor;
        private final Runnable onCancel;

        public OpenQuestionImplementation(String body, Convertor<String, T> convertor, boolean cancellable,
                Runnable onCancel) {
            super(body, cancellable);
            this.convertor = convertor;
            this.onCancel = onCancel;
        }

        @Override
        public T ask() {
            print(getBody());
            if (isCancellable())
                print("(DO NOT ENTER ANYTHING TO CANCEL)");
            System.out.print("> ");
            String s = scanner.next();
            if (isCancellable()) {
                if (s == "") {
                    if (onCancel != null) {
                        onCancel.run();
                    }
                    return null;
                }
            }
            return convertor.convert(s);
        }
    }
}
