package distilled_slogo.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import distilled_slogo.Constants;
import distilled_slogo.tokenization.IToken;
import distilled_slogo.tokenization.Token;

public class ParsingTest {

    @Test
    public void testTokenToNode () {
        Parser parser = null;
        try {
            parser = new Parser(new ArrayList<>());
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
    public void testGrammarRule () throws InvalidGrammarRuleException {
        String[] args = { "Sum", "constant", "constant" };
        IGrammarRule<String> rule = new GrammarRule(args, "0", Constants.RESULT_LABEL);
        List<ISyntaxNode<String>> tokens = new ArrayList<>();
        tokens.add(new SyntaxNode<String>(new Token("Minus", "Minus"), "", new ArrayList<>()));
        tokens.add(new SyntaxNode<String>(new Token("Sum","Sum"), "", new ArrayList<>()));
        tokens.add(new SyntaxNode<String>(new Token("constant", "constant"), "", new ArrayList<>()));
        tokens.add(new SyntaxNode<String>(new Token("constant", "constant"), "", new ArrayList<>()));
        assertEquals(1, rule.matches(tokens));
    }

    @Test
    public void testParse () throws InvalidGrammarRuleException {
        String[] unaryArgs1 = { "unaryCommand", "constant" };
        String[] unaryArgs2 = { "unaryCommand", Constants.RESULT_LABEL };
        String[] binaryArgs1 = { "binaryCommand", "constant", "constant" };
        String[] binaryArgs2 = { "binaryCommand", Constants.RESULT_LABEL, Constants.RESULT_LABEL };
        
        List<IGrammarRule<String>> rules = new ArrayList<>();

        rules.add(new GrammarRule(unaryArgs1, "0", Constants.RESULT_LABEL));
        rules.add(new GrammarRule(unaryArgs2, "0", Constants.RESULT_LABEL));
        rules.add(new GrammarRule(binaryArgs1, "0", Constants.RESULT_LABEL));
        rules.add(new GrammarRule(binaryArgs2, "0", Constants.RESULT_LABEL));

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
        assertEquals("result", root.token().label());
    }

    @Test(expected = MalformedSyntaxException.class)
    public void testInvalidCommand () throws Exception {
        Parser parser = new Parser(new ArrayList<>());
        IToken[] tokens = { new Token("foo", "fruh"), new Token("ins", "bett") };
        parser.parse(Arrays.asList(tokens));
    }

    @Test(expected = MalformedSyntaxException.class)
    public void testUnparseableCommand () throws Exception {
        String[] unaryArgs1 = { "Minus", "constant" };
        IGrammarRule<String> unaryRule = new GrammarRule(unaryArgs1, "0", Constants.RESULT_LABEL);
        List<IGrammarRule<String>> rules = new ArrayList<>();
        rules.add(unaryRule);
        Parser parser = new Parser(rules);
        IToken[] tokens = { new Token("Minus", Constants.COMMAND_LABEL),
                new Token("Minus", Constants.COMMAND_LABEL) };
        parser.parse(Arrays.asList(tokens));
    }
}
