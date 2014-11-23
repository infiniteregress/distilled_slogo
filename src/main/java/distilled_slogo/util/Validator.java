package distilled_slogo.util;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class Validator {
    public static boolean validate (String jsonPath, String schemaPath, Object theClass) throws IOException, ProcessingException {
        String tokenRuleString = FileLoader.loadExternalFile(jsonPath);
        String schemaString = FileLoader.loadInternalFile(schemaPath, theClass);
        JsonNode schemaNode = makeJsonNode(schemaString);
        JsonNode tokenRule = makeJsonNode(tokenRuleString);
        
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema schema = factory.getJsonSchema(schemaNode);
        try {
            ProcessingReport report = schema.validate(tokenRule);
            return report.isSuccess();
        }
        catch (ProcessingException e) {
            return false;
        }
    }
    private static JsonNode makeJsonNode(String string) throws JsonProcessingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(string);
        return node;
    }
}
