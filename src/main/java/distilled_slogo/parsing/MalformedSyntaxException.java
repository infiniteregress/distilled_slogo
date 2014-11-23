package distilled_slogo.parsing;

import java.util.List;

/**
 * Indicate that the syntax of the command was incorrect
 *
 */
public class MalformedSyntaxException extends Exception {
    private static final long serialVersionUID = 927618039710294637L;
    private List<ISyntaxNode<String>> all;
    private List<ISyntaxNode<String>> remaining;

    /**
     * Indicate a syntax error during parsing, including a helpful message
     * 
     * @param all The list of symbols that were parsed
     * @param remaining The incorrect result of the parsing
     */
    public MalformedSyntaxException (List<ISyntaxNode<String>> all, List<ISyntaxNode<String>> remaining) {
        super("Reducing " + all + " failed, creating " + remaining + " instead");
        this.all = all;
        this.remaining = remaining;
    }

    /**
     * Get the symbols that were attempted to be parsed
     * 
     * @return The symbols
     */
    public List<ISyntaxNode<String>> allSymbols () {
        return all;
    }

    /**
     * Get the invalid list of symbols that were created during parsing
     * 
     * @return The symbols
     */
    public List<ISyntaxNode<String>> remainingSymbols () {
        return remaining;
    }
}
