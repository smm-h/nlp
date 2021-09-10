package util;

import java.util.Objects;

public interface ToString<T> {

    public String alternativeToString(T thing);

    @SuppressWarnings("rawtypes")
    public static final ToString DEFAULT = new ToString() {

        @Override
        public String alternativeToString(Object thing) {
            return Objects.toString(thing);
        }

    };

    public static ToString<Double> getDoubleToString(int i) {
        String formattingPattern = "%." + i + "f";
        return new ToString<Double>() {
            @Override
            public String alternativeToString(Double value) {
                return String.format(formattingPattern, value);
            }
        };
    }

    public static ToString<Boolean> getBooleanToString() {
        return getBooleanToString("Yes", "No");
    }

    public static ToString<Boolean> getBooleanToString(String whenTrue, String whenFalse) {
        return new ToString<Boolean>() {
            @Override
            public String alternativeToString(Boolean value) {
                return value ? whenTrue : whenFalse;
            }
        };
    }
}