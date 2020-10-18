package Parser.NodeBuilders;

import Exceptions.FMLParsingException;

import java.util.Deque;
import java.util.LinkedList;

import static Language.FMLGrammar.NEWLINE;

public class BuilderUtils {

    /* Does not modify tokens */
    static Deque<String> deepCopy(Deque<String> deque) {
        Deque<String> copy = new LinkedList<>();
        for (String s: deque) copy.addLast(s);
        return copy;
    }

    /**
     * Modifies tokens.
     * Removes redundant parenthesis from the front and back
     * @param tokens input tokens
     */
    static boolean stripRedundantParenthesis(Deque<String> tokens) {
        boolean removedParenthesis = false;
        while (hasRedundantParenthesis(tokens)) {
            tokens.removeFirst();
            tokens.removeLast();
            removedParenthesis = true;
        }
        return removedParenthesis;
    }

    /**
     * Does not modify tokens
     * Returns true if removing first and last parenthesis won't change semantics
     */
    static boolean hasRedundantParenthesis(Deque<String> tokens) {
        if (!(tokens.size() > 1 && tokens.getFirst().equals("(") && tokens.getLast().equals(")"))) {
            return false;
        }
        int balance = 0;
        int count = 0;
        for (String token: tokens) {
            count++;
            if (token.equals("(")) balance++;
            if (token.equals(")")) balance--;
            if (balance == 0) break;
        }
        // 0 balance was reached before all tokens were seen
        return count == tokens.size();
    }

    public static Deque<String> getBlockTokens(Deque<String> tokens) throws FMLParsingException {
        Deque<String> blockTokens = new LinkedList<>();
        // add everything until the first brace
        while (!(tokens.isEmpty() || tokens.getFirst().equals("{"))) {
            blockTokens.addLast(tokens.poll());
        }
        // if no first brace, throw error
        if (tokens.isEmpty()) {
            throw new FMLParsingException("Invalid block found - missing opening brace");
        }
        int balance = 0;
        while (!tokens.isEmpty()) {
            if (tokens.getFirst().equals("{")) balance++;
            if (tokens.getFirst().equals("}")) balance--;
            blockTokens.addLast(tokens.poll());
            if (balance == 0) break;
        }
        if (tokens.isEmpty() && balance != 0) {
            throw new FMLParsingException("Invalid block found - missing closing brace");
        }
        removeOuterBraces(blockTokens);
        stripNewlines(blockTokens);
        return blockTokens;
    }

    static void stripNewlines(Deque<String> tokens) throws FMLParsingException {
        while(!tokens.isEmpty() && tokens.getFirst().equals(NEWLINE)) {
            tokens.removeFirst();
        }
        while(!tokens.isEmpty() && tokens.getLast().equals(NEWLINE)) {
            tokens.removeLast();
        }
    }

    static void removeOuterBraces(Deque<String> tokens) throws FMLParsingException {
        if (tokens.size() > 1 && tokens.getFirst().equals("{") && tokens.getLast().equals("}")) {
            tokens.removeFirst();
            tokens.removeLast();
        }
    }
}
