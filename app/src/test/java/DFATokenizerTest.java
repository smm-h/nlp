
import automata.dfa.DFA.State;
import automata.dfa.DFA.ConfigurableTransition.Condition;
import nlp.dfatokenizer.DFATokenizer;

public abstract class DFATokenizerTest {

    // number of states
    final int n;
    DFATokenizer t;
    State<Character>[] s;

    @SuppressWarnings("unchecked")
    public DFATokenizerTest(int n) {
        this.n = n;

        t = new DFATokenizer();

        // create and populate an array of states
        s = new State[n + 1];
        for (int i = 1; i <= n; i++) {
            s[i] = t.addState();
        }
    }

    public abstract void test();

    void setStart(int id) {
        t.setStart(s[id]);
    }

    void setFinal(int id) {
        s[id].setFinal(true);
        s[id].setName(s[id].getName() + "_f");
    }

    void setFinal(int id, String name) {
        s[id].setFinal(true);
        s[id].setName(name);
    }

    void go(int from, int to, Condition<Character> on) {
        t.getTransition(s[from], s[to]).getConfigurable().setCondition(on);
    }

    void go(int from, int to, char on) {
        t.getTransition(s[from], s[to]).getConfigurable().setCondition((Character c) -> c == on);
    }
}
