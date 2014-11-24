package distilled_slogo.tokenization;

import distilled_slogo.util.InvalidRulesException;

/**
 * An error indicating that something was wrong a list of loaded token rules
 *
 */
public class InvalidTokenRulesException extends InvalidRulesException {
    private static final long serialVersionUID = 2695436108557393474L;

    public InvalidTokenRulesException (String message) {
        super(message);
    }
}
