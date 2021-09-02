
import java.io.IOException;

import nl.NaturalLanguage;
import nl.languages.Farsi;
import vis.InFrame;
import web.html.HTMLView;
import web.wikipedia.DocumentGenerator;
import web.wikipedia.RandomDocumentGenerator;

public class DocumentGeneratorTest {
    public static void main(String[] args) {

        NaturalLanguage farsi = new Farsi();
        DocumentGenerator r = new RandomDocumentGenerator(farsi);
        try {
            InFrame.show(new HTMLView(r.generate().toHTML()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
