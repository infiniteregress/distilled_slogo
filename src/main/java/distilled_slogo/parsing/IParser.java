package distilled_slogo.parsing;

import java.util.List;
import distilled_slogo.MalformedSyntaxException;
import distilled_slogo.tokenization.IToken;

public interface IParser {
    public ISyntaxNode parse (List<IToken> tokens) throws MalformedSyntaxException;

    public void loadGrammar (List<IGrammarRule> rules) throws InvalidGrammarRuleException;
}
