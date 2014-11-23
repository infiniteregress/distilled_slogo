package distilled_slogo.parsing;

import java.io.IOException;
import org.junit.Test;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import distilled_slogo.tokenization.InvalidTokenRulesException;
import distilled_slogo.tokenization.TokenRuleLoader;
import distilled_slogo.tokenization.Tokenizer;

public class TokenRuleLoaderTest {
    @Test
    public void testLoadGood() throws IOException, InvalidTokenRulesException, ProcessingException{
        TokenRuleLoader loader = new TokenRuleLoader("./src/test/resources/token_rules.json");
        Tokenizer tokenizer = new Tokenizer(loader.getRules());
    }
    @Test(expected=InvalidTokenRulesException.class)
    public void testLoadBad() throws IOException, InvalidTokenRulesException, ProcessingException{
        TokenRuleLoader loader = new TokenRuleLoader("./src/test/resources/token_rules_bad.json");
    }
}
