package distilled_slogo.parsing;

public class StringOperationFactory implements IOperationFactory<String> {
    @Override
    public String makeOperation (String string) {
        return string;
    }
}
