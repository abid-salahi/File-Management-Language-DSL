package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import TestUtils.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteTest {

    private static Delete deleteCommand;

    private static Program mockProgram;
    private static FileVariable mockFileVariable;

    private final static String TEST_VAR_NAME = "testVar";
    private final static String PATH_TO_TEST_DIR = "test/TestFolder";
    private final static String PATH_TO_FILE = PATH_TO_TEST_DIR+"/testFile.txt";

    @BeforeAll
    public static void setUp() throws Exception {
        mockProgram = mock(Program.class);
        mockFileVariable = mock(FileVariable.class);
    }

    @BeforeEach
    public void prepareTestEnv() throws Exception {
        deleteCommand = new Delete(TEST_VAR_NAME);
        // Create Test Directory
        TestUtils.createFolderAtPath(PATH_TO_TEST_DIR);
        // Create file to delete
        TestUtils.createFileWithPath(PATH_TO_FILE);
    }

    @Test
    public void deleteFileTest() throws Exception {
        File fileToDelete = new File(PATH_TO_FILE);
        Assert.assertTrue(fileToDelete.exists());
        when(mockFileVariable.getInnerFileObject()).thenReturn(fileToDelete);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);

        deleteCommand.evaluate(mockProgram);

        assertFalse(fileToDelete.exists());
    }

    @Test
    public void deleteFolderTest() throws Exception {
        File folderToDelete = new File(PATH_TO_TEST_DIR);
        Assert.assertTrue(folderToDelete.exists());
        when(mockFileVariable.getInnerFileObject()).thenReturn(folderToDelete);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);

        deleteCommand.evaluate(mockProgram);

        assertFalse(folderToDelete.exists());
    }

    @Test
    public void deleteNonExistentFileTest() throws Exception {
        File fileToDelete = new File("SOME RANDOM PATH");
        when(mockFileVariable.getInnerFileObject()).thenReturn(fileToDelete);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);
        try {
            deleteCommand.evaluate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof FMLExecutionException);
        }
    }

    @Test
    public void validateNullTargetTest() {
        deleteCommand = new Delete(null);
        try {
            deleteCommand.validate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFMLException && e.getMessage().equals("Invalid Command structure: Target identifier is null"));
        }
    }

    @Test
    public void undeclaredTargetIdentifierTest() {
        when(mockProgram.identifierIsDeclared(any())).thenReturn(false);
        try {
            deleteCommand.validate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFMLException && e.getMessage().equals("Invalid Command Structure: Target is not declared"));
        }
    }


    @AfterEach
    public void cleanUp() throws Exception {
        File file = new File(PATH_TO_TEST_DIR);
        if (file.exists()) {
            TestUtils.deleteFileWithPath(PATH_TO_TEST_DIR);
        }
    }
}
