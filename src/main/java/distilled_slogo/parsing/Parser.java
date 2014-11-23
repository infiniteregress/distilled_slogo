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
 */
public class Parser implements IParser<String> {

    private List<IGrammarRule<String>> rules;
    //private IOperationFactory operationFactory;

    /**
     * Create a new parser with a certain list of rules
     * 
     * @param rules The rules this parser uses
     * @throws InvalidGrammarRuleException If the specified rules are null
     */
    public Parser (List<IGrammarRule<String>> rules) throws InvalidGrammarRuleException {
        loadGrammar(rules);
        //operationFactory = new OperationFactory();
    }

    @Override
    public void loadGrammar (List<IGrammarRule<String>> rules) throws InvalidGrammarRuleException {
        if (rules != null) {
            this.rules = rules;
        } else {
            throw new InvalidGrammarRuleException("The rules supplied are null");
        }
    }

    @Override
    public ISyntaxNode<String> parse (List<IToken> tokens) throws MalformedSyntaxException {
        List<ISyntaxNode<String>> nodes = tokensToNodes(tokens);
        List<ISyntaxNode<String>> nodeStack = new ArrayList<>();

        for (ISyntaxNode<String> node : nodes) {
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
    List<ISyntaxNode<String>> tokensToNodes (List<IToken> tokens) {
        List<ISyntaxNode<String>> nodes = new ArrayList<>();
        for (IToken token : tokens) {
            ISyntaxNode<String> newNode =
                    new SyntaxNode<String>(token, token.text(), new ArrayList<>());
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
    private List<ISyntaxNode<String>> tryProductions(List<ISyntaxNode<String>> nodes){
        List<ISyntaxNode<String>> newNodes = new ArrayList<>(nodes);
        boolean atLeastOneMatch;
        do {
            atLeastOneMatch = false;
            for (IGrammarRule<String> rule : rules) {
                if (rule.hasMatch(nodes)) {
                    newNodes = rule.reduce(newNodes);
                    atLeastOneMatch = true;
                }
            }
        } while (atLeastOneMatch);
        return newNodes;
    }
}
