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
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompressTest {
    private static Compress compressCommand;
    private static Program mockProgram;
    private static FileVariable mockFileVariable;

    private final static String TEST_VAR_NAME = "testVar";
    private final static String PATH_TO_TEST_DIR = "test/TestFolder";
    private final static String PATH_TO_FILE = PATH_TO_TEST_DIR+"/testFile.txt";
    private final static String PATH_TO_TEST_SUB_DIR = "test/TestFolder/SubFolder";

    @BeforeAll
    public static void setUp() throws Exception {
        mockProgram = mock(Program.class);
        mockFileVariable = mock(FileVariable.class);
        // Set up Delete
    }

    @BeforeEach
    public void prepareTestEnv() throws Exception {
        compressCommand = new Compress(TEST_VAR_NAME);
        // Create Test Directory
        TestUtils.createFolderAtPath(PATH_TO_TEST_DIR);
        // Create file to delete
        TestUtils.createFileWithPath(PATH_TO_FILE);

        // Create subfolders and files within them
        TestUtils.createFolderAtPath(PATH_TO_TEST_DIR+"/SubFolder");
        TestUtils.createFileWithPath(PATH_TO_TEST_DIR+"/SubFolder/testSubFile.txt");
        TestUtils.createFileWithPath(PATH_TO_TEST_DIR+"/SubFolder/testSubFile.html");
        TestUtils.createFileWithPath(PATH_TO_TEST_DIR+"/SubFolder/testSubFile.mp4");
    }

    @Test
    public void compressFileTest() throws Exception {
        File fileToCompress = new File(PATH_TO_FILE);
        Assert.assertTrue(fileToCompress.exists());
        when(mockFileVariable.getInnerFileObject()).thenReturn(fileToCompress);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);
        compressCommand.evaluate(mockProgram);

        String compressedFileName = fileToCompress.getName().concat(".zip");
        String compressedFilePath = Paths.get(fileToCompress.getParentFile().getAbsolutePath(), compressedFileName).toString();
        Assert.assertTrue(new File(compressedFilePath).exists());
    }

    @Test
    public void compressFolderTest() throws Exception {
        File fileToCompress = new File(PATH_TO_TEST_SUB_DIR);
        Assert.assertTrue(fileToCompress.exists());
        when(mockFileVariable.getInnerFileObject()).thenReturn(fileToCompress);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);
        compressCommand.evaluate(mockProgram);

        String compressedFileName = fileToCompress.getName().concat(".zip");
        String compressedFilePath = Paths.get(fileToCompress.getParentFile().getAbsolutePath(), compressedFileName).toString();
        Assert.assertTrue(new File(compressedFilePath).exists());
    }

    @Test
    public void compressNonExistentFileTest() throws Exception {
        File fileToDelete = new File("SOME RANDOM PATH");
        when(mockFileVariable.getInnerFileObject()).thenReturn(fileToDelete);
        when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);
        try {
            compressCommand.evaluate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof FMLExecutionException);
        }
    }

    @Test
    public void validateNullTargetTest() {
        compressCommand = new Compress(null);
        try {
            compressCommand.validate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFMLException && e.getMessage().equals("Invalid Command structure: Target identifier is null"));
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
