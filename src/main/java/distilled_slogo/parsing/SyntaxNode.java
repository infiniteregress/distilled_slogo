package distilled_slogo.parsing;

import java.util.List;

public class SyntaxNode implements ISyntaxNode {

    private IOperation operation;
    private List<ISyntaxNode> children;
    private String type;

    public SyntaxNode (String type, IOperation operation, List<ISyntaxNode> children) {
        this.type = type;
        this.operation = operation;
        this.children = children;
    }

    @Override
    public IOperation operation () {
        return operation;
    }

    @Override
    public List<ISyntaxNode> children () {
        return children;
    }

    @Override
    public void setChildren (List<ISyntaxNode> children) {
        this.children = children;
    }

    @Override
    public String type () {
        return type;
    }

    @Override
    public String toString () {
        return type;
    }
}
