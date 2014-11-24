package distilled_slogo.parsing;

import java.util.ArrayList;
import java.util.List;
import distilled_slogo.tokenization.IToken;

/**
 * Indicate that the syntax of the command was incorrect
 *
 */
public class MalformedSyntaxException extends Exception {
    private static final long serialVersionUID = 927618039710294637L;
    private List<IToken> all;
    private List<IToken> remaining;

    /**
     * Indicate a syntax error during parsing, including a helpful message
     * 
     * @param all The list of symbols that were parsed
     * @param remaining The incorrect result of the parsing
     * @param <T> The generic type of the ISyntaxNode. Not used.
     */
    public <T> MalformedSyntaxException (List<ISyntaxNode<T>> all, List<ISyntaxNode<T>> remaining) {
        super("Reducing " + all + " failed, creating " + remaining + " instead");
        this.all = new ArrayList<>();
        for (ISyntaxNode<T> node: all) {
            this.all.add(node.token());
        }
        this.remaining = new ArrayList<>();
        for (ISyntaxNode<T> node: remaining) {
            this.remaining.add(node.token());
        }
    }

    /**
     * Get the symbols that were attempted to be parsed
     * 
     * @return The symbols
     */
    public List<IToken> allSymbols () {
        return all;
    }

    /**
     * Get the invalid list of symbols that were created during parsing
     * 
     * @return The symbols
     */
    public List<IToken> remainingSymbols () {
        return remaining;
    }
}
