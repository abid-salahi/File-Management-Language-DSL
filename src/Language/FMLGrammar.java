package Language;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Stores constants regarding the fml grammar
 */
public class FMLGrammar {

    /* Fixed Literals */
    public static String IF = "if";
    public static String ELSE = "else";
    public static String FOR = "for";
    public static String RETURN = "return";
    public static String PREPOSITION = "to";
    public static String RIGHT_ARROW = "->";
    public static String EQUALS = "=";
    public static String LIST_SYMBOL = "[]";
    public static String AND = "AND";
    public static String OR = "OR";
    public static String NOT = "NOT";
    public static String IN = "in";
    public static String TRUE = "True";
    public static String FALSE = "False";
    public static String NEWLINE = "NEWLINE";
    public static String SEMI_COLON = ";";

    /* Groups of Fixed Literals */
    public static List<String> COMMAND_STRINGS = CommandEnum.toListOfString();
    public static List<String> COMPARISON_OPERATORS = ComparisonOperator.toListOfString();
    public static List<String> LOGICAL_OPERATORS = Arrays.asList(AND, OR, NOT);
    public static List<String> ATTRIBUTES = AttributeName.toListOfString();
    public static List<String> SIZES = FileSizeUnit.toListOfString();
    public static List<String> SEPARATORS = Arrays.asList("\r\n", "{", "}", NEWLINE);

    /* Regex Patterns */
    public static Pattern USER_DEFINED_STRING_REGEX = Pattern.compile("\"(.)*\"");
    public static Pattern WILDCARD_PATH = Pattern.compile("(.)*\\*(.)*");
    public static Pattern FILE_VARIABLE_IDENTIFIER = Pattern.compile("[a-zA-Z0-9]+$");
    public static Pattern LIST_VARIABLE_IDENTIFIER = Pattern.compile("[a-zA-Z0-9]+\\[\\]$");
    public static Pattern COMMENT = Pattern.compile("\\/\\/(.*)\r\n");

    public enum ComparisonOperator {
        LESS_THAN("<"),
        GREATER_THAN(">"),
        EQUAL_TO("=="),
        GREATER_THAN_EQUAL_TO(">="),
        LESS_THAN_EQUAL_TO("<="),
        NOT_EQUAL_TO("!=");

        private String codeRep;

        ComparisonOperator(String codeRepresentation) {
            this.codeRep = codeRepresentation;
        }

        public static ComparisonOperator fromString(String name) {
            for (ComparisonOperator c : ComparisonOperator.values()) {
                if (c.toString().equals(name)) {
                    return c;
                }
            }
            return null;
        }

        public static List<String> toListOfString() {
            List<String> list = new LinkedList<>();
            for (ComparisonOperator c : ComparisonOperator.values()) {
                list.add(c.toString());
            }
            return list;
        }

        public String toString() {
            return this.codeRep;
        }
    }

    public enum CommandEnum {
        COPY("copy"),
        MOVE("move"),
        DELETE("delete"),
        RENAME("rename"),
        CREATE("create"),
        COMPRESS("compress");

        private String name;

        CommandEnum(String name) {
            this.name = name;
        }

        public static CommandEnum fromString(String name) {
            for (CommandEnum c : CommandEnum.values()) {
                if (c.toString().equals(name)) {
                    return c;
                }
            }
            return null;
        }

        public static List<String> toListOfString() {
            List<String> list = new LinkedList<>();
            for (CommandEnum c : CommandEnum.values()) {
                list.add(c.toString());
            }
            return list;
        }

        public String toString() {
            return this.name;
        }
    }

    public enum FileSizeUnit {
        B(1L, "B"),
        KB(1000L, "KB"),
        MB(1000000L, "MB"),
        GB(1000000000L, "GB"),
        TB(1000000000000L, "TB"),
        PB(1000000000000000L, "PB");

        private BigDecimal multiplier;
        private String name;
        FileSizeUnit(Long multiplier, String name) {
            this.multiplier = BigDecimal.valueOf(multiplier);
            this.name = name;
        }

        public BigDecimal getMultiplier() {
            return this.multiplier;
        }

        public static List<String> toListOfString() {
            List<String> list = new LinkedList<>();
            for (FileSizeUnit u : FileSizeUnit.values()) {
                list.add(u.toString());
            }
            return list;
        }

        public static FileSizeUnit fromString(String name) {
            for (FileSizeUnit u : FileSizeUnit.values()) {
                if (u.toString().equals(name)) {
                    return u;
                }
            }
            return null;
        }

        public String toString() {
            return this.name;
        }
    }

    public enum AttributeName {
        NAME(".name"),
        CREATED(".created"),
        MODIFIED(".modified"),
        EXTENSION(".extension"),
        PARENT(".parent"),
        SIZE(".size"),
        IS_FILE(".isFile"),
        IS_DIRECTORY(".isDirectory");

        private String name;

        AttributeName(String name) {
            this.name = name;
        }

        public static AttributeName fromString(String name) {
            for (AttributeName attributeName : AttributeName.values()) {
                if (attributeName.toString().equals(name)) {
                    return attributeName;
                }
            }
            return null;
        }

        public static List<String> toListOfString() {
            List<String> list = new LinkedList<>();
            for (AttributeName attributeName : AttributeName.values()) {
                list.add(attributeName.toString());
            }
            return list;
        }

        public String toString() {
            return this.name;
        }
    }
}