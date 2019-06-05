package tqs.cloudit.utils;

/**
 * Util to beautify areas, so they obey the same format, title case
 */
public class AreaBeautifier {

    private AreaBeautifier() {}

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

        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (word.length() == 0) {
                continue;
            }

            word = word.toLowerCase();

            if (word.length() == 1) {
                sb.append(word.toUpperCase());
            }
            else if (!Character.isLetter(word.charAt(0))) {
                sb.append(word);
            }
            else {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1));
            }

            sb.append(DELIMITER);
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
