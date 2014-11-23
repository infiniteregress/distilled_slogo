package distilled_slogo.parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import distilled_slogo.Constants;
import distilled_slogo.tokenization.ITokenRule;
import distilled_slogo.util.FileLoader;
import distilled_slogo.util.Validator;

public class GrammarRuleLoader {
    private List<IGrammarRule<String>> rules;
    public GrammarRuleLoader(String grammarRulePath) throws JSONException, IOException, ProcessingException, InvalidGrammarRuleException{
        this(grammarRulePath, "/grammar_rule_schema.json");
    }
    private GrammarRuleLoader(String grammarRulePath, String schemaPath) throws JSONException, IOException, ProcessingException, InvalidGrammarRuleException{
        if (Validator.validate(grammarRulePath, schemaPath, this)){
            rules = generateRules(grammarRulePath);
        }
        else {
            throw new InvalidGrammarRuleException(grammarRulePath + "is not valid");
        }
    }
    private List<IGrammarRule<String>> generateRules (String grammarRulePath) throws IOException, JSONException, InvalidGrammarRuleException {
        String grammarRuleString = FileLoader.loadExternalFile(grammarRulePath);
        JSONArray grammarRules = new JSONArray(grammarRuleString);
        List<IGrammarRule<String>> rules = new ArrayList<>();
        for (int i = 0; i < grammarRules.length(); i++){
            rules.add(makeGrammarRuleFromJsonObject(grammarRules.getJSONObject(i)));
        }
        return rules;
    }
    private IGrammarRule<String> makeGrammarRuleFromJsonObject (JSONObject object) throws InvalidGrammarRuleException {
        String parent = object.getString(Constants.JSON_GRAMMAR_PARENT);
        String grandparent = "";
        if (object.has(Constants.JSON_GRAMMAR_GRANDPARENT)){
            grandparent = object.getString(Constants.JSON_GRAMMAR_GRANDPARENT);
        }
        JSONArray patternArray = object.getJSONArray(Constants.JSON_GRAMMAR_PATTERN);
        List<String> pattern = new ArrayList<>();
        for (int i = 0; i < patternArray.length(); i++){
            pattern.add(patternArray.getString(i));
        }
        IGrammarRule<String> rule = new GrammarRule(pattern, parent, grandparent);
        return rule;
    }
    public List<IGrammarRule<String>> getRules(){
        return rules;
    }
}
