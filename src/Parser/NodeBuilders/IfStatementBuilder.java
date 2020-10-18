package Parser.NodeBuilders;

import Exceptions.FMLParsingException;
import Language.FMLGrammar;
import Parser.ASTNodes.Conditions.OrCondition;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.IfStatement;

import java.util.Deque;
import java.util.LinkedList;

import static Language.FMLGrammar.IF;
import static Parser.NodeBuilders.BuilderUtils.getBlockTokens;
import static Parser.NodeBuilders.BuilderUtils.hasRedundantParenthesis;
import static Parser.Parser.parse;

public class IfStatementBuilder {

    public static IfStatement build(Deque<String> ifTokens) throws FMLParsingException {
        if (!ifTokens.getFirst().equals(IF)) {
            throw new FMLParsingException("Invalid conditional - missing \"If\"");
        }
        ifTokens.removeFirst(); // ignore "if"

        Deque<String> conditionTokens = getConditionTokens(ifTokens);
        OrCondition condition = ConditionStatementBuilder.buildCondition(conditionTokens);

        Deque<String> ifProgramTokens = getBlockTokens(ifTokens);
        Program ifProgram = parse(ifProgramTokens);

        if (!ifTokens.isEmpty() && ifTokens.getFirst().equals(FMLGrammar.ELSE)) {
            ifTokens.removeFirst(); // ignore "else"
            Deque<String> elseProgramTokens = getBlockTokens(ifTokens);
            Program elseProgram = parse(elseProgramTokens);
            return new IfStatement(condition,ifProgram,elseProgram);
        }
        return new IfStatement(condition, ifProgram);
    }

    private static Deque<String> getConditionTokens(Deque<String> ifTokens) throws FMLParsingException {
        Deque<String> conditionTokens = new LinkedList<>();
        while(!(ifTokens.isEmpty() || ifTokens.getFirst().equals("{"))) {
            conditionTokens.addLast(ifTokens.poll());
        }
        if (!hasRedundantParenthesis(conditionTokens)) {
            throw new FMLParsingException("Invalid if-statement: missing parenthesis around condition");
        }
        return conditionTokens;
    }
}
