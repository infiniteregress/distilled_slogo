package distilled_slogo.tokenization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import distilled_slogo.Constants;

public class TokenRule implements ITokenRule {
    private final String type;
    private Map<String, String> definition;
    private final boolean ignoreCase;

    public static class Builder {
        private final String type;
        private Map<String, String> definition;
        private boolean ignoreCase;

        public Builder (String type, String body) {
            this.type = type;
            this.definition = new HashMap<>();
            definition.put(Constants.BODY_TOKEN_STRING, body);
            definition.put(Constants.OPENING_TOKEN_STRING, "\\s+");
            definition.put(Constants.CLOSING_TOKEN_STRING, "\\s+");
            this.ignoreCase = false;
        }

        public Builder opening (String opening) {
            this.definition.put(Constants.OPENING_TOKEN_STRING, opening);
            return this;
        }

        public Builder closing (String closing) {
            this.definition.put(Constants.CLOSING_TOKEN_STRING, closing);
            return this;
        }

        public Builder ignoreCase (boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            return this;
        }

        public TokenRule build () {
            return new TokenRule(this);
        }
    }

    private TokenRule (Builder builder) {
        this.type = builder.type;
        this.definition = builder.definition;
        this.ignoreCase = builder.ignoreCase;
    }

    @Override
    public String toString () {
        return type;
    }

    private Map<String, IToken> matchRaw (String text, List<String> parts) {
        Pattern pattern = generatePattern(parts);
        Matcher matcher = pattern.matcher(text);
        Map<String, IToken> match = new HashMap<>();
        // need to call matches() this to enable group()
        if (matcher.matches()) {
            for (String part : parts) {
                match.put(part, new Token(matcher.group(part), type));
            }
        }
        return match;
    }

    private Pattern generatePattern (List<String> parts) {
        String regex = "^";
        for (String part : parts) {
            regex += "(?<" + part + ">" + definition.get(part) + ")";
        }
        regex += "$";
        int flags = ignoreCase ? Pattern.CASE_INSENSITIVE : 0;
        return Pattern.compile(regex, flags);
    }

    @Override
    public Map<String, IToken> match (String text, boolean isStart, boolean isEnd) {
        List<String> parts = new ArrayList<>();
        if (!isStart) {
            parts.add(Constants.OPENING_TOKEN_STRING);
        }
        parts.add(Constants.BODY_TOKEN_STRING);
        if (!isEnd) {
            parts.add(Constants.CLOSING_TOKEN_STRING);
        }
        return matchRaw(text, parts);
    }
}
