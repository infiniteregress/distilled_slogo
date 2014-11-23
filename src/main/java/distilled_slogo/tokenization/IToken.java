package distilled_slogo.tokenization;

/**
 * A representation of a token emitted by the tokenizer
 *
 */
public interface IToken {
    /**
     * Get the text associated with the token
     * 
     * @return The text of the token
     */
    public String text ();

    /**
     * Get the token's label, a.k.a. the "type" of token
     * 
     * @return The label
     */
    public String label ();
}
