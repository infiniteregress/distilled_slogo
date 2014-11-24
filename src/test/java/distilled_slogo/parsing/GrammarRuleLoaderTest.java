package distilled_slogo.parsing;

import java.io.IOException;
import java.util.List;
import org.junit.Test;
import distilled_slogo.util.GrammarRuleLoader;
import distilled_slogo.util.InvalidRulesException;

// Note these tests only work when running mvn test due to file paths
public class GrammarRuleLoaderTest {
    @Test
    public void testLoadGood() throws IOException, InvalidRulesException {
        GrammarRuleLoader loader = new GrammarRuleLoader("./src/test/resources/parsing_rules.json");
        List<IGrammarRule<String>> rules = loader.getRules();
        Parser<String> parser = new Parser<>(rules, new StringOperationFactory());
    }
    @Test(expected = InvalidRulesException.class)
    public void testLoadBad() throws IOException, InvalidRulesException {
        GrammarRuleLoader loader = new GrammarRuleLoader("./src/test/resources/parsing_rules_bad.json");
    }
}
