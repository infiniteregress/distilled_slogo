package distilled_slogo.parsing;

import java.util.List;

/**
 * A node containing a certain operation to perform, and its children
 * 
 * @param <T> The operation contained within this node
 */
public interface ISyntaxNode<T> {
    /**
     * Get the operation associated with the node
     * 
     * @return The operation associated with the node
     */
    public T operation ();

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
     * Get a name representing this node
     * 
     * @return The name
     */
    public String type ();
}
