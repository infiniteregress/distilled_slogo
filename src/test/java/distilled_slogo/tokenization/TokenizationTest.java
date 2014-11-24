package distilled_slogo.tokenization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import distilled_slogo.Constants;
import distilled_slogo.TestConstants;

public class TokenizationTest {
    @Test
    public void testMatch () {
        ITokenRule tokenRule = new TokenRule.Builder("constant", "-?[0-9]+\\.?[0-9]*").build();
        int[] results = { 3, 2, 2, 0 };
        String[] texts = { " -90.9 ", "-90.9   ", "   -90.9", "  asdf99 " };
        boolean[] isStarts = { false, true, false, false };
        boolean[] isEnds = { false, false, true, false };

        for (int i = 0; i < results.length; i++) {
            assertEquals(results[i], tokenRule.match(texts[i], isStarts[i], isEnds[i]).size());
        }
        Map<String, IToken> groupTester = tokenRule.match(" -50.5 ", false, false);
        assertEquals(" ", groupTester.get(Constants.OPENING_TOKEN_STRING).text());
        assertEquals("-50.5", groupTester.get(Constants.BODY_TOKEN_STRING).text());
        assertEquals(" ", groupTester.get(Constants.CLOSING_TOKEN_STRING).text());
    }

    @Test
    public void testTokenize () {
        List<ITokenRule> rules = new ArrayList<>();
        rules.add(new TokenRule.Builder(TestConstants.CONSTANT_LABEL, "-?[0-9]+\\.?[0-9]*").build());
        rules.add(new TokenRule.Builder(TestConstants.COMMAND_LABEL, "[a-zA-Z_]+(\\?)?").build());
        rules.add(new TokenRule.Builder(TestConstants.OPENING_LIST_LABEL, "\\[").build());
        Reader input = new StringReader("sum [ 60 50");
        ITokenizer tokenizer = null;
        try {
            tokenizer = new Tokenizer(rules);
        } catch (InvalidTokenRulesException e) {
            fail();
        }
        List<IToken> tokens = new ArrayList<>();
        try {
            tokens = tokenizer.tokenize(input);
        } catch (IOException e) {
            fail();
        }
        String[][] blah = { { TestConstants.COMMAND_LABEL, "sum" },
                { TestConstants.OPENING_LIST_LABEL, "[" }, { TestConstants.CONSTANT_LABEL, "60" },
                { TestConstants.CONSTANT_LABEL, "50" } };
        for (int i = 0; i < blah.length; i++) {
            assertEquals(blah[i][0], tokens.get(i).label());
            assertEquals(blah[i][1], tokens.get(i).text());
        }
    }
}
