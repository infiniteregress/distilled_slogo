package distilled_slogo.parsing;

import java.util.regex.Pattern;
import distilled_slogo.Constants;

/**
 * A class to encapsulate the parsing rule for a single symbol.
 * 
 * Contains a label pattern, which is a string regex indicating what labels to
 * match, and a level, indicating what level the symbol should be put at in
 * the generated tree.
 *
 */
public class SymbolParsingRule {
    private final String label;
    private final Pattern labelPattern;
    private final int level;
    private final boolean repeating;

    /**
     * Create a new symbol parsing rule
     * 
     * @param label The regex pattern to match
     * @param level The level the symbol should be placed at in the tree
     * @param repeating Whether the pattern is repeating or not
     */
    public SymbolParsingRule(String label, int level, boolean repeating) {
        this.label = label;
        this.labelPattern = Pattern.compile("^" + label + "$");
        this.level = level;
        this.repeating = repeating;
    }
    /**
     * Get the label of this rule
     * 
     * @return The label
     */
    public String label(){
        return label;
    }
    /**
     * Get the level the symbol should be placed at in the tree
     * 
     * @return The level
     */
    public int level(){
        return level;
    }
    /**
     * Check whether the symbol should be excluded from the generated tree
     * 
     * @return Whether the symbol should be dropped from the tree
     */
    public boolean drop(){
        return (level == -1);
    }
    /**
     * Check whether the rule is repeating
     * 
     * @return Whether the label may match any number of repeating symbols
     */
    public boolean isRepeating(){
        return repeating;
    }

    /**
     * Check whether a label pattern for this node matches a string symbol
     * 
     * @param candidate The string to check for a match on
     * @return Whether the string matches this rule's pattern
     */
    public boolean matches(String candidate){
        return labelPattern.matcher(candidate).matches();
    }
    @Override
    public String toString(){
        if (repeating) {
            return label.toString() + "+";
        }
        return label.toString();
    }
}
