package distilled_slogo;

/**
 * Indicate that the syntax of the command was incorrect
 *
 */
public class MalformedSyntaxException extends Exception {

	/**
	 * Indicate a syntax error, including a helpful message
	 */
    public MalformedSyntaxException (String message) {
        super(message);
    }

}
