package distilled_slogo.parsing;

import java.util.List;

/**
 * The interface for a grammar rule, which determines how to nest a flat
 * list of symbols into a parse tree
 * 
 * @param <T> The type associated with the ISyntaxNodes returned by this
 *            grammar rule
 */
public interface IGrammarRule<T> {

    /**
     * Check if the rule matches any right subset of a list of tokens
     * 
     * @param tokens The tokens to match on
     * @return The index indicating the beginning of the match; -1 if no match
     */
    public int matches (List<ISyntaxNode<T>> tokens);

    /**
     * Check whether a list of symbols matches this rule
     * 
     * @param nodes The list of nodes to check
     * @return Whether the list matches
     */
    public boolean hasMatch(List<ISyntaxNode<T>> nodes);

    /**
     * Nest all or a rightmost subset of the nodes under a single node
     * 
     * @param nodes The nodes to reduce
     * @param factory The factory used to create additional nodes
     * @return The reduced nodes
     */
    public List<ISyntaxNode<T>> reduce (List<ISyntaxNode<T>> nodes, IOperationFactory<T> factory);
}
