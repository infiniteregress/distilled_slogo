package distilled_slogo.parsing;

/**
 * A class implementing IOperationFactory which just returns the String
 * passed in as a parameter
 *
 */
public class StringOperationFactory implements IOperationFactory<String> {
    @Override
    public String makeOperation (String string) {
        return string;
    }
}
