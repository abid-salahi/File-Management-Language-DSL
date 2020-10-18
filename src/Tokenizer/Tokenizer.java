package Tokenizer;

import Exceptions.FMLParsingException;
import Util.Logger;

import java.util.*;

import static Language.FMLGrammar.*;

public class Tokenizer {

    static Logger logger = Logger.get();

    private static final String separator = "_separator_";
    public static List<String> PREPOSITIONS = Arrays.asList(addSpaces(PREPOSITION));
    public static List<String> DECLARATIONS = Arrays.asList(EQUALS, RIGHT_ARROW);
    public static List<String> IF_STATEMENTS = Arrays.asList("(", ")", addSpaces(AND), addSpaces(OR), NOT);
    public static List<String> FOR_LOOPS = Arrays.asList("(", ")", addSpaces(IN));
    private static Queue<String> tokens;

    /**
     * Get the contents of the .fml script as a queue of tokens
     *
     * @param data contents of a .fml script to be tokenized
     * @return tokenized .fml script
     */
    public static Queue<String> getTokens(String data, List<String> Separators) throws FMLParsingException {
        logger.log("Starting script tokenization");
        data = removeComments(data);
        tokens = new LinkedList<>();
        String[] statements;
        for (String s : Separators) {
            data = data.replace(s, separator + s + separator);
        }
        data = data.replace(separator + "\r\n" + separator, separator + NEWLINE + separator);
        data = data.replace(separator + separator, separator);
        if (data.length() > 0 && data.startsWith(separator)) {
            data = data.substring(separator.length()); // without first character
        }
        statements = data.split(separator);
        for (String st : statements) {
            st = st.trim();
            if (Separators.contains(st)) {
                tokens.add(st);
            } else if (st.startsWith(RETURN)) {
                tokens.add(st);
            } else if (st.startsWith(IF) || st.startsWith(ELSE)) {
                tokens.addAll(tokenIf(st));
            } else if (st.startsWith(FOR)) {
                tokens.addAll(tokenFor(st));
            } else {
                boolean declaration = true;
                for (String command : COMMAND_STRINGS) {
                    if (st.startsWith(command+" ")) {
                        tokens.addAll(tokenAction(st, command+" "));
                        declaration = false;
                        break;
                    }
                }
                if (!st.equals("") && declaration) {
                    tokens.addAll(tokenDeclaration(st));
                }
            }
        }
        logger.log("Script tokenization complete");
        return removeSemiColons(tokens);
    }

    private static Queue<String> tokenIf(String data) {
        Queue<String> tokenized = new LinkedList<>();
        if (data.startsWith(IF)) {
            data = separator+IF+separator+data.substring(IF.length());
        } else if (data.startsWith(ELSE)) {
            data = separator+ELSE+separator+data.substring(ELSE.length());
        }
        for (String i : IF_STATEMENTS) {
            data = data.replace(i, separator + i + separator);
        }
        for (String i : COMPARISON_OPERATORS) {
            data = data.replace(i, separator + i + separator);
            if (i == ComparisonOperator.LESS_THAN.toString() || i == ComparisonOperator.GREATER_THAN.toString()) {
                data = data.replace(i + separator + "=", i + "=" + separator);
            }
        }
        for (String i : ATTRIBUTES) {
            data = data.replace(i, separator + i + separator);
        }
        for (String t : data.split(separator)) {
            t = t.trim();
            if (!t.isEmpty()) {
                tokenized.add(t);
            }
        }
        return tokenized;
    }

    private static Queue<String> tokenFor(String data) {
        Queue<String> tokenized = new LinkedList<>();
        if (data.startsWith(FOR)) {
            data = separator+FOR+separator+data.substring(FOR.length());
        }
        for (String f : FOR_LOOPS) {
            data = data.replace(f, separator + f + separator);
        }
        for (String t : data.split(separator)) {
            t = t.trim();
            if (!t.isEmpty()) {
                tokenized.add(t);
            }
        }
        return tokenized;
    }

    private static Queue<String> tokenAction(String data, String command) {
        Queue<String> tokenized = new LinkedList<>();
        for (String s : PREPOSITIONS) {
            data = data.replace(s, separator + s + separator);
        }
        data = data.replace(command, separator + command + separator);
        for (String t : data.split(separator)) {
            t = t.trim();
            if (!t.isEmpty()) {
                tokenized.add(t);
            }
        }
        return tokenized;
    }

    private static Queue<String> tokenDeclaration(String data) {
        Queue<String> tokenized = new LinkedList<>();
        for (String d : DECLARATIONS) {
            data = data.replace(d, separator + d + separator);
        }
        for (String t : data.split(separator)) {
            t = t.trim();
            if (!t.isEmpty()) {
                tokenized.add(t);
            }
        }
        return tokenized;
    }

    private static String addSpaces(String literal) {
        return " "+literal+" ";
    }

    private static Queue<String> removeSemiColons(Queue<String> tokens) {
        LinkedList<String> cleaned = new LinkedList<>();
        for (String token: tokens) {
            if (token.contains(SEMI_COLON)) {
                cleaned.addLast(token.replaceAll(SEMI_COLON, ""));
            } else {
                cleaned.add(token);
            }
        }
        return cleaned;
    }

    private static String removeComments(String data) {
        return COMMENT.matcher(data.trim() + "\r\n").replaceAll("\r\n").trim();
    }
}
