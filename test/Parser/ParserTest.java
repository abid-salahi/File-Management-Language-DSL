package Parser;

import Exceptions.FMLParsingException;
import Language.FMLGrammar.*;
import Parser.ASTNodes.Commands.*;
import Parser.ASTNodes.Conditions.AndCondition;
import Parser.ASTNodes.Conditions.Booleans.*;
import Parser.ASTNodes.Conditions.Negation;
import Parser.ASTNodes.Conditions.OrCondition;
import Parser.ASTNodes.Conditions.Terms.*;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.*;
import Parser.ASTNodes.Statements.Declarations.DirectDeclaration;
import Parser.ASTNodes.Statements.Declarations.Paths.FMLPath;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static Language.FMLGrammar.*;
import static org.junit.jupiter.api.Assertions.*;


class ParserTest {

    @Nested
    class BaseCaseParserTests {

        /* Tokenized scripts */

        private final Queue<String> EMPTY = new LinkedList<>();


        /* Valid Inputs Tests */

        @Test
        void testParsesEmptyTokens() throws FMLParsingException {
            Program program = Parser.parse(EMPTY);
            assertEquals(program.getStatements().size(), 0);
        }
    }

    @Nested
    class ActionParserTests {

        /* Tokenized scripts */

        private final Queue<String> COPY_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("copy", "fileVariable", "to", "folderVariable"));
        private final ActionStatement COPY_ACTION_STATEMENT_EXPECTED = new ActionStatement(new Copy("fileVariable", "folderVariable"), "fileVariable", "folderVariable", false);

        private final Queue<String> MOVE_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("move", "fileVariable2", "to", "folderVariable2"));
        private final ActionStatement MOVE_ACTION_STATEMENT_EXPECTED = new ActionStatement(new Move("fileVariable2", "folderVariable2"), "fileVariable2", "folderVariable2", false);

        private final Queue<String> DELETE_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("delete", "fileVariable2"));
        private final ActionStatement DELETE_ACTION_STATEMENT_EXPECTED = new ActionStatement(new Delete("fileVariable2"), "fileVariable2", null, false);

        private final Queue<String> RENAME_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("rename", "fileVariable2", "to", "\"a new name\""));
        private final ActionStatement RENAME_ACTION_STATEMENT_EXPECTED = new ActionStatement(new Rename("fileVariable2", "a new name"), "fileVariable2", "a new name", true);

        private final Queue<String> CREATE_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("create", "folder1"));
        private final ActionStatement CREATE_ACTION_STATEMENT_EXPECTED = new ActionStatement(new Create("folder1"), "folder1", null, false);

        private final Queue<String> COMPRESS_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("compress", "folder2"));
        private final ActionStatement COMPRESS_ACTION_STATEMENT_EXPECTED = new ActionStatement(new Compress("folder2"), "folder2", null, false);

        @Test
        void testParsesCopyActionStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(COPY_ACTION_STATEMENT_TOKENS), COPY_ACTION_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesMoveActionStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(MOVE_ACTION_STATEMENT_TOKENS), MOVE_ACTION_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesDeleteActionStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(DELETE_ACTION_STATEMENT_TOKENS), DELETE_ACTION_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesRenameActionStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(RENAME_ACTION_STATEMENT_TOKENS), RENAME_ACTION_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCreateActionStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(CREATE_ACTION_STATEMENT_TOKENS), CREATE_ACTION_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCompressActionStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(COMPRESS_ACTION_STATEMENT_TOKENS), COMPRESS_ACTION_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesMultipleActionStatement() throws FMLParsingException {
            Queue<String> tokens = concatTokens(
                    COPY_ACTION_STATEMENT_TOKENS,
                    MOVE_ACTION_STATEMENT_TOKENS,
                    COMPRESS_ACTION_STATEMENT_TOKENS
            );

            List<ActionStatement> expected = Arrays.asList(
                    COPY_ACTION_STATEMENT_EXPECTED,
                    MOVE_ACTION_STATEMENT_EXPECTED,
                    COMPRESS_ACTION_STATEMENT_EXPECTED
            );
            testStatements(tokens, expected);
        }
    }

    @Nested
    class DeclarationParserTests {

        private final Queue<String> DIRECT_DECLARATION_STRING = new LinkedList<>(Arrays.asList("file", "=", "\"/some/path\""));
        private final DirectDeclaration DIRECT_DECLARATION_STRING_EXPECTED = new DirectDeclaration("file", new FMLPath("/some/path"));

        private final Queue<String> DIRECT_DECLARATION_STRING_LIST = new LinkedList<>(Arrays.asList("file", "=", "\"/some/path/*\""));
        private final DirectDeclaration DIRECT_DECLARATION_STRING_LIST_EXPECTED = new DirectDeclaration("file", new FMLPath("/some/path/*"));

        private final Queue<String> DIRECT_DECLARATION_ARROW = new LinkedList<>(Arrays.asList("file", "=", "folder", "->", "\"/some/path\""));
        private final DirectDeclaration DIRECT_DECLARATION_ARROW_EXPECTED = new DirectDeclaration("file", new FMLPath("folder", "/some/path"));

        private final Queue<String> DIRECT_DECLARATION_ARROW_LIST = new LinkedList<>(Arrays.asList("file", "=", "folder", "->", "\"/some/path/*\""));
        private final DirectDeclaration DIRECT_DECLARATION_ARROW_LIST_EXPECTED = new DirectDeclaration("file", new FMLPath("folder", "/some/path/*"));

        @Test
        void testParsesDirectStringDeclaration() throws FMLParsingException {
            testStatement(DIRECT_DECLARATION_STRING, DIRECT_DECLARATION_STRING_EXPECTED);
        }

        @Test
        void testParsesDirectStringListDeclaration() throws FMLParsingException {
            testStatement(DIRECT_DECLARATION_STRING_LIST, DIRECT_DECLARATION_STRING_LIST_EXPECTED);
        }

        @Test
        void testParsesDirectArrowDeclaration() throws FMLParsingException {
            testStatement(DIRECT_DECLARATION_ARROW, DIRECT_DECLARATION_ARROW_EXPECTED);
        }

        @Test
        void testParsesDirectArrowListDeclaration() throws FMLParsingException {
            testStatement(DIRECT_DECLARATION_ARROW_LIST, DIRECT_DECLARATION_ARROW_LIST_EXPECTED);
        }

        @Test
        void testParsesMultipleDeclaration() throws FMLParsingException {
            Queue<String> tokens = concatTokens(
                    DIRECT_DECLARATION_STRING,
                    DIRECT_DECLARATION_STRING_LIST,
                    DIRECT_DECLARATION_ARROW_LIST,
                    DIRECT_DECLARATION_ARROW
            );

            List<Statement> expected = Arrays.asList(
                    DIRECT_DECLARATION_STRING_EXPECTED,
                    DIRECT_DECLARATION_STRING_LIST_EXPECTED,
                    DIRECT_DECLARATION_ARROW_LIST_EXPECTED,
                    DIRECT_DECLARATION_ARROW_EXPECTED
            );

            testStatements(tokens, expected);
        }

    }

    @Nested
    class ReturnParserTests {

        private final Queue<String> RETURN_TOKENS = new LinkedList<>(Arrays.asList("return"));
        private final ReturnStatement RETURN_EXPECTED = new ReturnStatement();

        @Test
        void testParsesReturnStatement() throws FMLParsingException {
            testStatement(RETURN_TOKENS, RETURN_EXPECTED);
        }

    }

    @Nested
    class IfStatementParserTests {
        //Unsure about Created & Modified date format
        private final Program TEST_PROGRAM = new Program(new LinkedList<>(Arrays.asList(new ActionStatement(new Move("fileVariable2", "folderVariable2"), "fileVariable2", "folderVariable2", false))));
        private final Comparison GT_SIZE = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(100), FileSizeUnit.KB), ComparisonOperator.GREATER_THAN);
        private final Comparison GTE_SIZE = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(250), FileSizeUnit.MB), ComparisonOperator.GREATER_THAN_EQUAL_TO);
        private final Comparison LT_SIZE = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(1), FileSizeUnit.PB), ComparisonOperator.LESS_THAN);
        private final Comparison LTE_SIZE = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(5.25), FileSizeUnit.TB), ComparisonOperator.LESS_THAN_EQUAL_TO);
        private final Comparison EQ_SIZE = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(3.5), FileSizeUnit.GB), ComparisonOperator.EQUAL_TO);
        private final Comparison NEQ_SIZE = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(1024), FileSizeUnit.B), ComparisonOperator.NOT_EQUAL_TO);
        private final Comparison NAME = new Comparison(new AttributeTerm("file", AttributeName.NAME),new StringTerm("text.txt"), ComparisonOperator.EQUAL_TO);
        private final Comparison EXTENSION = new Comparison(new AttributeTerm("file", AttributeName.EXTENSION),new StringTerm("txt"), ComparisonOperator.EQUAL_TO);
        private final Comparison ISFILE = new Comparison(new AttributeTerm("file", AttributeName.IS_FILE),new BooleanTerm(new True()), ComparisonOperator.EQUAL_TO);
        private final Comparison ISDIRECTORY = new Comparison(new AttributeTerm("file", AttributeName.IS_DIRECTORY),new BooleanTerm(new False()), ComparisonOperator.EQUAL_TO);
        private final Comparison PARENT = new Comparison(new AttributeTerm("file", AttributeName.PARENT),new StringTerm("C:\\Users\\User\\Documents"), ComparisonOperator.EQUAL_TO);
        private final Comparison CREATED = new Comparison(new AttributeTerm("file", AttributeName.CREATED),new StringTerm("9/20/20"), ComparisonOperator.EQUAL_TO);
        private final Comparison MODIFIED = new Comparison(new AttributeTerm("file", AttributeName.MODIFIED),new StringTerm("10/10/20"), ComparisonOperator.EQUAL_TO);

        private final Queue<String> GT_SIZE_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size",">","100 KB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement GT_SIZE_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(GT_SIZE))))))),TEST_PROGRAM);

        private final Queue<String> GTE_SIZE_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size",">=","250 MB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement GTE_SIZE_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(GTE_SIZE))))))),TEST_PROGRAM);

        private final Queue<String> LT_SIZE_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size","<","1 PB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement LT_SIZE_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(LT_SIZE))))))),TEST_PROGRAM);

        private final Queue<String> LTE_SIZE_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size","<=","5.25 TB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement LTE_SIZE_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(LTE_SIZE))))))),TEST_PROGRAM);

        private final Queue<String> EQ_SIZE_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size","==","3.5 GB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement EQ_SIZE_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(EQ_SIZE))))))),TEST_PROGRAM);

        private final Queue<String> NEQ_SIZE_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size","!=","1024 B",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement NEQ_SIZE_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(NEQ_SIZE))))))),TEST_PROGRAM);

        private final Queue<String> NAME_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".name","==","\"text.txt\"",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement NAME_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(NAME))))))),TEST_PROGRAM);

        private final Queue<String> EXTENSION_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".extension","==","\"txt\"",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement EXTENSION_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(EXTENSION))))))),TEST_PROGRAM);

        private final Queue<String> ISFILE_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".isFile","==","True",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement ISFILE_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(ISFILE))))))),TEST_PROGRAM);

        private final Queue<String> ISDIRECTORY_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".isDirectory","==","False",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement ISDIRECTORY_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(ISDIRECTORY))))))),TEST_PROGRAM);

        private final Queue<String> PARENT_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".parent","==","\"C:\\Users\\User\\Documents\"",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement PARENT_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(PARENT))))))),TEST_PROGRAM);

        private final Queue<String> CREATED_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".created","==","\"9/20/20\"",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement CREATED_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(CREATED))))))),TEST_PROGRAM);

        private final Queue<String> MODIFIED_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".modified","==","\"10/10/20\"",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement MODIFIED_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(MODIFIED))))))),TEST_PROGRAM);

        @Test
        void testParsesGreaterThanIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(GT_SIZE_IF_STATEMENT_TOKENS), GT_SIZE_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesGreaterThanEqualIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(GTE_SIZE_IF_STATEMENT_TOKENS), GTE_SIZE_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesLessThanIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(LT_SIZE_IF_STATEMENT_TOKENS), LT_SIZE_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesLessThanEqualIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(LTE_SIZE_IF_STATEMENT_TOKENS), LTE_SIZE_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesEqualIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(EQ_SIZE_IF_STATEMENT_TOKENS), EQ_SIZE_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesNotEqualIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(NEQ_SIZE_IF_STATEMENT_TOKENS), NEQ_SIZE_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCheckNameIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(NAME_IF_STATEMENT_TOKENS), NAME_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCheckExtensionIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(EXTENSION_IF_STATEMENT_TOKENS), EXTENSION_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCheckIsFileIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(ISFILE_IF_STATEMENT_TOKENS), ISFILE_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCheckIsDirectoryIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(ISDIRECTORY_IF_STATEMENT_TOKENS), ISDIRECTORY_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCheckParentIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(PARENT_IF_STATEMENT_TOKENS), PARENT_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCheckIsCreatedIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(CREATED_IF_STATEMENT_TOKENS), CREATED_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesCheckIsModifiedIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(MODIFIED_IF_STATEMENT_TOKENS), MODIFIED_IF_STATEMENT_EXPECTED);
        }
    }

    @Nested
    class testParsesMultipleIfStatement {

        private final Program TEST_PROGRAM = new Program(new LinkedList<>(Arrays.asList(new ActionStatement(new Move("fileVariable2", "folderVariable2"), "fileVariable2", "folderVariable2", false))));
        private final Comparison FILE_GT_100KB = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(100), FileSizeUnit.KB), ComparisonOperator.GREATER_THAN);
        private final Comparison FILE_LEQ_1TB = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(1), FileSizeUnit.TB), ComparisonOperator.LESS_THAN_EQUAL_TO);
        private final Comparison FILE_NEQ_50GB = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(50), FileSizeUnit.GB), ComparisonOperator.NOT_EQUAL_TO);

        private final Queue<String> AND_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size",">","100 KB",AND,"file",".size","<=","1 TB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement AND_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(FILE_GT_100KB),new Negation(FILE_LEQ_1TB))))))),TEST_PROGRAM);

        private final Queue<String> OR_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size",">","100 KB",OR,"file",".size","<=","1 TB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement OR_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(FILE_GT_100KB)))),new AndCondition(new LinkedList<>(Arrays.asList(new Negation(FILE_LEQ_1TB))))))),TEST_PROGRAM);

        private final Queue<String> NOT_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(",NOT,"file",".size",">","100 KB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement NOT_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(new Negation(FILE_GT_100KB)))))))),TEST_PROGRAM);

        private final Queue<String> NOTANDOR_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","(","(",NOT,"file",".size",">","100 KB",")",OR,"file",".size","<=","1 TB",")",AND,"file",".size","!=","50 GB",")","{",NEWLINE,"move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final IfStatement NOTANDOR_IF_STATEMENT_EXPECTED = new IfStatement(
                new OrCondition(
                        new AndCondition(Arrays.asList(
                                new Negation(
                                        new BooleanCondition(
                                                new OrCondition(Arrays.asList(
                                                        new AndCondition(new Negation(new Negation(FILE_GT_100KB))),
                                                        new AndCondition(new Negation(FILE_LEQ_1TB))
                                                ))
                                        )
                                ),
                                new Negation(FILE_NEQ_50GB)
                        ))
                ),
                TEST_PROGRAM
        );

        @Test
        void testParsesAndIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(AND_IF_STATEMENT_TOKENS), AND_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesOrIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(OR_IF_STATEMENT_TOKENS), OR_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesNotIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(NOT_IF_STATEMENT_TOKENS), NOT_IF_STATEMENT_EXPECTED);
        }

        @Test
        void testParsesNotAndOrIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(NOTANDOR_IF_STATEMENT_TOKENS), NOTANDOR_IF_STATEMENT_EXPECTED);
        }
    }

    @Nested
    class testParsesNestedIfStatement {
        private final Program TEST_PROGRAM_NESTED = new Program(new LinkedList<>(Arrays.asList(new ActionStatement(new Move("fileVariable2", "folderVariable2"), "fileVariable2", "folderVariable2", false))));
        private final Comparison TEST_COMPARISON = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(100), FileSizeUnit.KB), ComparisonOperator.GREATER_THAN);
        private final Comparison TEST_COMPARISON_2 = new Comparison(new AttributeTerm("file", AttributeName.SIZE),new NumberTerm(BigDecimal.valueOf(1), FileSizeUnit.TB), ComparisonOperator.LESS_THAN_EQUAL_TO);
        private final Program TEST_PROGRAM = new Program(new LinkedList<>(Arrays.asList(new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(TEST_COMPARISON_2))))))),TEST_PROGRAM_NESTED))));

        private final Queue<String> NESTED_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("if","(","file",".size",">","100 KB",")","{","NEWLINE","if","(","file",".size","<=","1 TB",")","{", "move", "fileVariable2", "to", "folderVariable2","NEWLINE","}","NEWLINE","}"));
        private final IfStatement NESTED_IF_STATEMENT_EXPECTED = new IfStatement(new OrCondition(new LinkedList<>(Arrays.asList(new AndCondition(new LinkedList<>(Arrays.asList(new Negation(TEST_COMPARISON))))))),TEST_PROGRAM);

        @Test
        void testParsesNestedIfStatement() throws FMLParsingException {
            testStatement(new LinkedList<>(NESTED_IF_STATEMENT_TOKENS), NESTED_IF_STATEMENT_EXPECTED);
        }
    }

    @Nested
    class testParsesForLoop {
        private final Program TEST_PROGRAM = new Program(new LinkedList<>(Arrays.asList(new ActionStatement(new Move("fileVariable2", "folderVariable2"), "fileVariable2", "folderVariable2", false))));

        private final Queue<String> FOR_LOOP_TOKENS = new LinkedList<>(Arrays.asList(FOR,"(","file",IN,"filesToCheck[]",")","{",NEWLINE, "move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}"));
        private final ForLoop FOR_LOOP_EXPECTED = new ForLoop("filesToCheck[]",TEST_PROGRAM,"file");

        @Test
        void testParsesForLoop() throws FMLParsingException {
            testStatement(new LinkedList<>(FOR_LOOP_TOKENS), FOR_LOOP_EXPECTED);
        }
    }

//    @Test
//    public void testParsesMultipleForLoop() throws FMLParsingException {
//        // TODO
//        throw new NotImplementedException();
//    }
//
    @Nested
    class testParsesNestedForLoop {
        private final Program TEST_PROGRAM_NESTED = new Program(new LinkedList<>(Arrays.asList(new ActionStatement(new Move("fileVariable2", "folderVariable2"), "fileVariable2", "folderVariable2", false))));
        private final Program TEST_PROGRAM = new Program(new LinkedList<>(Arrays.asList(new ForLoop("filesToCheck[]",TEST_PROGRAM_NESTED,"file"))));

        private final Queue<String> FOR_LOOP_TOKENS = new LinkedList<>(Arrays.asList(FOR,"(","file2",IN,"filesInFolder[]",")","{",NEWLINE,FOR,"(","file",IN,"filesToCheck[]",")","{",NEWLINE, "move", "fileVariable2", "to", "folderVariable2",NEWLINE,"}",NEWLINE,"}"));
        private final ForLoop FOR_LOOP_EXPECTED = new ForLoop("filesInFolder[]",TEST_PROGRAM,"file2");

        @Test
        void testParsesNestedForLoop() throws FMLParsingException {
            testStatement(new LinkedList<>(FOR_LOOP_TOKENS), FOR_LOOP_EXPECTED);
        }
    }

//    @Test
//    public void testParsesFullProgram() throws FMLParsingException {
//        // TODO
//        throw new NotImplementedException();
//    }

    /* Invalid inputs */

    /* Helpers */

    private static Queue<String> concatTokens(Queue<String>... tokenCollection) {
        LinkedList<String> concatenated = new LinkedList<>();
        for (Queue<String> tokens: tokenCollection) {
            concatenated.addAll(tokens);
            concatenated.add(NEWLINE);
        }
        return concatenated;
    }

    private static void testStatement(Queue<String> tokens, Statement expected) throws FMLParsingException {
        testStatements(tokens, Collections.singletonList(expected));
    }

    private static void testStatements(Queue<String> tokens, List<? extends Statement> expected) throws FMLParsingException {
        Program program = Parser.parse(tokens);
        assertEquals(program.getStatements().size(), expected.size());
        for (int i=0; i < expected.size(); i++) {
            assertEquals(expected.get(i), program.getStatements().get(i));
        }
    }
}
