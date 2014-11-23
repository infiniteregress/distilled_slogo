package distilled_slogo.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import distilled_slogo.Constants;

public class GrammarRule implements IGrammarRule {
    private List<String> command;
    private List<List<String>> args;

    public GrammarRule (String command, String[] args) {
        this(command, Arrays.asList(args));
    }

    public GrammarRule (String command, List<String> args) {
        this.command = new ArrayList<>();
        this.command.add(command);
        this.args = new ArrayList<>();
        for (String arg : args) {
            List<String> argChoices = new ArrayList<>();
            argChoices.add(arg);
            this.args.add(argChoices);
        }
    }

    public GrammarRule (String command[], String[][] args) {
        this.command = Arrays.asList(command);
        this.args = new ArrayList<>();
        for (String[] arg : args) {
            this.args.add(Arrays.asList(arg));
        }
    }

    @Override
    public int matches (List<ISyntaxNode> nodes) {
        List<List<String>> searchPattern = new ArrayList<>();
        searchPattern.add(command);
        searchPattern.addAll(args);

        List<String> toSearch = new ArrayList<>();
        for (ISyntaxNode node : nodes) {
            toSearch.add(node.type());
        }

        for (int i = 0; i < toSearch.size(); i++) {
            if (infiniteMatches(searchPattern, toSearch, i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean infiniteMatches (List<List<String>> searchPattern, List<String> toSearch,
            int index) {
        return infiniteMatchRecurse(searchPattern, toSearch.subList(index, toSearch.size()));
    }

    public boolean infiniteMatchRecurse (List<List<String>> searchPattern,
            List<String> searchRemaining) {
        if (searchRemaining.size() == 0) {
            if (searchPattern.size() == 0
                    || (searchPattern.size() == 2 && searchPattern.get(1).contains(
                            Constants.INFINITE_MATCHING_LABEL))) {
                return true;
            }
            return false;
        }
        List<List<String>> newPattern;
        List<String> newSearch;
        if (isInfinite(searchPattern)) {
            // if we're at the wildcard and both leading elements match, we
            // keep the pattern (so that we can continue wildcard matching),
            // but we iterate to the next search element
            if (searchPattern.get(0).contains(searchRemaining.get(0))) {
                newPattern = searchPattern;
                newSearch = searchRemaining.subList(1, searchRemaining.size());
                return true && infiniteMatchRecurse(newPattern, newSearch);
            }
            // if we encounter the element right after the wildcard element,
            // then
            // we have finished matching everything for the wildcard, so we
            // should
            // set the pattern to the element after the wildcard and handle it
            // normally using non-infinite logic; searchRemaining stays the same
            // so that the next time around, the two leading elements of each
            // list
            // can be matched and iterated over in unison
            if (searchPattern.size() > 2 && searchPattern.get(2).contains(searchRemaining.get(0))) {
                newPattern = searchPattern.subList(2, searchPattern.size());
                newSearch = searchRemaining;
                return true && infiniteMatchRecurse(newPattern, newSearch);
            }
        }
        // if we're not matching a wildcard, we iterate one at a time over both
        // lists checking to see that both leading elements are equal
        else if (searchPattern.get(0).contains(searchRemaining.get(0))) {
            newPattern = searchPattern.subList(1, searchPattern.size());
            newSearch = searchRemaining.subList(1, searchRemaining.size());
            return true && infiniteMatchRecurse(newPattern, newSearch);
        }
        return false;
    }

    /**
     * To check for infinite, if any of the possible matches is the infinite
     * wildcard, then everything else will be ignored; i.e. don't include
     * anything else if you include the infinite matching wildcard
     * 
     * @param searchPattern
     *            The remaining pattern to search
     * @return Whether the second element contains the infinite matching
     *         wildcard
     */
    private static boolean isInfinite (List<List<String>> searchPattern) {
        if (searchPattern.size() > 1
                && searchPattern.get(1).contains(Constants.INFINITE_MATCHING_LABEL)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString () {
        return command.toString();
    }
}
