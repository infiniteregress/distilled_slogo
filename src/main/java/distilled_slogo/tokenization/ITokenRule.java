package distilled_slogo.tokenization;

import java.util.Map;

/**
 * A rule for recognizing a token
 *
 */
public interface ITokenRule {
    /**
     * Check if a string of text matches and tokenize it if so
     * 
     * @param text The text to match for
     * @param isStart Whether the text is at the start of the stream
     * @param isEnd Whether the text is at the end of the stream
     * @return A map with "opening", "body", and "closing" keys corresponding to
     *         the tokens representing the opening, body, and closing
     */
    public Map<String, IToken> match (String text, boolean isStart, boolean isEnd);
}
