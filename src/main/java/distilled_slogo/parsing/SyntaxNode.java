package distilled_slogo.parsing;

import java.util.ArrayList;
import java.util.List;
import distilled_slogo.tokenization.IToken;

/**
 * An implementation of a parse tree node. 
 *
 * @param <T> The operation associated with the node
 */
public class SyntaxNode<T> implements ISyntaxNode<T> {

    private T operation;
    private boolean operationIsSet;
    private List<ISyntaxNode<T>> children;
    private IToken token;

    /**
     * Create a new parse tree node
     * 
     * @param token The token associated with the node
     */
    public SyntaxNode (IToken token) {
        this.token = token;
        this.operation = null;
        this.operationIsSet = false;
        this.children = new ArrayList<>();
    }

    @Override
    public T operation () throws IllegalStateException {
        if (operationIsSet) {
            return operation;
        }
        else {
            throw new IllegalStateException("Operation was not set for: " + this);
        }
    }

    @Override
    public void setOperation(T operation) {
        this.operation = operation;
        this.operationIsSet = true;
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
