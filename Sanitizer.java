package plu.bikecoop;

/**
 * Cleans strings to avoid SQL injection attacks.
 * @author Egan Dunning
 *
 */
public class Sanitizer {

	/**
	 * Remove characters that could be used to form sql queries
	 * @param input
	 * @return string that can safely be inserted into SQL statement
	 */
	public static String cleanSql(String input) {
		input = input.replace("'", "");
		input = input.replace("(", "");
		input = input.replace(")", "");
		input = input.replace(";", "");
		return input;
	}
}
