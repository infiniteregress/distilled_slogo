package distilled_slogo.parsing;

import java.util.List;
import distilled_slogo.tokenization.IToken;

/**
 * A class which takes a list of tokens and returns a tree of ISyntaxNodes
 * containing a particular type of object to evaluate
 *
 * @param <T> The object to evaluate over
 */
public interface IParser<T> {

    /**
     * Return the parse tree of a list of tokens
     * 
     * @param tokens The tokens to evaluate
     * @return The root of the parse tree
     * @throws MalformedSyntaxException If no single root could be created,
     *                                  indicating an error with the list of
     *                                  tokens
     */
    public ISyntaxNode<T> parse (List<IToken> tokens) throws MalformedSyntaxException;

    /**
     * Load a list of rules to combine tokens together
     * 
     * @param rules The rules to load
     * @throws InvalidGrammarRuleException If the list of rules is invalid
     */
    public void loadGrammar (List<IGrammarRule<T>> rules) throws InvalidGrammarRuleException;
}
