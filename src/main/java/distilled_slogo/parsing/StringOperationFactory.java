package distilled_slogo.parsing;

/**
 * A class implementing IOperationFactory which just returns the String
 * passed in as a parameter
 *
 */
public class StringOperationFactory implements IOperationFactory<String> {
    @Override
    public String makeOperation (ISyntaxNode<String> currentNode) {
        return currentNode.token().label();
    }
}
