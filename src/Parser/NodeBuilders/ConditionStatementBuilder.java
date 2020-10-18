package Parser.NodeBuilders;

import Exceptions.FMLParsingException;
import Parser.ASTNodes.Conditions.AndCondition;
import Parser.ASTNodes.Conditions.Booleans.*;
import Parser.ASTNodes.Conditions.Negation;
import Parser.ASTNodes.Conditions.OrCondition;
import Parser.ASTNodes.Conditions.Terms.*;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static Language.FMLGrammar.*;
import static Parser.NodeBuilders.BuilderUtils.stripRedundantParenthesis;
import static Util.StringUtil.removeEscapedQuotes;

class ConditionStatementBuilder {

    /* Modifies tokens */
    static OrCondition buildCondition(Deque<String> condTokens) throws FMLParsingException {
        return buildOrCondition(condTokens);
    }

    /* Modifies tokens */
    private static OrCondition buildOrCondition(Deque<String> tokens) throws FMLParsingException {
        stripRedundantParenthesis(tokens);
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Invalid OR Condition found");
        }
        List<AndCondition> innerConditions = new LinkedList<>();
        while(!tokens.isEmpty()) {
            if (tokens.getFirst().equals(OR)) {
                tokens.removeFirst();
                continue;
            }
            Deque<String> andConditionTokens = getAndConditionTokens(tokens);
            if (andConditionTokens.size() > 0) {
                innerConditions.add(buildAndCondition(andConditionTokens));
            }
        }
        return new OrCondition(innerConditions);
    }

    private static Deque<String> getAndConditionTokens(Deque<String> tokens) throws FMLParsingException {
        Deque<String> andConditionTokens = new LinkedList<>();
        if (tokens.isEmpty() || tokens.getFirst().equals(OR)) {
            return andConditionTokens;
        }

        int balance = 0;
        do {
            if (tokens.getFirst().equals("(")) balance++;
            if (tokens.getFirst().equals(")")) balance--;
            andConditionTokens.addLast(tokens.poll());
        } while ((!tokens.isEmpty() && !tokens.getFirst().equals(OR)) || (balance !=0));
        return andConditionTokens;
    }

    /* Modifies tokens */
    private static AndCondition buildAndCondition(Deque<String> tokens) throws FMLParsingException {
        stripRedundantParenthesis(tokens);
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Invalid AND Condition found");
        }
        List<Negation> innerConditions = new LinkedList<>();
        while(!tokens.isEmpty()) {
            if (tokens.getFirst().equals(AND)) {
                tokens.removeFirst();
                continue;
            }
            Deque<String> negationTokens = getNegationTokens(tokens);
            if (negationTokens.size() > 0) {
                innerConditions.add(buildNegation(negationTokens));
            }
        }
        return new AndCondition(innerConditions);
    }

    private static Deque<String> getNegationTokens(Deque<String> tokens) throws FMLParsingException {
        Deque<String> negationTokens = new LinkedList<>();
        if (tokens.isEmpty() || tokens.getFirst().equals(AND)) {
            return negationTokens;
        }

        int balance = 0;
        do {
            if (tokens.getFirst().equals("(")) balance++;
            if (tokens.getFirst().equals(")")) balance--;
            negationTokens.addLast(tokens.poll());
        } while ((!tokens.isEmpty() && !tokens.getFirst().equals(AND)) || (balance !=0));
        return negationTokens;
    }

    /* Modifies tokens */
    private static Negation buildNegation(Deque<String> tokens) throws FMLParsingException  {
        stripRedundantParenthesis(tokens);
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Invalid Negation found");
        }
        if (tokens.getFirst().equals(NOT)) {
            tokens.removeFirst(); // ignore 'NOT'
            Negation innerNegation = buildNegation(tokens);
            return new Negation(innerNegation);
        } else {
            FMLBoolean bool = buildBoolean(tokens, false);
            return new Negation(bool);
        }
    }

    /* Modifies tokens */
    private static FMLBoolean buildBoolean(Deque<String> tokens, boolean isNestedBoolean) throws FMLParsingException  {
        boolean hadParenthesis = stripRedundantParenthesis(tokens);
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Invalid Boolean found");
        }
        if (tokens.getFirst().equalsIgnoreCase(TRUE) && tokens.size() == 1) {
            return new True();
        } else if (tokens.getFirst().equalsIgnoreCase(FALSE) && tokens.size() == 1) {
            return new False();
        } else if (containsNonNestedLogicalOperation(tokens)) {
            OrCondition cond = buildCondition(tokens);
            return new BooleanCondition(cond);
        }
        Comparison comparison = tryBuildingComparison(tokens);
        if (comparison != null) {
            return comparison;
        }
        throw new FMLParsingException("Invalid Boolean found");
    }

    // call stripRedundantParenthesis before calling this
    private static boolean containsNonNestedLogicalOperation(Deque<String> tokens) throws FMLParsingException {
        int balance = 0;
        for (String token: tokens) {
            if (token.equals("(")) balance++;
            if (token.equals(")")) balance--;
            if (LOGICAL_OPERATORS.contains(token) && balance == 0) return true;
        }
        return false;
    }

    /* Does not modify tokens */
    private static Comparison tryBuildingComparison(Deque<String> tokens) throws FMLParsingException  {
        try {
            Deque<String> copy = BuilderUtils.deepCopy(tokens);
            Comparison comparison = buildComparison(copy);
            tokens.clear();
            tokens.addAll(copy);
            return comparison;
        } catch (Exception e) {
            return null;
        }
    }

    /* Modifies tokens */
    private static Comparison buildComparison(Deque<String> tokens) throws FMLParsingException  {
        stripRedundantParenthesis(tokens);
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Invalid Comparison found");
        }
        Term term1 = buildTerm(tokens);
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Expected comparison term but did not find any");
        }
        ComparisonOperator op = buildComparisonOperator(tokens);
        Term term2 = buildTerm(tokens);

        if (!tokens.isEmpty()) {
            throw new FMLParsingException("Comparison has extra tokens");
        }
        return new Comparison(term1, term2, op);
    }

    /* Modifies tokens */
    private static Term buildTerm(Deque<String> tokens) throws FMLParsingException {
        Deque<String> termTokens = getTermTokens(tokens);
        try { return buildAttributeTerm(BuilderUtils.deepCopy(termTokens)); } catch (Exception ignored) {}
        try { return buildNumberTerm(BuilderUtils.deepCopy(termTokens)); } catch (Exception ignored) {}
        try { return buildStringTerm(BuilderUtils.deepCopy(termTokens)); } catch (Exception ignored) {}
        try { return new BooleanTerm(buildBoolean(BuilderUtils.deepCopy(termTokens), true));} catch (Exception ignored) {}
        throw new FMLParsingException("Invalid Term found");
    }

    /* Modifies tokens */
    private static ComparisonOperator buildComparisonOperator(Deque<String> tokens) throws FMLParsingException {
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Invalid ComparisonOperator found");
        }
        ComparisonOperator op = ComparisonOperator.fromString(tokens.poll());
        if (op == null) {
            throw new FMLParsingException("Invalid ComparisonOperator found");
        }
        return op;
    }

    /* Modifies tokens */
    private static AttributeTerm buildAttributeTerm(Deque<String> tokens) throws FMLParsingException {
        stripRedundantParenthesis(tokens);
        if (tokens.size() != 2) {
            throw new FMLParsingException("Invalid AttributeTerm found");
        }
        String identifier = tokens.poll();
        AttributeName name = AttributeName.fromString(tokens.poll());

        boolean identifierIsCorrect = FILE_VARIABLE_IDENTIFIER.matcher(identifier).matches();
        boolean nameIsCorrect = name != null;
        if (identifierIsCorrect && nameIsCorrect) {
            return new AttributeTerm(identifier, name);
        }
        throw new FMLParsingException("Invalid AttributeTerm found");
    }

    /* Modifies tokens */
    private static NumberTerm buildNumberTerm(Deque<String> tokens) throws FMLParsingException {
        stripRedundantParenthesis(tokens);
        if (tokens.size() != 1) {
            throw new FMLParsingException("Invalid NumberTerm found");
        }

        String[] parts = tokens.getFirst().split("[ ]");
        if (parts.length == 0 || parts.length > 2) {
            throw new FMLParsingException("Invalid NumberTerm found");
        }

        BigDecimal num;
        try {
            num = new BigDecimal(parts[0]);
        } catch (Exception e) {
            throw new FMLParsingException("Invalid NumberTerm found", e);
        }

        if (parts.length == 2) {
            FileSizeUnit unit = FileSizeUnit.fromString(parts[1].trim());
            if (unit == null) {
                throw new FMLParsingException("Invalid NumberTerm found");
            }
            return new NumberTerm(num, unit);
        }

        return new NumberTerm(num);
    }

    /* Modifies tokens */
    private static StringTerm buildStringTerm(Deque<String> tokens) throws FMLParsingException {
        stripRedundantParenthesis(tokens);
        if (tokens.size() == 1 && USER_DEFINED_STRING_REGEX.matcher(tokens.getFirst()).matches()) {
            return new StringTerm(removeEscapedQuotes(tokens.getFirst()));
        }
        throw new FMLParsingException("Invalid StringTerm found");
    }

    /* Modifies tokens */
    private static LinkedList<String> getTermTokens(Deque<String> tokens) throws FMLParsingException {
        if (tokens.isEmpty() || COMPARISON_OPERATORS.contains(tokens.getFirst())) {
            throw new FMLParsingException("Expected comparison term but did not find any");
        }
        LinkedList<String> termTokens = new LinkedList<>();
        int balance = 0;
        do {
            if (tokens.getFirst().equals("(")) balance++;
            if (tokens.getFirst().equals(")")) balance--;
            termTokens.addLast(tokens.poll());
        } while (!tokens.isEmpty() && (!COMPARISON_OPERATORS.contains(tokens.getFirst()) || balance != 0));

        if (balance != 0) {
            throw new FMLParsingException("Expected comparison term but did not find any");
        }
        return termTokens;
    }
}
