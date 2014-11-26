package distilled_slogo.parsing;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import distilled_slogo.tokenization.Token;

public class GrammarRuleTest {
    public void testInfiniteMatchRecurse (boolean expected, SymbolParsingRule[] pattern, String[] search)
            throws InvalidGrammarRuleException {
        GrammarRule<String> rule = new GrammarRule<>(Arrays.asList(pattern));
        boolean result = rule.infiniteMatchRecurse(0, 0, Arrays.asList(search));
        assertEquals(expected, result);
    }

    @Test
    public void testSunny () throws InvalidGrammarRuleException {
        boolean sunnyResult = true;
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("hi", 0, false),
            new SymbolParsingRule("there", 0, true),
            new SymbolParsingRule("foo", 0, false)
        };
        String[] toSearch = { "hi", "there", "there", "there", "there", "foo" };
        testInfiniteMatchRecurse(sunnyResult, pattern, toSearch);
    }

    @Test
    public void testSunnyEnd () throws InvalidGrammarRuleException {
        boolean sunnyEndResult = true;
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("hi", 0, false),
            new SymbolParsingRule("there", 0, true)
        };
        String[] toSearch = { "hi", "there", "there", "there", "there" };
        testInfiniteMatchRecurse(sunnyEndResult, pattern, toSearch);
    }

    @Test
    public void testExtraPattern () throws InvalidGrammarRuleException {
        boolean extraPatternResult = false;
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("hi", 0, false),
            new SymbolParsingRule("there", 0, true),
            new SymbolParsingRule("blah", 0, false)
        };
        String[] toSearch = { "hi", "there", "there", "there" };
        testInfiniteMatchRecurse(extraPatternResult, pattern, toSearch);
    }

    @Test
    public void testExtraSearch () throws InvalidGrammarRuleException {
        boolean extraSearchResult = false;
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("hi", 0, false),
            new SymbolParsingRule("there", 0, true)
        };
        String[] toSearch = { "hi", "there", "there", "blah" };
        testInfiniteMatchRecurse(extraSearchResult, pattern, toSearch);
    }

    @Test
    public void testJustOneRepeat () throws InvalidGrammarRuleException {
        boolean justOneRepeatResult = true;
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("hi", 0, false),
            new SymbolParsingRule("there", 0, true)
        };
        String[] toSearch = { "hi", "there" };
        testInfiniteMatchRecurse(justOneRepeatResult, pattern, toSearch);
    }

    @Test
    public void testMatches () throws InvalidGrammarRuleException {
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("hi", 0, false),
            new SymbolParsingRule("there", 0, true),
            new SymbolParsingRule("blah", 0, false)
        };
        IGrammarRule<String> rule = new GrammarRule<>(Arrays.asList(pattern));
        String[] tokens = { "o", "hai", "hi", "there", "there", "there", "blah" };
        List<ISyntaxNode<String>> nodes = new ArrayList<>();
        for (String token : tokens) {
            nodes.add(new SyntaxNode<String>(new Token("", token), "", new ArrayList<>()));
        }
        assertEquals(2, rule.matches(nodes));
    }
    
    @Test
    public void testReduce () throws InvalidGrammarRuleException {
        // sequence: "o", "hai", "there", "there", "blah"
        // pattern: hai, there(infinite), blah(-1) additional: "snap"
        // generates:
        // "o", "snap"
        //        |
        //      "hai"
        //      /   \
        // "there" "there"
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("hai", 1, false),
            new SymbolParsingRule("there", 0, true),
            new SymbolParsingRule("blah", -1, false)
        };
        SymbolParsingRule[] additional = {
            new SymbolParsingRule("snap", 2, false)
        };
        String[] sequence = { "o", "hai", "there", "there", "blah" };
        
        IGrammarRule<String> rule = new GrammarRule<>(Arrays.asList(pattern),
                Arrays.asList(additional));
        List<ISyntaxNode<String>> sequenceList = generateSequence(sequence);
        IOperationFactory<String> factory = new StringOperationFactory();
        List<ISyntaxNode<String>> reduced = rule.reduce(sequenceList, factory);
        assertEquals(2, reduced.size());
        assertEquals("o", reduced.get(0).token().label());
        assertEquals("snap", reduced.get(1).token().label());
        assertEquals(1, reduced.get(1).children().size());
        ISyntaxNode<String> haiNode = reduced.get(1).children().get(0);
        assertEquals("hai", haiNode.token().label());
        assertEquals(2, haiNode.children().size());
        assertEquals("there", haiNode.children().get(0).token().label());
        assertEquals("there", haiNode.children().get(1).token().label());
    }
    private List<ISyntaxNode<String>> generateSequence(String[] sequenceArray) {
        List<ISyntaxNode<String>> sequence = new ArrayList<>();
        for (String element: sequenceArray) {
            sequence.add(new SyntaxNode<>(new Token("", element), "", new ArrayList<>()));
        }
        return sequence;
    }
}
