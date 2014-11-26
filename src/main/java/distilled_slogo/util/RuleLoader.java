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
     * @param jsonLoader The file loader used to load the JSON file
     * @throws IOException If an error occurred reading files
     * @throws InvalidRulesException If the rules loaded are invalid
     */
    protected RuleLoader(String jsonPath, String schemaPath, FileLoader jsonLoader)
            throws IOException, InvalidRulesException {
        this.schemaPath = schemaPath;
        if (validate(jsonPath, jsonLoader)) {
            rules = generateRules(jsonPath, jsonLoader);
        }
        else {
            throw new InvalidRulesException(jsonPath + " is not valid");
        }
    }
    /**
     * Generate a list of rules based on a json file
     * 
     * @param jsonPath The path to the rules
     * @param loader The file loader used to load the json file
     * @return The list of rules
     * @throws IOException If an error occurred reading files
     * @throws InvalidRulesException If the rules loaded are invalid
     */
    public abstract List<T> generateRules (String jsonPath, FileLoader loader) throws IOException, InvalidRulesException;

    /**
     * Validate the json file based on the JSON schema
     * 
     * @param jsonPath The path to the rules
     * @param jsonLoader The file loader used to load the json file
     * @return Whether the rules file is valid
     * @throws IOException If an error occurred reading files
     */
    public boolean validate(String jsonPath, FileLoader jsonLoader) throws IOException {
        return Validator.validate(jsonPath, this.schemaPath, jsonLoader);
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
