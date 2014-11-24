package distilled_slogo.util;

/**
 * An error indicating that something was wrong a list of loaded token rules
 *
 */
public class InvalidRulesException extends Exception {
    private static final long serialVersionUID = 7501047164278077923L;

    public InvalidRulesException (String message) {
        super(message);
    }
}
