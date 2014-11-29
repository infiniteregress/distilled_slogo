package distilled_slogo.parsing;

import java.util.ArrayList;
import java.util.List;
import distilled_slogo.tokenization.IToken;

/**
 * A class which generates a parse tree for a list of tokens based on certain
 * rules
 * 
 * Note that this implementation only creates nodes with a string as each
 * node's "payload"
 * 
 * @param <T> The type of operation to be created in each node of the parse tree
 */
public class Parser<T> implements IParser<T> {

    private List<IGrammarRule<T>> rules;
    private IOperationFactory<T> operationFactory;

    /**
     * Create a new parser with a certain list of rules
     * 
     * @param rules The rules this parser uses
     * @param factory The factory used to create operations of a certain type
     * @throws InvalidGrammarRuleException If the specified rules are null
     */
    public Parser (List<IGrammarRule<T>> rules, IOperationFactory<T> factory) throws InvalidGrammarRuleException {
        loadGrammar(rules);
        this.operationFactory = factory;
    }

    @Override
    public void loadGrammar (List<IGrammarRule<T>> rules) throws InvalidGrammarRuleException {
        if (rules != null) {
            this.rules = rules;
        } else {
            throw new InvalidGrammarRuleException("The rules supplied are null");
        }
    }

    @Override
    public ISyntaxNode<T> parse (List<IToken> tokens) throws MalformedSyntaxException {
        List<ISyntaxNode<T>> nodes = tokensToNodes(tokens);
        List<ISyntaxNode<T>> nodeStack = new ArrayList<>();

        for (ISyntaxNode<T> node : nodes) {
            nodeStack.add(node);
            nodeStack = tryProductions(nodeStack);
        }
        if (nodeStack.size() == 1) {
            return nodeStack.get(0);
        } else {
            throw new MalformedSyntaxException(nodes, nodeStack);
        }
    }

    /**
     * Convert the list of tokens to a flat list of parse tree nodes. Note that
     * the "payload" of these tree nodes is currently just the text associated
     * with the token.
     *  
     * @param tokens The list of tokens to convert
     * @return The corresponding list of parse tree nodes
     */
    List<ISyntaxNode<T>> tokensToNodes (List<IToken> tokens) {
        List<ISyntaxNode<T>> nodes = new ArrayList<>();
        for (IToken token : tokens) {
            ISyntaxNode<T> newNode =
                    new SyntaxNode<T>(token);
            nodes.add(newNode);
        }
        return nodes;
    }
    
    /**
     * Keep on trying to reduce the specified input until no more rules can be
     * applied
     * 
     * @param nodes The list of nodes to reduce
     * @return The reduce list of nodes
     */
    private List<ISyntaxNode<T>> tryProductions(List<ISyntaxNode<T>> nodes){
        List<ISyntaxNode<T>> newNodes = new ArrayList<>(nodes);
        boolean atLeastOneMatch;
        do {
            atLeastOneMatch = false;
            for (IGrammarRule<T> rule : rules) {
                if (rule.hasMatch(newNodes)) {
                    newNodes = rule.reduce(newNodes, operationFactory);
                    atLeastOneMatch = true;
                }
            }
        } while (atLeastOneMatch);
        return newNodes;
    }
}
