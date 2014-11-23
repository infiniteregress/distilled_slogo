package distilled_slogo.tokenization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import distilled_slogo.Constants;
import distilled_slogo.util.FileLoader;
import distilled_slogo.util.Validator;

public class TokenRuleLoader {
    private List<ITokenRule> rules;
    public TokenRuleLoader(String tokenRulePath) throws IOException, InvalidTokenRulesException, ProcessingException {
        this(tokenRulePath, "/token_rule_schema.json");
    }
    private TokenRuleLoader(String tokenRulePath, String schemaPath)
            throws IOException, InvalidTokenRulesException, ProcessingException {
        if (Validator.validate(tokenRulePath, schemaPath, this)) {
            rules = generateRules(tokenRulePath);
        }
        else {
            throw new InvalidTokenRulesException(tokenRulePath + " is not valid");
        }
    }

    public List<ITokenRule> generateRules(String tokenRulePath) throws IOException{
        String tokenRuleString = FileLoader.loadExternalFile(tokenRulePath);
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
    public List<ITokenRule> getRules(){
        return rules;
    }
}
