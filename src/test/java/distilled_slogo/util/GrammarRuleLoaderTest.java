package distilled_slogo.util;

import java.io.IOException;
import java.util.List;
import org.junit.Test;
import distilled_slogo.parsing.IGrammarRule;
import distilled_slogo.parsing.Parser;
import distilled_slogo.parsing.StringOperationFactory;

// Note these tests only work when running mvn test due to file paths
public class GrammarRuleLoaderTest {
    @Test
    public void testLoadGood() throws IOException, InvalidRulesException {
        GrammarRuleLoader<String> loader = new GrammarRuleLoader<>("./src/test/resources/parsing_rules.json");
        List<IGrammarRule<String>> rules = loader.getRules();
        Parser<String> parser = new Parser<>(rules, new StringOperationFactory());
    }
    @Test(expected = InvalidRulesException.class)
    public void testLoadBad() throws IOException, InvalidRulesException {
        GrammarRuleLoader<String> loader = new GrammarRuleLoader<>("./src/test/resources/parsing_rules_bad.json");
    }
}
