package distilled_slogo.parsing;

import java.util.List;
import distilled_slogo.tokenization.IToken;

/**
 * An implementation of a parse tree node. 
 *
 * @param <T> The operation associated with the node
 */
public class SyntaxNode<T> implements ISyntaxNode<T> {

    private T operation;
    private List<ISyntaxNode<T>> children;
    private IToken token;

    /**
     * Create a new parse tree node
     * 
     * @param token The token associated with the node
     * @param operation The operation associated with the node
     * @param children The children of this node
     */
    public SyntaxNode (IToken token, T operation, List<ISyntaxNode<T>> children) {
        this.token = token;
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
    public IToken token () {
        return token;
    }

    @Override
    public String toString () {
        return token.toString();
    }
}
