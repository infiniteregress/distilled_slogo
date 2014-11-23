package distilled_slogo.parsing;

/**
 * An exception indicating that an invalid grammar rule has been created
 */
public class InvalidGrammarRuleException extends Exception {
    private static final long serialVersionUID = 5696559468097296057L;

    public InvalidGrammarRuleException (String message) {
        super(message);
    }
}
