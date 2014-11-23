package distilled_slogo.parsing;

import java.util.List;

public class SyntaxNode<T> implements ISyntaxNode<T> {

    private T operation;
    private List<ISyntaxNode<T>> children;
    private String type;

    public SyntaxNode (String type, T operation, List<ISyntaxNode<T>> children) {
        this.type = type;
        this.operation = operation;
        this.children = children;
    }

    @Override
    public T operation () {
        return operation;
    }

    @Override
    public List<ISyntaxNode<T>> children () {
        return children;
    }

    @Override
    public void setChildren (List<ISyntaxNode<T>> children) {
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
