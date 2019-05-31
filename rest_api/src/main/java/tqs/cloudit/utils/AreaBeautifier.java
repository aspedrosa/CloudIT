package tqs.cloudit.utils;

/**
 * Util to beautify areas, so they obey the same format, title case
 */
public class AreaBeautifier {

    /**
     * With this variable the delimiter of where the words
     *  are split is parameterized.
     */
    private static final String DELIMITER = " ";

    /**
     * Turns a multi/uni-word area into a multi/uni-word
     *  area with title case
     *
     * @param area to beautify
     * @return Area titled case
     */
    public static String beautify(String area) {
        area = area.trim();

        String[] words = area.split(DELIMITER);

        String[] titled_words = new String[words.length];

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.length() == 1 || word.length() == 0) {
                continue;
            }

            if (Character.isLetter(word.charAt(1))) {
                titled_words[i] = Character.toUpperCase(word.charAt(0)) + word.substring(1);
            }
            else {
                titled_words[i] = word;
            }
        }

        return String.join(DELIMITER, titled_words);
    }
}
