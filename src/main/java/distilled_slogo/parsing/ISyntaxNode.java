package distilled_slogo.parsing;

import java.util.List;

/**
 * A node of an abstract syntax tree
 *
 */
public interface ISyntaxNode {
    /**
     * Get the operation associated with the node
     * 
     * @return the operation associated with the node
     */
    IOperation operation ();

    /**
     * Get the children of this node
     * 
     * @return the node's children
     */
    List<ISyntaxNode> children ();

    public void setChildren (List<ISyntaxNode> children);

    /**
     * Get a name representing the child
     * 
     * @return the name
     */
    String type ();
}
