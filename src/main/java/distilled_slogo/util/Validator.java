package distilled_slogo.util;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * A utility class to validate JSON using JSON Schema.
 * 
 * Currently, JSON Schema version 4 is supported.
 *
 */
public class Validator {
    /**
     * Validate a json file using a json schema file
     * 
     * @param jsonPath The path to the JSON to validate
     * @param schemaPath The path to the JSON schema used for validation
     * @param jsonIsExternal Whether the json path is internal or external
     * @param theClass The class relative which to evaluate the file path
     * @return Whether the JSON is valid
     * @throws IOException If an error occurred reading files
     */
    public static boolean validate (String jsonPath, String schemaPath, boolean jsonIsExternal, Object theClass) throws IOException {
        String tokenRuleString = FileLoader.loadExternalFile(jsonPath);
        String schemaString = FileLoader.loadInternalFile(schemaPath, theClass);
        JsonNode schemaNode = makeJsonNode(schemaString);
        JsonNode tokenRule = makeJsonNode(tokenRuleString);
        
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        try {
            JsonSchema schema = factory.getJsonSchema(schemaNode);
            ProcessingReport report = schema.validate(tokenRule);
            return report.isSuccess();
        }
        catch (ProcessingException e) {
            return false;
        }
    }
    private static JsonNode makeJsonNode(String string) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(string);
        return node;
    }
}
