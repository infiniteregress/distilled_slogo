package distilled_slogo.parsing;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import distilled_slogo.Constants;

public class GrammarRuleTest {
    public void testInfiniteMatchRecurse (boolean expected, String[][] pattern, String[] search) {
        GrammarRule rule = new GrammarRule("", new ArrayList<>());
        List<List<String>> searchPattern = new ArrayList<>();
        for (String[] arg : pattern) {
            searchPattern.add(Arrays.asList(arg));
        }
        boolean result = rule.infiniteMatchRecurse(searchPattern, Arrays.asList(search));
        assertEquals(expected, result);
    }

    @Test
    public void testSunny () {
        boolean sunnyResult = true;
        String[][][] sunny = {
                { { "hi" }, { "there" }, { Constants.INFINITE_MATCHING_LABEL }, { "foo" } },
                { { "hi", "there", "there", "there", "there", "foo" } } };
        testInfiniteMatchRecurse(sunnyResult, sunny[0], sunny[1][0]);
    }

    @Test
    public void testSunnyEnd () {
        boolean sunnyEndResult = true;
        String[][][] sunnyEnd = { { { "hi" }, { "there" }, { Constants.INFINITE_MATCHING_LABEL } },
                { { "hi", "there", "there", "there", "there" } } };
        testInfiniteMatchRecurse(sunnyEndResult, sunnyEnd[0], sunnyEnd[1][0]);
    }

    @Test
    public void testExtraPattern () {
        boolean extraPatternResult = false;
        String[][][] extraPattern = {
                { { "hi" }, { "there" }, { Constants.INFINITE_MATCHING_LABEL }, { "blah" } },
                { { "hi", "there", "there", "there" } } };
        testInfiniteMatchRecurse(extraPatternResult, extraPattern[0], extraPattern[1][0]);
    }

    @Test
    public void testExtraSearch () {
        boolean extraSearchResult = false;
        String[][][] extraSearch = {
                { { "hi" }, { "there" }, { Constants.INFINITE_MATCHING_LABEL } },
                { { "hi", "there", "there", "blah" } } };
        testInfiniteMatchRecurse(extraSearchResult, extraSearch[0], extraSearch[1][0]);
    }

    @Test
    public void testJustOneRepeat () {
        boolean justOneRepeatResult = true;
        String[][][] justOneRepeat = {
                { { "hi" }, { "there" }, { Constants.INFINITE_MATCHING_LABEL } },
                { { "hi", "there" } } };
        testInfiniteMatchRecurse(justOneRepeatResult, justOneRepeat[0], justOneRepeat[1][0]);
    }

    @Test
    public void testMatches () {
        String[] args = { "there", Constants.INFINITE_MATCHING_LABEL, "blah" };
        IGrammarRule rule = new GrammarRule("hi", Arrays.asList(args));
        String[] tokens = { "o", "hai", "hi", "there", "there", "there", "blah" };
        List<ISyntaxNode> nodes = new ArrayList<>();
        for (String token : tokens) {
            nodes.add(new SyntaxNode(token, null, null));
        }
        assertEquals(2, rule.matches(nodes));
    }

    @Test
    public void testMultiMatches () {
        String[][] args = { { "there", "canhaz" }, { Constants.INFINITE_MATCHING_LABEL },
                { "blah" } };
        String[] commands = { "I", "you" };
        IGrammarRule rule = new GrammarRule(commands, args);
        String[] selfAbsorbed = { "o", "hai", "I", "there", "there", "there", "there", "blah" };
        String[] otherAbsorbed = { "o", "hai", "you", "there", "there", "there", "blah" };
        String[] selfish = { "o", "hai", "I", "canhaz", "there", "there", "blah" };
        String[] otherish = { "o", "hai", "you", "canhaz", "there", "blah" };
        String[][] allTehSentenz = { selfAbsorbed, otherAbsorbed, selfish, otherish };
        for (String[] sentenz : allTehSentenz) {
            List<ISyntaxNode> nodes = new ArrayList<>();
            for (String token : sentenz) {
                nodes.add(new SyntaxNode(token, null, null));
            }
            assertEquals(2, rule.matches(nodes));
        }
    }

    @Test
    public void testDoTimes () {
        String[][] doTimes = {
                { "DoTimes" },
                { Constants.OPENING_LIST_LABEL, Constants.VARIABLE_LABEL, Constants.CONSTANT_LABEL,
                        Constants.CLOSING_LIST_LABEL, Constants.OPENING_LIST_LABEL,
                        Constants.CONSTANT_LABEL, Constants.INFINITE_MATCHING_LABEL,
                        Constants.CLOSING_LIST_LABEL } };
        IGrammarRule doTimesRule = new GrammarRule(doTimes[0][0], doTimes[1]);
        String[] expression = { "DoTimes", Constants.OPENING_LIST_LABEL, Constants.VARIABLE_LABEL,
                Constants.CONSTANT_LABEL, Constants.CLOSING_LIST_LABEL,
                Constants.OPENING_LIST_LABEL, Constants.CONSTANT_LABEL,
                Constants.CLOSING_LIST_LABEL };
        List<ISyntaxNode> tokens = new ArrayList<>();
        for (String string : expression) {
            tokens.add(new SyntaxNode(string, null, null));
        }
        assertEquals(0, doTimesRule.matches(tokens));
    }
}
