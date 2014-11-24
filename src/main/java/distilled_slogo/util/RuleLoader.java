package distilled_slogo.util;

import java.io.IOException;
import java.util.List;

/**
 * An abstract class that loads and validates JSON rules using JSON schema
 *
 * @param <T> The type of rule to load
 */
public abstract class RuleLoader<T> {
    List<T> rules;
    private String schemaPath;
    /**
     * Create a rule loader to load JSON rules from a file
     * 
     * @param jsonPath The path to the JSON file
     * @param schemaPath The path to the schema file
     * @param jsonIsExternal Whether the JSON file is internal or external
     * @throws IOException If an error occurred reading files
     * @throws InvalidRulesException If the rules loaded are invalid
     */
    protected RuleLoader(String jsonPath, String schemaPath, boolean jsonIsExternal)
            throws IOException, InvalidRulesException {
        this.schemaPath = schemaPath;
        if (validate(jsonPath, jsonIsExternal)) {
            rules = generateRules(jsonPath, jsonIsExternal);
        }
        else {
            throw new InvalidRulesException(jsonPath + " is not valid");
        }
    }
    /**
     * Generate a list of rules based on a json file
     * 
     * @param jsonPath The path to the rules
     * @param isExternal Whether the rules file is external
     * @return The list of rules
     * @throws IOException If an error occurred reading files
     * @throws InvalidRulesException If the rules loaded are invalid
     */
    public abstract List<T> generateRules (String jsonPath, boolean isExternal) throws IOException, InvalidRulesException;

    /**
     * Validate the json file based on the JSON schema
     * 
     * @param jsonPath The path to the rules
     * @param isExternal Whether the rules file is external
     * @return Whether the rules file is valid
     * @throws IOException If an error occurred reading files
     */
    public boolean validate(String jsonPath, boolean isExternal) throws IOException {
        return Validator.validate(jsonPath, this.schemaPath, isExternal, this);
    }

    /**
     * Get the rules associated with this loader
     * 
     * @return The list of rules
     */
    public List<T> getRules(){
        return rules;
    }
}
