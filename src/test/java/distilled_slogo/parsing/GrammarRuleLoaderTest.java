package distilled_slogo.parsing;

import java.io.IOException;
import java.util.List;
import org.json.JSONException;
import org.junit.Test;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

// Note these tests only work when running mvn test due to file paths
public class GrammarRuleLoaderTest {
    @Test
    public void testLoadGood() throws JSONException, IOException, ProcessingException, InvalidGrammarRuleException{
        GrammarRuleLoader loader = new GrammarRuleLoader("./src/test/resources/parsing_rules.json");
        List<IGrammarRule<String>> rules = loader.getRules();
        Parser parser = new Parser(rules);
    }
    @Test(expected = InvalidGrammarRuleException.class)
    public void testLoadBad() throws JSONException, IOException, ProcessingException, InvalidGrammarRuleException {
        GrammarRuleLoader loader = new GrammarRuleLoader("./src/test/resources/parsing_rules_bad.json");
    }
}
