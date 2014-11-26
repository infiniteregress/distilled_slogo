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
     * @param schemaPath The path to the JSON schema used for validation.
     *                   This path is always internal to protect the JSON
     *                   schema from modification 
     * @param jsonLoader The file loader to load the json file
     * @return Whether the JSON is valid
     * @throws IOException If an error occurred reading files
     */
    public static boolean validate (String jsonPath, String schemaPath, FileLoader jsonLoader) throws IOException {
        String schemaString = new InternalFileLoader().loadFile(schemaPath);
        JsonNode schemaNode = makeJsonNode(schemaString);
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        try {
            String tokenRuleString = jsonLoader.loadFile(jsonPath);
            JsonNode tokenRule = makeJsonNode(tokenRuleString);
            JsonSchema schema = factory.getJsonSchema(schemaNode);
            ProcessingReport report = schema.validate(tokenRule);
            return report.isSuccess();
        }
        catch (ProcessingException | IOException e) {
            return false;
        }
    }
    private static JsonNode makeJsonNode(String string) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(string);
        return node;
    }
}
