package distilled_slogo.parsing;

import java.util.List;

/**
 * 
 *
 */
public interface IGrammarRule {

    /**
     * Check if the rule matches any right subset of a list of tokens
     * 
     * @param tokens
     *            The tokens to match on
     * @return The index indicating the beginning of the match; -1 if no match
     */
    public int matches (List<ISyntaxNode> tokens);

}
