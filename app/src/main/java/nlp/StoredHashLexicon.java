package nlp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StoredHashLexicon extends HashLexicon {
    public StoredHashLexicon(Path path) throws IOException {
        for (String line : Files.readAllLines(path)) {
            add(new StringToken(line));
        }
    }
}
