
public class DFATokenizerSmallTest {

    public static void main(String[] args) {
        new DFATokenizerTest(5) {
            @Override
            public void test() {

                setStart(1);

                setFinal(3);
                setFinal(4);
                setFinal(5);

                go(1, 2, 'a');
                go(2, 2, 'a');
                go(2, 3, 'b');
                go(3, 3, 'a');
                go(1, 4, 'c');
                go(4, 4, 'd');
                go(4, 5, 'c');
                go(5, 5, 'a');

                System.out.println(t.tokenize("aabaa"));
            }
        }.test();
    }
}
