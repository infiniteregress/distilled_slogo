package distilled_slogo.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import distilled_slogo.Constants;
import distilled_slogo.tokenization.Token;

/**
 * A class which represents a parsing rule for a particular language                <br><br>
 * 
 * A GrammarRule will match a list of symbols based on a pattern, which
 * is itself a list of symbols                                                      <br><br>
 * 
 * For instance, the rule:                                                          <br><br>
 * 
 * [{"label": "foo", "level": 1},
 *  {"label": "bar", "level": 0, "repeating": true}                                 <br><br>
 * 
 * will match the token list ["asdf", "foo", "bar", "bar"].                         <br><br>
 * 
 * As seen by the example, the "repeating" attribute is analogous to the "+"
 * character in regular expressions.                                                <br><br>
 * 
 * Note also that matching will occur as long as a rightmost subset of the
 * list of symbols matches the pattern.                                             <br><br>
 * 
 * Once a GrammarRule has been matched, it will nest the specified list of
 * symbols based on the "level" specified for each symbol in the rule definition.   <br><br>
 * 
 * The level of a symbol indicates its depth in the generated subtree; symbols
 * at a specific level will become the parent of the level below, i.e. the
 * level one less than the current level. The special level -1 means to discard
 * the symbol during the generation of the parse tree.                              <br><br>
 * 
 * Additional new nodes can be generated at a certain level during application of
 * the rule using the "additional" list. To emulate a grammar, you would set the
 * level of each symbol in the pattern to 0 and specify a single additional node
 * of level 1.                                                                      <br><br>
 *
 * For example, with the pattern set to:                                            <br><br>
 * 
 * [{"label": "foo", "level": 1},
 *  {"label": "bar", "level": 0}]                                                   <br><br>
 *  
 * and the additional section set to:                                               <br><br>
 * 
 * [{"label": result", "level": 2}]                                                 <br><br>
 * 
 * the result will be a tree which looks like:                                      <br><br>
 * 
 * result    <br>
 * |         <br>
 * foo       <br>
 * |         <br>
 * bar       <br>
 * 
 * @param <T> The type associated with the ISyntaxNodes returned by this
 *            grammar rule
 */
public class GrammarRule<T> implements IGrammarRule<T> {   
    List<SymbolParsingRule> pattern;
    List<SymbolParsingRule> additional;

    /**
     * Create a new grammar rule with the specified pattern and no additional
     * nodes.
     * 
     * @param pattern The pattern associated with the rule
     * @throws InvalidGrammarRuleException If an error with the rule definition
     *                                     was found
     */
    public GrammarRule (List<SymbolParsingRule> pattern)
            throws InvalidGrammarRuleException{
        this(pattern, new ArrayList<>());
    }

    /**
     * Create a new grammar rule with the specified pattern and additional
     * nodes.
     * 
     * Currently, there can be no more than one symbol per level greater than
     * 0; i.e., there is no "multiple inheritance" in creating the tree.
     * 
     * @param pattern The pattern associated with the rule
     * @param additional The additional nodes to create when applying this rule
     * @throws InvalidGrammarRuleException If an error with the rule definition
     *                                     was found
     */
    public GrammarRule (List<SymbolParsingRule> pattern,
                        List<SymbolParsingRule> additional)
            throws InvalidGrammarRuleException {
        List<SymbolParsingRule> allRules = new ArrayList<>(pattern);
        allRules.addAll(additional);
        validateRules(allRules);
        this.pattern = new ArrayList<>(pattern);
        this.additional = new ArrayList<>(additional);
    }

    /**
     * Validate a list of rules. The rules must contain levels which are
     * consecutive and which start at 0 or -1. There can be no more than
     * one symbol at a level greater than 0.
     * 
     * @param rules The rules to validate
     * @throws InvalidGrammarRuleException If there is more than one symbol at
     *                                     a level greater than 0
     * @throws InvalidGrammarRuleException If no symbols were found
     * @throws InvalidGrammarRuleException If no symbols of level -1 or 0 were
     *                                     found
     * @throws InvalidGrammarRuleException The symbols specify levels that are
     *                                     not consecutive
     */
    public void validateRules(List<SymbolParsingRule> rules)
            throws InvalidGrammarRuleException{
        SortedSet<Integer> levels = new TreeSet<>();
        for (SymbolParsingRule rule: rules) {
            if (levels.contains(rule.level()) && rule.level() > 0){
                    throw new InvalidGrammarRuleException(rule
                        + ": cannot have more than one symbol at level "
                        + rule.level());
            }
            else {
                levels.add(rule.level());
            }
        }
        if (levels.size() == 0) {
            throw new InvalidGrammarRuleException("No symbols found");
        }
        if (!(levels.first().equals(-1) || levels.first().equals(0))){
            throw new InvalidGrammarRuleException(
                "no symbol of level -1 or 0 found");
        }
        Integer last = levels.first() - 1;
        for (Integer level: levels) {
            int expected = last + 1;
            if (!level.equals(expected)) {
                throw new InvalidGrammarRuleException(
                    "No rule found at level " + expected);
            }
            last = expected;
        }
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
    public int matches (List<ISyntaxNode<T>> nodes) {
        List<SymbolParsingRule> searchPattern =
                Collections.unmodifiableList(pattern);

        List<String> toSearch = new ArrayList<>();
        for (ISyntaxNode<T> node : nodes) {
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
    private boolean matches (List<SymbolParsingRule> searchPattern,
                             List<String> toSearch, int index) {
        return infiniteMatchRecurse(0, index, toSearch);
    }

    /**
     * Check whether a search pattern, matches using a not so horribly baroque
     * anymore recursive algorithm. See the comments below if you are curious
     * 
     * @param patternIndex The index in the pattern to match against
     * @param searchIndex The index in the search to match against
     * @param toSearch In what to search for a match
     * @return Whether a match was found
     */
    boolean infiniteMatchRecurse (int patternIndex,
                                  int searchIndex,
                                  List<String> toSearch) {
        // we've consumed everything from both the search and the pattern, so
        // we have a match
        if (searchIndex == toSearch.size() && patternIndex == pattern.size()) {
            return true;
        }
        // There's still something left over in either the search or the
        // pattern, so we don't have an exact match
        if (searchIndex == toSearch.size() || patternIndex == pattern.size()) {
            return false;
        }
        SymbolParsingRule currentPattern = pattern.get(patternIndex);
        if (currentPattern.matches(toSearch.get(searchIndex))) {
            int newPatternIndex = decideWhatTheNextPatternIs(
                patternIndex, searchIndex, toSearch);
            int newSearchIndex = searchIndex + 1;
            return true
                && infiniteMatchRecurse(newPatternIndex, newSearchIndex, toSearch);
        }
        return false;
    }

    /**
     * When iterating through a list of symbols and the pattern, figure out
     * what the next pattern to match is.
     * 
     * Currently, this is important for supporting repeating pattern rules.
     * For instance, if the current pattern is:
     * 
     * [{"label": "opening_parentheses", "level": -1, "repeating": false},
     *  {"label": "parameter", "level": 0, "repeating": true},
     *  {"label": "closing_parentheses", "level": -1, "repeating": false}]
     *  
     * and the list of symbols is:
     * 
     * ["opening_parentheses", "parameter", "parameter", "closing_parentheses"]
     * 
     * when we reach the first "parameter" in the list of symbols and in the
     * pattern, we want to figure out whether the next pattern to match will be
     * "closing_parentheses" or "parameter" when we iterate to the next symbol
     * in the list. This is achieved by looking ahead to the next symbol and
     * seeing if that symbol matches the next pattern.
     * 
     * In the case of the first "parameter", the pattern stays at "parameter"
     * to match for the second "parameter". In the case of the second
     * "parameter", the pattern list would advance to the next pattern because
     * the next symbol, "closing_parentheses", matches the next pattern,
     * "closing_parentheses".
     * 
     * @param currentPatternIndex The current index in the pattern list
     * @param currentSymbolIndex The current index in the list of symbols
     * @param symbols The list of symbols to use
     * @return The next index in the pattern list
     */
    private int decideWhatTheNextPatternIs(int currentPatternIndex,
                                          int currentSymbolIndex,
                                          List<String> symbols){
        if (pattern.get(currentPatternIndex).isRepeating()) {
            boolean lastPattern = (currentPatternIndex + 1 == pattern.size());
            boolean lastSymbol = (currentSymbolIndex + 1 == symbols.size());
            if (! lastSymbol) {
                String nextSymbol = symbols.get(currentSymbolIndex + 1);
                if (lastPattern) {
                    return currentPatternIndex;
                }
                SymbolParsingRule nextPattern = pattern.get(currentPatternIndex + 1);
                if (! nextPattern.matches(nextSymbol)) {
                    return currentPatternIndex;
                }
            }
        }
        return currentPatternIndex + 1;
    }

    @Override
    public boolean hasMatch(List<ISyntaxNode<T>> nodes) {
        return matches(nodes) == -1 ? false: true;
    }

    @Override
    public List<ISyntaxNode<T>> reduce (List<ISyntaxNode<T>> nodes,
                                        IOperationFactory<T> factory) {
        int matchLocation = matches(nodes);
        if (matchLocation == -1) {
            return nodes;
        }
        List<ISyntaxNode<T>> reduced = reduce(nodes, matchLocation, factory);
        return reduced;
    }

    /**
     * Create a nested list of symbols from a flat list
     * 
     * @param nodes The nodes to nest
     * @param index The starting index where the nesting will occur
     * @param factory The factory used to create additional generated nodes
     * @return The nested list
     */
    private List<ISyntaxNode<T>> reduce (List<ISyntaxNode<T>> nodes, int index,
                                         IOperationFactory<T> factory) {
        List<ISyntaxNode<T>> newNodes = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            newNodes.add(nodes.get(i));
        }
        List<ISyntaxNode<T>> reducedNodes = new ArrayList<>(nodes.subList(index, nodes.size()));
        reducedNodes = createNestedNodes(reducedNodes, factory);
        newNodes.addAll(reducedNodes);
        return newNodes;
    }

    /**
     * Nest the nodes and any additional generated nodes based on their level
     * 
     * @param nodes The nodes to nest
     * @param factory The factory used to create additional generated nodes
     * @return The nested list of nodes
     */
    private List<ISyntaxNode<T>> createNestedNodes (List<ISyntaxNode<T>> nodes,
                                                    IOperationFactory<T> factory) {
        SortedMap<Integer, List<ISyntaxNode<T>>> levels = new TreeMap<>();
        if (pattern.size() == 0){
            return nodes;
        }
        appendNodesByLevel(nodes, levels);

        for (SymbolParsingRule rule: additional) {
            ISyntaxNode<T> newRule = new SyntaxNode<T>(new Token("", rule.label()));
            appendToMapOfLists(rule.level(), newRule, levels);
        }

        List<ISyntaxNode<T>> nestedNodes = nestNodesByLevel(levels, factory);
        return nestedNodes;
    }

    /**
     * Add all pattern nodes, grouping them in sorted order by level
     * 
     * @param nodes The nodes to add
     * @param levels The levels SortedMap to add the nodes to
     */
    private void appendNodesByLevel (List<ISyntaxNode<T>> nodes,
                                     SortedMap<Integer, List<ISyntaxNode<T>>> levels) {
        int currentRuleIndex = 0;
        for (int i = 0; i < nodes.size(); i++) {
            ISyntaxNode<T> node = nodes.get(i);
            SymbolParsingRule currentRule = pattern.get(currentRuleIndex);
            if (! currentRule.drop()){
                appendToMapOfLists(currentRule.level(), node, levels);
            }
            List<String> nodesAsString = new ArrayList<>();
            for (ISyntaxNode<T> stringyNode: nodes) {
                nodesAsString.add(stringyNode.token().label());
            }
            currentRuleIndex = decideWhatTheNextPatternIs(
                currentRuleIndex, i, nodesAsString);
        }
    }

    /**
     * Helper function to append a new entry to a list with a certain key.
     * 
     * This technically could be generalized for all Collections, but handling
     * types such as Sets introduces additional complexity that is unnecessary
     * for this particular application.
     * 
     * @param key The key of the list to append to
     * @param toAppend The item to append to the list
     * @param map The map to append the item to
     */
    private <K,V> void appendToMapOfLists(K key, V toAppend, Map<K,List<V>> map) {
        List<V> newValue;
        if (map.containsKey(key)){
            newValue = new ArrayList<>(map.get(key));
        }
        else {
            newValue = new ArrayList<>();
        }
        newValue.add(toAppend);
        map.put(key, newValue);
    }

    /**
     * Nest the nodes by level using a SortedMap
     * 
     * @param levels The SortedMap of levels to use
     * @param factory The factory to use to create the operation for each node
     * @return A list which contains the tree of nested nodes
     */
    private List<ISyntaxNode<T>> nestNodesByLevel(
            SortedMap<Integer, List<ISyntaxNode<T>>> levels,
            IOperationFactory<T> factory){
        // yo dawg I heard you like generics
        Iterator<Entry<Integer, List<ISyntaxNode<T>>>> levelIterator =
                levels.entrySet().iterator();
        if (! levelIterator.hasNext()) {
            return new ArrayList<>();
        }
        List<ISyntaxNode<T>> currentLevel = levelIterator.next().getValue();
        for (ISyntaxNode<T> node: currentLevel) {
            node.setOperation(factory.makeOperation(node));
        }
        while(levelIterator.hasNext()) {
            ISyntaxNode<T> nextLevel = levelIterator.next().getValue().get(0);
            nextLevel.setChildren(currentLevel);
            nextLevel.setOperation(factory.makeOperation(nextLevel));
            currentLevel = new ArrayList<>();
            currentLevel.add(nextLevel);
        }
        return currentLevel;
    }

    @Override
    public String toString () {
        return pattern.toString();
    }
}
