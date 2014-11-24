package distilled_slogo.tokenization;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Something which converts a stream of characters to a list of tokens
 * 
 */
public interface ITokenizer {
    /**
     * Convert a stream into a list of characters
     * 
     * @param input An input stream of characters
     * @return A list of tokens, in order
     * @throws IOException If an error occurs with the input stream
     */
    public List<IToken> tokenize (Reader input) throws IOException;

    /**
     * Load rules for recognizing tokens
     * 
     * @param rules A list of rules
     * @throws InvalidTokenRulesException If the rules are null
     */
    public void loadTokenRules (List<ITokenRule> rules) throws InvalidTokenRulesException;
}
