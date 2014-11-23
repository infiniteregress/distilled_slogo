package distilled_slogo.tokenization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import distilled_slogo.Constants;

/**
 * A class implementing a regex tokenizer.
 *
 */
public class TokenRule implements ITokenRule {
    private final String type;
    private Map<String, String> definition;
    private final boolean ignoreCase;

    /**
     * A Builder to create new instances of a token rule
     */
    public static class Builder {
        private final String type;
        private Map<String, String> definition;
        private boolean ignoreCase;

        /**
         * Create a new Builder for the tokenizer; by default, let the
         * opening and closing be one or more whitespace characters,
         * and let the regex pattern be case sensitive
         * 
         * @param type The type of token
         * @param body A regex matching the body of the token
         */
        public Builder (String type, String body) {
            this.type = type;
            this.definition = new HashMap<>();
            definition.put(Constants.BODY_TOKEN_STRING, body);
            definition.put(Constants.OPENING_TOKEN_STRING, "\\s+");
            definition.put(Constants.CLOSING_TOKEN_STRING, "\\s+");
            this.ignoreCase = false;
        }

        /**
         * Explicitly set the opening regex
         * 
         * @param opening The regex defining the start of where to match the
         *                token
         * @return The Builder with the explicit opening regex
         */
        public Builder opening (String opening) {
            if (opening.length() != 0){
                this.definition.put(Constants.OPENING_TOKEN_STRING, opening);
            }
            return this;
        }

        /**
         * Explicitly set the closing regex
         * 
         * @param closing The regex defining the end of where to match the
         *                token
         * @return The Builder with the explicit closing regex
         */
        public Builder closing (String closing) {
            if (closing.length() != 0){
                this.definition.put(Constants.CLOSING_TOKEN_STRING, closing);
            }
            return this;
        }

        /**
         * Explicitly set whether to ignore the case
         * 
         * @param ignoreCase Whether to ignore case
         * @return The Builder with an explicit ignore case setting
         */
        public Builder ignoreCase (boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            return this;
        }

        /**
         * Finalize the Builder and create a new instance of a token rule
         * @return The new TokenRule instance
         */
        public TokenRule build () {
            return new TokenRule(this);
        }
    }

    /**
     * Create a new TokenRule from a Builder
     * @param builder The Builder to create from
     */
    private TokenRule (Builder builder) {
        this.type = builder.type;
        this.definition = builder.definition;
        this.ignoreCase = builder.ignoreCase;
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

    /**
     * Match a text using a regex
     * 
     * @param text The text to match for
     * @param parts The body, and potentially the opening and closing regexes
     * @return A Map with keys "body" and potentially "opening" and "closing"
     */
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

    /**
     * Generate a single regex pattern using the 1 to 3-length regex format
     * for a token rule
     * 
     * @param parts The body, and potentially the opening and closing, regex
     * @return The compiled Pattern of the combined regex
     */
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
    public String toString () {
        return type;
    }
}
