package Parser;

import Exceptions.FMLParsingException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.*;
import Parser.ASTNodes.Statements.Declarations.DeclarationStatement;
import Parser.NodeBuilders.*;
import Util.Logger;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static Language.FMLGrammar.*;

/**
 * Transforms a tokenized FML script into a Program
 */
public class Parser {

    static Logger logger = Logger.get();

    /**
     * Converts a collection of tokens into an AST (Program) that can be evaluated
     *
     * @param tokens tokens of the .fml script in order
     * @return the parsed Program
     */
    public static Program parse(Queue<String> tokens) throws FMLParsingException {
        logger.log("Starting program parsing");
        List<Statement> statements = new LinkedList<>();
        Deque<String> tokensDeque = new LinkedList<>(tokens);

        try {
            while (!tokensDeque.isEmpty()) {
                if (nextIsActionStatement(tokensDeque)) {
                    statements.add(buildActionStatement(tokensDeque));
                } else if (nextIsIfStatement(tokensDeque)) {
                    statements.add(buildIfStatement(tokensDeque));
                } else if (nextIsForLoop(tokensDeque)) {
                    statements.add(buildForLoop(tokensDeque));
                } else if (nextIsDeclarationStatement(tokensDeque)) {
                    statements.add(buildDeclarationStatement(tokensDeque));
                } else if (nextIsReturnStatement(tokensDeque)) {
                    statements.add(buildReturnStatement(tokensDeque));
                } else if (nextIsNewline(tokensDeque)) {
                    tokensDeque.remove();
                } else {
                    throw new Exception("Illegal Statement: " + getNextLine(tokensDeque));
                }
            }
        } catch (Exception e) {
            throw new FMLParsingException("Failed to parse script", e);
        }

        logger.log("Program parsing complete");
        return new Program(statements);
    }

    /**
     * Returns true if the next set of tokens describe an ActionStatement.
     * Tokens queue remains unmodified at the end of the method.
     */
    private static boolean nextIsActionStatement(Deque<String> tokens) {
        String next = tokens.peek();
        return (next != null && COMMAND_STRINGS.contains(next));
    }

    /**
     * Returns true if the next set of tokens describe an IfStatement.
     * Tokens queue remains unmodified at the end of the method.
     */
    private static boolean nextIsIfStatement(Deque<String> tokens) {
        String next = tokens.peek();
        return (next != null && next.equals(IF));
    }

    /**
     * Returns true if the next set of tokens describe a ForLoop.
     * Tokens queue remains unmodified at the end of the method.
     */
    private static boolean nextIsForLoop(Deque<String> tokens) {
        String next = tokens.peek();
        return (next != null && next.equals(FOR));
    }

    /**
     * Returns true if the next set of tokens describe a DeclarationStatement.
     * Tokens queue remains unmodified at the end of the method.
     */
    private static boolean nextIsDeclarationStatement(Deque<String> tokens) {
        String first = tokens.poll();
        String second = tokens.peek();
        boolean isDeclaration = second != null && second.equals(EQUALS);
        tokens.addFirst(first);
        return isDeclaration;
    }

    private static boolean nextIsReturnStatement(Deque<String> tokens) {
        String next = tokens.peek();
        return (next != null && next.equals(RETURN));
    }

    /**
     * Returns true if the next token identifies a new line
     */
    private static boolean nextIsNewline(Deque<String> tokens) {
        String next = tokens.peek();
        return (next != null && next.equals(NEWLINE));
    }

    private static ActionStatement buildActionStatement(Deque<String> tokens) throws FMLParsingException {
        logger.log("Parsing action");
        String command = tokens.poll();
        String targetIdentifier = tokens.poll();
        String destination = null;
        if (tokens.peek() != null && tokens.peek().equals(PREPOSITION)) {
            tokens.remove(); // ignore the preposition
            destination = tokens.poll();
        }
        return ActionStatementBuilder.build(command, targetIdentifier, destination);
    }

    private static DeclarationStatement buildDeclarationStatement(Deque<String> tokens) throws FMLParsingException {
        logger.log("Parsing declaration");
        Deque<String> declarationTokens = getLineTokens(tokens);
        return DeclarationStatementBuilder.build(declarationTokens);
    }

    private static ReturnStatement buildReturnStatement(Deque<String> tokens) {
        logger.log("Parsing return");
        tokens.poll();
        return new ReturnStatement();
    }

    private static IfStatement buildIfStatement(Deque<String> tokens) throws FMLParsingException {
        logger.log("Parsing if statement");
        Deque<String> ifStatementTokens = BuilderUtils.getBlockTokens(tokens);
        if (!tokens.isEmpty() && tokens.peek().equals(ELSE)) {
            ifStatementTokens.addAll(BuilderUtils.getBlockTokens(tokens));
        }
        return IfStatementBuilder.build(ifStatementTokens);
    }

    private static ForLoop buildForLoop(Deque<String> tokens) throws FMLParsingException {
        logger.log("Parsing for loop");
        Deque<String> forLoopTokens = BuilderUtils.getBlockTokens(tokens);
        return ForLoopBuilder.build(forLoopTokens);
    }

    private static String getNextLine(Deque<String> tokens) {
        StringBuilder nextLine = new StringBuilder();
        while (!tokens.isEmpty()) {
            String token = tokens.poll();
            nextLine.append(token);
            nextLine.append(" ");
            if (token.equals(NEWLINE)) {
                break;
            }
        }
        return nextLine.toString().trim();
    }

    private static Deque<String> getLineTokens(Deque<String> tokens) {
        Deque<String> lineTokens = new LinkedList<>();
        while (!(tokens.isEmpty() || tokens.getFirst().equals(NEWLINE))) {
            String token = tokens.poll();
            lineTokens.addLast(token);
        }
        return lineTokens;
    }

}
