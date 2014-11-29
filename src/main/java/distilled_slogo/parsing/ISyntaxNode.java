package distilled_slogo.parsing;

import java.util.List;
import distilled_slogo.tokenization.IToken;

/**
 * A node containing a certain operation to perform, and its children
 * 
 * @param <T> The operation contained within this node
 */
public interface ISyntaxNode<T> {
    /**
     * Get the operation associated with the node
     * 
     * @throws IllegalStateException If the operation is not set yet
     * @return The operation associated with the node
     */
    public T operation () throws IllegalStateException;

    /**
     * Set the operation associated with the node
     * 
     * @param operation The operation associated with the node
     */
    public void setOperation(T operation);

    /**
     * Get the children of this node
     * 
     * @return The node's children
     */
    public List<ISyntaxNode<T>> children ();

    /**
     * Set the children of this node
     * 
     * @param children The node's children
     */
    public void setChildren (List<ISyntaxNode<T>> children);

    /**
     * Get the token associated with this node
     * 
     * @return The token
     */
    public IToken token ();
}
