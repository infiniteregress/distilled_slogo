package distilled_slogo.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import distilled_slogo.MalformedSyntaxException;
import org.junit.Test;
import distilled_slogo.Constants;

import distilled_slogo.tokenization.IToken;
import distilled_slogo.tokenization.Token;

public class ParsingTest {

    /*
    @Test
    public void testTokenToNode () {
        Parser parser = null;
        try {
            parser = new Parser(new ArrayList<>());
        } catch (InvalidGrammarRuleException e) {
            fail();
        }
        List<IToken> tokens = new ArrayList<>();
        tokens.add(new Token("Minus", "command"));
        tokens.add(new Token("Sum", "command"));
        tokens.add(new Token("50", "constant"));
        tokens.add(new Token("30", "constant"));
        List<ISyntaxNode> nodes = null;
        try {
            nodes = parser.tokensToNodes(tokens);
        } catch (MalformedSyntaxException e) {
            fail("Minus Sum 50 30 is a valid command");
        }
        String[] expected = { "Minus", "Sum", "constant", "constant" };
        assertEquals(4, nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            assertEquals(expected[i], nodes.get(i).type());
        }
        List<IToken> badTokens = new ArrayList<>();
        badTokens.add(new Token("hai", "command"));
        try {
            List<ISyntaxNode> badNodes = parser.tokensToNodes(badTokens);
            fail("Exception should be thrown for nonexistent command \"hai\"");
        } catch (MalformedSyntaxException e) {

        }
    }
    */

    @Test
    public void testGrammarRule () {
        String[] args = { "constant", "constant" };
        IGrammarRule<String> rule = new GrammarRule("Sum", Arrays.asList(args));
        List<ISyntaxNode<String>> tokens = new ArrayList<>();
        tokens.add(new SyntaxNode<>("Minus", null, null));
        tokens.add(new SyntaxNode<>("Sum", null, null));
        tokens.add(new SyntaxNode<>("constant", null, null));
        tokens.add(new SyntaxNode<>("constant", null, null));
        assertEquals(1, rule.matches(tokens));
    }

    @Test
    public void testParse () {
        String[] unaryArgs1 = { "constant" };
        String[] unaryArgs2 = { Constants.RESULT_LABEL };
        String[] binaryArgs1 = { "constant", "constant" };
        String[] binaryArgs2 = { Constants.RESULT_LABEL, Constants.RESULT_LABEL };
        
        List<IGrammarRule<String>> rules = new ArrayList<>();

        rules.add(new GrammarRule("unaryCommand", unaryArgs1));
        rules.add(new GrammarRule("unaryCommand", unaryArgs2));
        rules.add(new GrammarRule("binaryCommand", binaryArgs1));
        rules.add(new GrammarRule("binaryCommand", binaryArgs2));

        Parser parser = null;
        try {
            parser = new Parser(rules);
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
        assertEquals("constant", root.type());
    }

    @Test(expected = MalformedSyntaxException.class)
    public void testInvalidCommand () throws Exception {
        Parser parser = new Parser(new ArrayList<>());
        IToken[] tokens = { new Token("foo", "fruh"), new Token("ins", "bett") };
        parser.parse(Arrays.asList(tokens));
    }

    @Test(expected = MalformedSyntaxException.class)
    public void testUnparseableCommand () throws Exception {
        String[] unaryArgs1 = { "constant" };
        IGrammarRule<String> unaryRule = new GrammarRule("Minus", Arrays.asList(unaryArgs1));
        List<IGrammarRule<String>> rules = new ArrayList<>();
        rules.add(unaryRule);
        Parser parser = new Parser(rules);
        IToken[] tokens = { new Token("Minus", Constants.COMMAND_LABEL),
                new Token("Minus", Constants.COMMAND_LABEL) };
        parser.parse(Arrays.asList(tokens));
    }
}
