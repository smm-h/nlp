package automata.dfa;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DFA<T> {

    private static int idCounter = 0;

    private State<T> start;
    private final Map<Integer, Transition<T>> transitions;
    private final Map<State<T>, Set<Transition<T>>> transitionsOf;

    public DFA() {
        transitions = new HashMap<Integer, Transition<T>>();
        transitionsOf = new HashMap<State<T>, Set<Transition<T>>>();
    }

    public State<T> process(T[] input) throws UnknownSymbolException, AmbiguousSymbolException {
        State<T> s = start;
        int routeCount;
        for (int i = 0; i < input.length; i++) {
            routeCount = 0;
            T symbol = input[i];
            for (Transition<T> t : transitionsOf.get(s)) {
                if (t.checkCondition(symbol)) {
                    routeCount++;
                    if (routeCount > 1) {
                        throw new AmbiguousSymbolException(s, symbol);
                    } else {
                        s = t.getDestination();
                    }
                }
            }
            if (routeCount == 0) {
                throw new UnknownSymbolException(s, symbol);
            }
        }
        return s;
    }

    public State<T> process(State<T> s, T symbol) throws UnknownSymbolException, AmbiguousSymbolException {
        int routeCount;
        routeCount = 0;
        for (Transition<T> t : transitionsOf.get(s)) {
            if (t.checkCondition(symbol)) {
                routeCount++;
                if (routeCount > 1) {
                    throw new AmbiguousSymbolException(s, symbol);
                } else {
                    s = t.getDestination();
                }
            }
        }
        if (routeCount == 0) {
            throw new UnknownSymbolException(s, symbol);
        }
        return s;
    }

    public State<T> addState() {
        return new IntegerState(++idCounter);
    }

    public void setStart(State<T> start) {
        this.start = start;
    }

    public State<T> getStart() {
        return start;
    }

    public Transition<T> getTransition(State<T> from, State<T> to) {
        int h = hashTransition(from, to);
        if (transitions.containsKey(h)) {
            return transitions.get(h);
        } else {
            Transition<T> t = new BasicTransition(from, to);
            transitions.put(h, t);
            if (!transitionsOf.containsKey(from)) {
                transitionsOf.put(from, new HashSet<Transition<T>>());
            }
            transitionsOf.get(from).add(t);
            return t;
        }
    }

    public interface State<T> {
        public void setFinal(boolean isFinal);

        public boolean isFinal();

        public void setName(String name);

        public Transition<T> getTransitionTo(State<T> destination);
    }

    public interface Transition<T> {
        public State<T> getSource();

        public State<T> getDestination();

        public boolean checkCondition(T input);

        public default ConfigurableTransition<T> getConfigurable() {
            if (this instanceof ConfigurableTransition) {
                return (ConfigurableTransition<T>) this;
            } else {
                throw new ClassCastException("transition not configurable");
            }
        }
    }

    public interface ConfigurableTransition<T> extends Transition<T> {

        public void setCondition(Condition<T> c);

        public interface Condition<T> {
            public boolean check(T input);
        }
    }

    private class IntegerState implements State<T> {
        private final int id;
        private boolean isFinal = false;
        private String name;

        public IntegerState(int id) {
            this.id = id;
            name = "S" + id;
        }

        @Override
        public void setFinal(boolean isFinal) {
            this.isFinal = isFinal;
        }

        @Override
        public boolean isFinal() {
            return isFinal;
        }

        @Override
        public Transition<T> getTransitionTo(State<T> destination) {
            return getTransition(this, destination);
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class BasicTransition implements ConfigurableTransition<T> {

        private final State<T> src, dst;
        private Condition<T> c;

        public BasicTransition(State<T> src, State<T> dst) {
            this(src, dst, null);
        }

        public BasicTransition(State<T> src, State<T> dst, Condition<T> c) {
            this.src = src;
            this.dst = dst;
            this.c = c;
        }

        @Override
        public State<T> getSource() {
            return src;
        }

        @Override
        public State<T> getDestination() {
            return dst;
        }

        @Override
        public void setCondition(Condition<T> c) {
            this.c = c;
        }

        @Override
        public boolean checkCondition(T input) {
            return c.check(input);
        }

        @Override
        public int hashCode() {
            return hashTransition(src, dst);
        }
    }

    private static <T> int hashTransition(State<T> src, State<T> dst) {
        return src.hashCode() << 10 + dst.hashCode();
    }
}
