package distilled_slogo.parsing;

import java.util.ArrayList;
import java.util.List;

public class StubNestedOperationFactory implements IOperationFactory<StubNestedOperation>{

    @Override
    public StubNestedOperation makeOperation (ISyntaxNode<StubNestedOperation> currentNode) {
        List<StubNestedOperation> children = new ArrayList<>();
        for (ISyntaxNode<StubNestedOperation> child: currentNode.children()) {
            children.add(child.operation());
        }
        StubNestedOperation current = new StubNestedOperation(currentNode.token().label(), children);
        return current;
    }

}
