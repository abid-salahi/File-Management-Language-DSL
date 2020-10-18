package Tokenizer;

import Exceptions.FMLParsingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static Language.FMLGrammar.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static Tokenizer.Tokenizer.getTokens;
import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    /* Tokenized scripts */
    private String EMPTY = " ";
    private Queue<String> EMPTY_TOKENS = new LinkedList<>();

    private String COPY_ACTION_STATEMENT = "copy fileVariable to folderVariable\r\n";
    private Queue<String> COPY_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("copy", "fileVariable", PREPOSITION, "folderVariable"));

    private String MOVE_ACTION_STATEMENT = "move file to newFolder\r\n";
    private Queue<String> MOVE_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("move", "file", PREPOSITION, "newFolder"));

    private String DELETE_ACTION_STATEMENT = "delete fileVariable2\r\n";
    private Queue<String> DELETE_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("delete", "fileVariable2"));

    private String RENAME_ACTION_STATEMENT = "rename fileVariable2 to \"a new name\"\r\n";
    private Queue<String> RENAME_ACTION_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList("rename", "fileVariable2", PREPOSITION, "\"a new name\""));

    private String STRING_IF_STATEMENT = "if (file.name == \"myFile\") {\r\n";
    private Queue<String> STRING_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".name","==","\"myFile\"",")","{"));

    private String NUM_IF_STATEMENT = "if (file.size > 100 GB) {\r\n";
    private Queue<String> NUM_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size",">","100 GB",")","{"));

    private String NOT_IF_STATEMENT = "if (NOT notfile.isFile) {\r\n";
    private Queue<String> NOT_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(", NOT,"notfile",".isFile",")","{"));

    private String AND_IF_STATEMENT = "if(file.isDirectory AND file.created < \"9/20/2020\") {\r\n";
    private Queue<String> AND_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".isDirectory",AND,"file",".created","<","\"9/20/2020\"",")","{"));

    private String OR_IF_STATEMENT = "if(file.size >= 10MB OR file.size <= 1GB) {\r\n";
    private Queue<String> OR_IF_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(IF,"(","file",".size",">=","10MB",OR,"file",".size","<=","1GB",")","{"));


    private String ELSE_STATEMENT = "else {\r\n";
    private Queue<String> ELSE_STATEMENT_TOKENS = new LinkedList<>(Arrays.asList(ELSE,"{"));

    private String FOR_LOOP = "for(file in filesInFolder[]) {\r\n";
    private Queue<String> FOR_LOOP_TOKENS = new LinkedList<>(Arrays.asList(FOR,"(","file",IN,"filesInFolder[]",")","{"));

    private String DIRECT_DECLARATION = "filesInFolder[] = absPath -> \"path/to/*\"\r\n";
    private Queue<String> DIRECT_DECLARATION_TOKENS = new LinkedList<>(Arrays.asList("filesInFolder[]",EQUALS,"absPath",RIGHT_ARROW,"\"path/to/*\""));

    private String RELATIVE_DECLARATION = "newFolder = oldFolder\r\n";
    private Queue<String> RELATIVE_DECLARATION_TOKENS = new LinkedList<>(Arrays.asList("newFolder",EQUALS,"oldFolder"));

    private String FULL_PROGRAM = DIRECT_DECLARATION + RELATIVE_DECLARATION + FOR_LOOP + "    " + AND_IF_STATEMENT + "        " + MOVE_ACTION_STATEMENT + "        }" + "    }";
    private Queue<String> FULL_PROGRAM_TOKENS = new LinkedList<>(Arrays.asList("filesInFolder[]",EQUALS,"absPath",RIGHT_ARROW,"\"path/to/*\"",NEWLINE,
            "newFolder",EQUALS,"oldFolder",NEWLINE,
            FOR,"(","file",IN,"filesInFolder[]",")","{",NEWLINE,
            IF,"(","file",".isDirectory",AND,"file",".created","<","\"9/20/2020\"",")","{",NEWLINE,
            "move", "file", PREPOSITION, "newFolder",NEWLINE,"}","}"));

    private Queue<String> tokens;

    @AfterEach
    void clearTokens() {
        tokens.clear();
    }

    /* Valid Inputs */

    @Test
    void testTokenizeEmptyString() throws FMLParsingException {
        tokens = getTokens(EMPTY,SEPARATORS);
        assertEquals(tokens, EMPTY_TOKENS);
    }


    @Test
    void testTokenizeCopyString() throws FMLParsingException {
        tokens = getTokens(COPY_ACTION_STATEMENT,SEPARATORS);
        assertEquals(tokens, COPY_ACTION_STATEMENT_TOKENS);
    }
    @Test
    void testTokenizeMoveString() throws FMLParsingException {
        tokens = getTokens(MOVE_ACTION_STATEMENT,SEPARATORS);
        assertEquals(tokens, MOVE_ACTION_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeDeleteString() throws FMLParsingException {
        tokens = getTokens(DELETE_ACTION_STATEMENT,SEPARATORS);
        assertEquals(tokens, DELETE_ACTION_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeRenameString() throws FMLParsingException {
        tokens = getTokens(RENAME_ACTION_STATEMENT,SEPARATORS);
        assertEquals(tokens, RENAME_ACTION_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeStringfString() throws FMLParsingException {
        tokens = getTokens(STRING_IF_STATEMENT,SEPARATORS);
        assertEquals(tokens, STRING_IF_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeNumIfString() throws FMLParsingException {
        tokens = getTokens(NUM_IF_STATEMENT,SEPARATORS);
        assertEquals(tokens, NUM_IF_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeNotIfString() throws FMLParsingException {
        tokens = getTokens(NOT_IF_STATEMENT,SEPARATORS);
        assertEquals(tokens, NOT_IF_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeAndIfString() throws FMLParsingException {
        tokens = getTokens(AND_IF_STATEMENT,SEPARATORS);
        assertEquals(tokens, AND_IF_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeOrIfString() throws FMLParsingException {
        tokens = getTokens(OR_IF_STATEMENT,SEPARATORS);
        assertEquals(tokens, OR_IF_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeElseString() throws FMLParsingException {
        tokens = getTokens(ELSE_STATEMENT,SEPARATORS);
        assertEquals(tokens, ELSE_STATEMENT_TOKENS);
    }

    @Test
    void testTokenizeForLoopString() throws FMLParsingException {
        tokens = getTokens(FOR_LOOP,SEPARATORS);
        assertEquals(tokens, FOR_LOOP_TOKENS);
    }

    @Test
    void testTokenizeDirectDeclareString() throws FMLParsingException {
        tokens = getTokens(DIRECT_DECLARATION,SEPARATORS);
        assertEquals(tokens, DIRECT_DECLARATION_TOKENS);
    }

    @Test
    void testTokenizeRelativeDeclareString() throws FMLParsingException {
        tokens = getTokens(RELATIVE_DECLARATION,SEPARATORS);
        assertEquals(tokens, RELATIVE_DECLARATION_TOKENS);
    }

    @Test
    void testTokenizeFullProgramString() throws FMLParsingException {
        tokens = getTokens(FULL_PROGRAM,SEPARATORS);
        assertEquals(tokens, FULL_PROGRAM_TOKENS);
    }
    /* Invalid inputs */
}
