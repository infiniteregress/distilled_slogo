package distilled_slogo.tokenization;

/**
 * A representation of a token emitted by the tokenizer
 *
 */
public interface IToken {
    /**
     * Get the text associated with the token
     * 
     * @return the text of the token
     */
    public String text ();

    /**
     * Get the type of token
     * 
     * @return the type
     */
    public String type ();
}
