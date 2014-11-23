package distilled_slogo.tokenization;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import distilled_slogo.MalformedSyntaxException;

/**
 * Something which converts a stream of characters to a list of tokens
 * 
 */
public interface ITokenizer {
    /**
     * Convert a stream into a list of characters
     * 
     * @param input
     *            an input stream of characters
     * @return a list of tokens, in order
     * @throws IOException
     * @throws MalformedSyntaxException
     */
    public List<IToken> tokenize (Reader input) throws IOException, MalformedSyntaxException;

    /**
     * Load rules for recognizing tokens
     * 
     * @param rules
     *            a list of rules
     * @throws InvalidTokenRulesException
     */
    public void loadTokenRules (List<ITokenRule> rules) throws InvalidTokenRulesException;
}
