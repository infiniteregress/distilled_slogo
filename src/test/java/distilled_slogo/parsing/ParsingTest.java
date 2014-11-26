package distilled_slogo.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import distilled_slogo.tokenization.IToken;
import distilled_slogo.tokenization.Token;

public class ParsingTest {

    @Test
    public void testTokenToNode () {
        Parser<String> parser = null;
        try {
            parser = new Parser<>(new ArrayList<>(), new StringOperationFactory());
        } catch (InvalidGrammarRuleException e) {
            fail();
        }
        List<IToken> tokens = new ArrayList<>();
        tokens.add(new Token("Minus", "unaryCommand"));
        tokens.add(new Token("Sum", "binaryCommand"));
        tokens.add(new Token("50", "constant"));
        tokens.add(new Token("30", "constant"));
        List<ISyntaxNode<String>> nodes = null;
        nodes = parser.tokensToNodes(tokens);
        String[] expected = { "unaryCommand", "binaryCommand", "constant", "constant" };
        assertEquals(4, nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            assertEquals(expected[i], nodes.get(i).token().label());
        }
    }

    @Test
    public void testParse () throws InvalidGrammarRuleException {
        SymbolParsingRule[][] rule1 = {
            {
                new SymbolParsingRule("unaryCommand", 1, false),
                new SymbolParsingRule("constant|result", 0, false)
            },
            {
                new SymbolParsingRule("result", 2, false)
            }
        };
        SymbolParsingRule[][] rule2 = {
            {
                new SymbolParsingRule("binaryCommand", 1, false),
                new SymbolParsingRule("constant|result", 0, false),
                new SymbolParsingRule("constant|result", 0, false)
            },
            {
                new SymbolParsingRule("result", 2, false)
            }
        };

        List<IGrammarRule<String>> rules = new ArrayList<>();
        rules.add(new GrammarRule<String>(Arrays.asList(rule1[0]), Arrays.asList(rule1[1])));
        rules.add(new GrammarRule<String>(Arrays.asList(rule2[0]), Arrays.asList(rule2[1])));

        Parser<String> parser = null;
        try {
            parser = new Parser<>(rules, new StringOperationFactory());
        } catch (InvalidGrammarRuleException e) {
            fail("these are valid rules");
        }
        IToken[] tokens = { new Token("Minus", "unaryCommand"), new Token("Sum", "binaryCommand"),
                new Token("50", "constant"), new Token("80", "constant") };

        ISyntaxNode<String> root = null;
        try {
            root = parser.parse(Arrays.asList(tokens));
        } catch (MalformedSyntaxException e) {
            fail("Should be parsed correctly");
        }
        assertNotNull(root);
        assertEquals("result", root.token().label());
    }

    @Test(expected = MalformedSyntaxException.class)
    public void testInvalidCommand () throws Exception {
        Parser<String> parser = new Parser<>(new ArrayList<>(), new StringOperationFactory());
        IToken[] tokens = { new Token("foo", "fruh"), new Token("ins", "bett") };
        parser.parse(Arrays.asList(tokens));
    }

    @Test(expected = MalformedSyntaxException.class)
    public void testUnparseableCommand () throws Exception {
        SymbolParsingRule[] pattern = {
            new SymbolParsingRule("Minus", 1, false),
            new SymbolParsingRule("constant", 0, false)
        };
        SymbolParsingRule[] additional = {
            new SymbolParsingRule("result", 2, false)
        };
        IGrammarRule<String> grammarRule =
            new GrammarRule<>(Arrays.asList(pattern), Arrays.asList(additional));
        List<IGrammarRule<String>> rules = new ArrayList<>();
        rules.add(grammarRule);
        Parser<String> parser = new Parser<>(rules, new StringOperationFactory());
        IToken[] tokens = { new Token("", "Minus"),
                new Token("", "Minus") };
        parser.parse(Arrays.asList(tokens));
    }
}
