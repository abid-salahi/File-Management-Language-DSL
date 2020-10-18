package Parser.ASTNodes.Commands;

import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import TestUtils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateTest {

    private static Create createCommand;

    private static Program mockProgram;
    private static FileVariable mockFileVariable;

    private final static String TEST_VAR_NAME = "testVar";
    private final static String PATH_TO_TEST_DIR = "test/TestFolder";
    private final static String PATH_TO_FILE = PATH_TO_TEST_DIR+"/testFile.txt";
    private final static String PATH_TO_SUBFOLDER = PATH_TO_TEST_DIR+"/Subfolder";

    @BeforeAll
    public static void setUp() throws Exception {
        mockProgram = mock(Program.class);
        mockFileVariable = mock(FileVariable.class);
    }

    @BeforeEach
    public void prepareTestEnv() throws Exception {
        createCommand = new Create(TEST_VAR_NAME);
        // Create Test Directory
        TestUtils.createFolderAtPath(PATH_TO_TEST_DIR);
        // Create file to delete
        TestUtils.createFileWithPath(PATH_TO_FILE);
    }

    @Test
    public void createFolderTest() throws Exception {
        File fileToCreate = new File(PATH_TO_SUBFOLDER);
        assertFalse(fileToCreate.exists());
        when(mockFileVariable.getInnerFileObject()).thenReturn(fileToCreate);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);

        createCommand.evaluate(mockProgram);

        assertTrue(fileToCreate.exists());
    }

    @Test
    public void createExistingFolderTest() throws Exception {
        File fileToCreate = new File(PATH_TO_SUBFOLDER);
        TestUtils.createFolderAtPath(fileToCreate.getPath());
        assertTrue(fileToCreate.exists());
        when(mockFileVariable.getInnerFileObject()).thenReturn(fileToCreate);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);

        createCommand.evaluate(mockProgram);

        assertTrue(fileToCreate.exists());
    }

    @Test
    public void validateNullTargetTest() {
        createCommand = new Create(null);
        try {
            createCommand.validate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFMLException && e.getMessage().equals("Invalid Command structure: Target identifier is null"));
        }
    }

    @Test
    public void undeclaredTargetIdentifierTest() {
        when(mockProgram.identifierIsDeclared(any())).thenReturn(false);
        try {
            createCommand.validate(mockProgram);
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
