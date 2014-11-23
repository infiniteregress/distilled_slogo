package distilled_slogo.parsing;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import distilled_slogo.Constants;
import distilled_slogo.MalformedSyntaxException;
import distilled_slogo.tokenization.IToken;

public class Parser implements IParser<String> {

    private List<IGrammarRule<String>> rules;
    //private IOperationFactory operationFactory;

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
            int index = hasProduction(nodeStack);
            while (index != -1) {
                nodeStack = reduce(nodeStack, index);
                index = hasProduction(nodeStack);
            }
        }
        if (nodeStack.size() == 1) {
            return nodeStack.get(0);
        } else {
            throw new MalformedSyntaxException(generateParseErrorMessage(nodes, nodeStack));
        }
    }

    public List<ISyntaxNode<String>> tokensToNodes (List<IToken> tokens) throws MalformedSyntaxException {
        List<ISyntaxNode<String>> nodes = new ArrayList<>();
        for (IToken token : tokens) {
            ISyntaxNode<String> newNode =
                    new SyntaxNode<String>(token.type(), token.text(), new ArrayList<>());
            nodes.add(newNode);
        }
        return nodes;
    }

    private int hasProduction (List<ISyntaxNode<String>> nodeStack) {
        for (IGrammarRule<String> rule : rules) {
            int index = rule.matches(nodeStack);
            if (index != -1) {
                return index;
            }
        }
        return -1;
    }

    private List<ISyntaxNode<String>> reduce (List<ISyntaxNode<String>> nodes, int index) {
        List<ISyntaxNode<String>> newNodes = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            newNodes.add(nodes.get(i));
        }
        List<ISyntaxNode<String>> reducedNodes = new ArrayList<>(nodes.subList(index, nodes.size()));
        ISyntaxNode<String> newNode = createNestedNode(reducedNodes);
        newNodes.add(newNode);
        return newNodes;
    }

    /**
     * Select a node to be the parent, and nest all other nodes under that node
     * 
     * Currently, we assume the first node to be the parent
     * 
     * @param nodes The nodes to nest
     * @return The first node, with the other nodes as children of the first node
     */
    private ISyntaxNode<String> createNestedNode (List<ISyntaxNode<String>> nodes) {
        List<ISyntaxNode<String>> args = new ArrayList<>(nodes.subList(1, nodes.size()));
        ISyntaxNode<String> operation = nodes.get(0);
        operation.setChildren(args);

        ISyntaxNode<String> result = new SyntaxNode<>(Constants.CONSTANT_LABEL, Constants.RESULT_LABEL,
                new ArrayList<>());
        List<ISyntaxNode<String>> resultChildren = new ArrayList<>();
        resultChildren.add(operation);
        result.setChildren(resultChildren);
        return result;
    }

    private String generateParseErrorMessage (List<ISyntaxNode<String>> all, List<ISyntaxNode<String>> remaining) {
        return "Reducing " + all + " failed, creating " + remaining + " instead";
    }
}
