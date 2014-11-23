package distilled_slogo.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import distilled_slogo.Constants;
import distilled_slogo.tokenization.Token;

/**
 * A class which represents a parsing rule for a particular language                <br><br>
 * 
 * A GrammarRule will match a list of symbols based on a pattern, which
 * is itself a list of symbols and special wildcards, such as
 * Constants.INFINITE_MATCHING_LABEL.                                               <br><br>
 * 
 * For instance, the rule ["foo", "bar", Constants.INFINITE_MATCHING_LABEL]
 * will match ["asdf", "foo", "bar", "bar"].                                        <br><br>
 * 
 * As seen by the example, Constants.INFINITE_MATCHING_LABEL is analogous
 * to the '+' character in regular expressions.                                     <br><br>
 * 
 * Note also that matching will occur as long as a rightmost subset of the
 * list of symbols matches the pattern.                                             <br><br>
 * 
 * Once a GrammarRule has been matched, it will nest the specified list of
 * symbols based on what the parent and grandparent are.                            <br><br>
 * 
 * The meaning of parent is overloaded to mean either the index of an entry
 * in the pattern, or the name of an external name to be created. In any case,
 * the parent node will be popped from the list of symbols and will become
 * the parent of the remaining symbols.                                             <br><br>
 * 
 * The parent will then become the child of the grandparent, if the grandparent
 * is specified. Note that the grandparent shares the same semantics as the
 * parent, i.e. it is either an external name or an index.                          <br><br>
 *
 * For example, with the pattern set to ["foo", "bar"], the parent set to "0",
 * and the grandparent set to "result", the result will be a tree which looks
 * like:                                                                            <br>
 * 
 * <pre>
 * result
 *    |
 *   foo
 *    |
 *   bar
 * <pre>
 */
public class GrammarRule implements IGrammarRule<String> {   
    /**
     * SILLY GOTCHA: the format is NOT listed per pattern, but rather
     * per individual symbol--
     * 
     * A single pattern ["hi", "there"] will generate
     * 
     * [["hi"],["there"]]
     */
    List<List<String>> pattern;
    String parent;
    String grandparent;

    /**
     * Create a new grammar rule with the specified pattern and parent,
     * but without a grandparent
     * 
     * @param pattern The pattern associated with the rule
     * @param parent The parent node to be used when applying this rule
     * @throws InvalidGrammarRuleException If parent is empty
     */
    public GrammarRule (String[] pattern, String parent)
            throws InvalidGrammarRuleException{
        this(pattern, parent, "");
    }

    /**
     * Create a new grammar rule with the specified pattern and parent,
     * but without a grandparent
     * 
     * @param pattern The pattern associated with the rule
     * @param parent The parent node to be used when applying this rule
     * @throws InvalidGrammarRuleException If parent is empty
     */
    public GrammarRule (List<String> pattern, String parent)
            throws InvalidGrammarRuleException{
        this(pattern, parent, "");
    }

    /**
     * Create a new grammar rule with the specified pattern, parent,
     * and grandparent
     * 
     * @param pattern The pattern associated with the rule
     * @param parent The parent node to be used when applying this rule
     * @param grandparent The parent of the parent node to be used when
     *                    applying this rule
     * @throws InvalidGrammarRuleException If the parent is empty
     */
    public GrammarRule (String[] pattern, String parent, String grandparent)
            throws InvalidGrammarRuleException{
        this(Arrays.asList(pattern), parent, grandparent);
    }
    
    /**
     * Create a new grammar rule with the specified pattern, parent,
     * and grandparent
     * 
     * @param pattern The pattern associated with the rule
     * @param parent The parent node to be used when applying this rule
     * @param grandparent The parent of the parent node to be used when
     *                    applying this rule
     * @throws InvalidGrammarRuleException If the parent is empty
     */
    public GrammarRule (List<String> pattern, String parent, String grandparent)
            throws InvalidGrammarRuleException {
        if (parent.length() == 0) {
            throw new InvalidGrammarRuleException("The parent specified is empty");
        }
        this.pattern = new ArrayList<>();
        for (String symbol: pattern) {
            List<String> patternEntry = new ArrayList<>();
            patternEntry.add(symbol);
            this.pattern.add(patternEntry);
        }
        this.parent = parent;
        this.grandparent = grandparent;
    }

    /**
     * Check whether a list of nodes matches this grammar rule's pattern.
     * 
     * Note that matching occurs based on the token's label, not its text,
     * to allow for rules to be abstract
     * 
     * @param nodes The list of symbols to match on
     * @return -1 if no match, the index (offset) of where the list matches
     *         otherwise
     */
    @Override
    public int matches (List<ISyntaxNode<String>> nodes) {
        List<List<String>> searchPattern = new ArrayList<>(pattern);

        List<String> toSearch = new ArrayList<>();
        for (ISyntaxNode<String> node : nodes) {
            toSearch.add(node.token().label());
        }

        for (int i = 0; i < toSearch.size(); i++) {
            if (matches(searchPattern, toSearch, i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check whether a search pattern matches a list of symbols at a certain
     * offset
     * 
     * @param searchPattern The pattern to search for
     * @param toSearch What to search on
     * @param index The index (offset) of where to search from in the list of
     *              symbols
     * @return Whether a match was found
     */
    private boolean matches (List<List<String>> searchPattern, List<String> toSearch,
            int index) {
        return infiniteMatchRecurse(searchPattern,
                                    toSearch.subList(index, toSearch.size()),
                                    Constants.INFINITE_MATCHING_LABEL);
    }

    /**
     * Check whether a search pattern, potentially including wildcards, matches
     * using a horribly baroque recursive algorithm. See the comments below for
     * the gory details.
     * 
     * @param searchPattern The pattern to search for
     * @param searchRemaining The rest of the list of symbols to search on
     * @param infiniteWildcard The string representing a sequence of one or
     *                         more of the previous element
     * @return Whether a match was found
     */
    boolean infiniteMatchRecurse (List<List<String>> searchPattern,
            List<String> searchRemaining, String infiniteWildcard) {
        if (searchRemaining.size() == 0) {
            if (searchPattern.size() == 0
                    || (searchPattern.size() == 2 && searchPattern.get(1).contains(
                            infiniteWildcard))) {
                return true;
            }
            return false;
        }
        List<List<String>> newPattern;
        List<String> newSearch;
        if (isInfinite(searchPattern)) {
            // if we're at the wildcard and both leading elements match, we
            // keep the pattern (so that we can continue wildcard matching),
            // but we iterate to the next search element
            if (searchPattern.get(0).contains(searchRemaining.get(0))) {
                newPattern = searchPattern;
                newSearch = searchRemaining.subList(1, searchRemaining.size());
                return true && infiniteMatchRecurse(newPattern, newSearch, infiniteWildcard);
            }
            // if we encounter the element right after the wildcard element,
            // then
            // we have finished matching everything for the wildcard, so we
            // should
            // set the pattern to the element after the wildcard and handle it
            // normally using non-infinite logic; searchRemaining stays the same
            // so that the next time around, the two leading elements of each
            // list
            // can be matched and iterated over in unison
            if (searchPattern.size() > 2 && searchPattern.get(2).contains(searchRemaining.get(0))) {
                newPattern = searchPattern.subList(2, searchPattern.size());
                newSearch = searchRemaining;
                return true && infiniteMatchRecurse(newPattern, newSearch, infiniteWildcard);
            }
        }
        // if we're not matching a wildcard, we iterate one at a time over both
        // lists checking to see that both leading elements are equal
        else if (searchPattern.get(0).contains(searchRemaining.get(0))) {
            newPattern = searchPattern.subList(1, searchPattern.size());
            newSearch = searchRemaining.subList(1, searchRemaining.size());
            return true && infiniteMatchRecurse(newPattern, newSearch, infiniteWildcard);
        }
        return false;
    }

    /**
     * To check for infinite, if any of the possible matches is the infinite
     * wildcard, then everything else will be ignored; i.e. don't include
     * anything else if you include the infinite matching wildcard
     * 
     * @param searchPattern
     *            The remaining pattern to search
     * @return Whether the second element contains the infinite matching
     *         wildcard
     */
    private boolean isInfinite (List<List<String>> searchPattern) {
        if (searchPattern.size() > 1
                && searchPattern.get(1).contains(Constants.INFINITE_MATCHING_LABEL)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasMatch(List<ISyntaxNode<String>> nodes) {
        return matches(nodes) == -1 ? false: true;
    }

    @Override
    public List<ISyntaxNode<String>> reduce (List<ISyntaxNode<String>> nodes) {
        int matchLocation = matches(nodes);
        if (matchLocation == -1) {
            return nodes;
        }
        List<ISyntaxNode<String>> reduced = reduce(nodes, matchLocation);
        return reduced;
    }

    /**
     * Create a nested list of symbols from a flat list
     * 
     * @param nodes The nodes to nest
     * @param index The starting index where the nesting will occur
     * @return The nested list
     */
    private List<ISyntaxNode<String>> reduce (List<ISyntaxNode<String>> nodes, int index) {
        List<ISyntaxNode<String>> newNodes = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            newNodes.add(nodes.get(i));
        }
        List<ISyntaxNode<String>> reducedNodes = new ArrayList<>(nodes.subList(index, nodes.size()));
        reducedNodes = createNestedNodes(reducedNodes);
        newNodes.addAll(reducedNodes);
        return newNodes;
    }

    /**
     * Perform the nesting on a list of symbols, nesting the parent on top
     * of the rest of the symbols and potentially nesting the grandparent on
     * top of the parent, if the grandparent is not empty
     * 
     * @param nodes The list of nodes to nest
     * @return The nested list
     */
    private List<ISyntaxNode<String>> createNestedNodes (List<ISyntaxNode<String>> nodes) {
        List<ISyntaxNode<String>> newNodes = new ArrayList<>(nodes);
        newNodes = createNestedNodes(newNodes, parent);
        if (grandparent.length() != 0){
            newNodes = createNestedNodes(newNodes, grandparent);
        }
        return newNodes;
    }

    /**
     * Decide whether the parent or grandparent is an external name or an
     * internal index, and continue delegating the nesting task further
     * down
     * 
     * @param nodes The list of nodes to nest
     * @param parent The parent or grandparent to nest
     * @return The nested list
     */
    private List<ISyntaxNode<String>> createNestedNodes
    (List<ISyntaxNode<String>> nodes, String parent) {
        int index = -1;
        try {
            index = Integer.parseInt(parent);
        } catch (NumberFormatException e){
        }
        boolean internal = isValidPatternIndex(index);
        if (internal) {
            return nestNodes(nodes, index);
        }
        else {
            return nestNodes(nodes, parent);
        }
    }
    
    /**
     * Determine whether an index is in range for the pattern list
     * 
     * @param index The potential index
     * @return Whether the index is in range of the pattern list
     */
    private boolean isValidPatternIndex(int index) {
        if (index < 0 || index >= pattern.size()){
            return false;
        }
        return true;
    }

    /**
     * Nest nodes using an internal index
     * 
     * @param nodes The nodes to nest
     * @param parentIndex The index of the parent
     * @return The nested list, with a single parent entry and the rest of the nodes
     *         as children of the parent
     */
    private List<ISyntaxNode<String>> nestNodes(List<ISyntaxNode<String>> nodes, int parentIndex){
        List<ISyntaxNode<String>> newNodes = new ArrayList<>();
        
        List<ISyntaxNode<String>> children = new ArrayList<>(nodes);
        ISyntaxNode<String> parent = children.get(parentIndex);
        children.remove(parentIndex);
        parent.setChildren(children);
        newNodes.add(parent);
        return newNodes;
    }

    /**
     * Nest nodes using an external name
     * 
     * @param nodes The nodes to nest
     * @param externalParent The name of the external parent node to create
     * @return The nested list, with a single parent entry and the rest of the nodes
     *         as children of the parent
     */
    private List<ISyntaxNode<String>> nestNodes(List<ISyntaxNode<String>> nodes, String externalParent){
        List<ISyntaxNode<String>> newNodes = new ArrayList<>();
        ISyntaxNode<String> parent =
                new SyntaxNode<String>(
                        new Token("", externalParent),
                        "",
                        new ArrayList<>(nodes));
        newNodes.add(parent);
        return newNodes;
    }

    @Override
    public String toString () {
        return pattern.toString();
    }
}
