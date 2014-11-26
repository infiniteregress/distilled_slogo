package distilled_slogo.util;

import java.io.IOException;
import org.junit.Test;
import distilled_slogo.tokenization.Tokenizer;

public class TokenRuleLoaderTest {
    @Test
    public void testLoadGood() throws IOException, InvalidRulesException {
        TokenRuleLoader loader = new TokenRuleLoader("./src/test/resources/token_rules.json");
        Tokenizer tokenizer = new Tokenizer(loader.getRules());
    }
    @Test(expected=InvalidRulesException.class)
    public void testLoadBad() throws IOException, InvalidRulesException{
        TokenRuleLoader loader = new TokenRuleLoader("./src/test/resources/token_rules_bad.json");
    }
}
