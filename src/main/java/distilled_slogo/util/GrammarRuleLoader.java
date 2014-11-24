package distilled_slogo.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import distilled_slogo.Constants;
import distilled_slogo.parsing.GrammarRule;
import distilled_slogo.parsing.IGrammarRule;
import distilled_slogo.parsing.InvalidGrammarRuleException;

/**
 * A class to load grammar rules from a file.
 */
public class GrammarRuleLoader extends RuleLoader<IGrammarRule<String>>{
    private static final String grammarRuleSchemaPath = "/grammar_rule_schema.json";

    /**
     * Create a new grammar rule loader that loads an external file
     * 
     * @param grammarRulePath The path to the grammar rule file
     * @throws IOException If a file I/O error occurs
     * @throws InvalidRulesException If the grammar rule file loaded is
     *                                     invalid
     */
    public GrammarRuleLoader(String grammarRulePath) throws IOException, InvalidRulesException {
        this(grammarRulePath, true);
    }
    /**
     * Create a new grammar rule loader that loads a file
     * 
     * @param grammarRulePath The path to the grammar rule
     * @param isExternal True if the file is relative to the filesytem,
     *                   false if it is relative to a class
     * @throws IOException If a file I/O error occurs
     * @throws InvalidRulesException If the grammar rule file loaded is invalid
     */
    public GrammarRuleLoader(String grammarRulePath, boolean isExternal) throws IOException, InvalidRulesException {
        super(grammarRulePath, grammarRuleSchemaPath, isExternal);
    }
    @Override
    public List<IGrammarRule<String>> generateRules (String grammarRulePath, boolean isExternal) throws IOException, InvalidRulesException {
        String grammarRuleString = FileLoader.loadFile(grammarRulePath, isExternal, this);
        JSONArray grammarRules = new JSONArray(grammarRuleString);
        List<IGrammarRule<String>> rules = new ArrayList<>();
        for (int i = 0; i < grammarRules.length(); i++){
            try {
                rules.add(makeGrammarRuleFromJsonObject(grammarRules.getJSONObject(i)));
            }
            catch (JSONException | InvalidGrammarRuleException e) {
                throw new InvalidRulesException(e.getMessage());
            }
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
}
