package distilled_slogo.parsing;

/**
 * An interface that transforms tokens into operations within each
 * ISyntaxNode. Write an adapter/wrapper for your factory classes
 * using this interface to automatically create operations while
 * parsing.
 * 
 * @param <T> The operation to return
 */
public interface IOperationFactory<T> {
    /**
     * Make an operation based on a string parameter
     * 
     * @param currentNode The ISyntaxNode to make an operation for
     * @return The created operation
     */
    public T makeOperation(ISyntaxNode<T> currentNode);
}
