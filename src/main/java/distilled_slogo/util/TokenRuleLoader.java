package distilled_slogo.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import distilled_slogo.Constants;
import distilled_slogo.tokenization.ITokenRule;
import distilled_slogo.tokenization.InvalidTokenRulesException;
import distilled_slogo.tokenization.TokenRule;

/**
 * A class to load token rules from a file
 *
 */
public class TokenRuleLoader extends RuleLoader<ITokenRule> {
    private static final String tokenRuleSchemaPath = "/token_rule_schema.json";
    /**
     * Create a new token rule loader that loads an external file
     * 
     * @param tokenRulePath The path to the token rule file
     * @throws IOException If an error occurred reading the file
     * @throws InvalidTokenRulesException If the token rules are invalid
     */
    public TokenRuleLoader(String tokenRulePath) throws IOException, InvalidRulesException {
        this(tokenRulePath, new ExternalFileLoader());
    }

    /**
     * Create a new token rule loader that loads a file
     * 
     * @param tokenRulePath The path to the token rule file
     * @param jsonLoader The file loader used to load the token rules
     * @throws IOException If an error occurred reading the file
     * @throws InvalidRulesException If the token rules are invalid
     */
    public TokenRuleLoader(String tokenRulePath, FileLoader jsonLoader) throws IOException, InvalidRulesException {
        super(tokenRulePath, tokenRuleSchemaPath, jsonLoader);
    }

    @Override
    public List<ITokenRule> generateRules(String tokenRulePath, FileLoader loader) throws IOException{
        String tokenRuleString = loader.loadFile(tokenRulePath);
        JSONArray tokenRules = new JSONArray(tokenRuleString);
        List<ITokenRule> rules = new ArrayList<>();
        for (int i = 0; i < tokenRules.length(); i++) {
            rules.add(makeTokenRuleFromJsonObject(tokenRules.getJSONObject(i)));
        }
        return rules;
    }
    private ITokenRule makeTokenRuleFromJsonObject(JSONObject object){
        String label = object.getString(Constants.JSON_TOKEN_LABEL);
        String body = object.getString(Constants.JSON_TOKEN_BODY);
        String opening = "";
        String closing = "";
        if (object.has(Constants.JSON_TOKEN_OPENING)){
            opening = object.getString(Constants.JSON_TOKEN_OPENING);
        }
        if (object.has(Constants.JSON_TOKEN_CLOSING)){
            closing = object.getString(Constants.JSON_TOKEN_CLOSING);
        }
        ITokenRule rule =
                new TokenRule.Builder(label, body).opening(opening).closing(closing).build();
        return rule;
    }
}
