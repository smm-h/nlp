package nlp;

public interface MultiCharacterNormalizer extends Normalizer {

    public void put(String unnormal, String normal);

    public Iterable<String> getUnnormalPieces(char c);

    public String normalizePiece(String unnormalPiece);

    @Override
    public default String normalize(String original) {
        final int n = original.length();
        if (n == 0)
            return original;
        int backward = 0;
        int forward = 0;
        StringBuilder normalized = new StringBuilder(n + 32);
        while (forward < n) {
            Iterable<String> u = getUnnormalPieces(original.charAt(forward));
            if (u != null) {
                for (String s : u) {
                    int k = s.length();
                    if (forward + k <= n) {
                        if (original.substring(forward, forward + k).equals(s)) {
                            if (forward > backward) {
                                normalized.append(original.substring(backward, forward));
                            }
                            forward += k;
                            backward = forward;
                            normalized.append(normalizePiece(s));
                            continue;
                        }
                    }
                }
            }
            forward++;
        }
        if (forward > backward) {
            normalized.append(original.substring(backward));
        }
        return normalized.toString();
    }

}
