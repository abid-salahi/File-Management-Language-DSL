package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static TestUtils.TestUtils.copyTestFiles;
import static TestUtils.TestUtils.deleteTestFilesCopy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RenameTest {

    private File testFiles;

    @BeforeEach
    void setup() throws IOException {
        deleteTestFilesCopy();
        testFiles = copyTestFiles();
    }

    @AfterEach
    void teardown() throws IOException {
        deleteTestFilesCopy();
    }

    @Test
    void testRename() throws FMLExecutionException, InvalidFMLException, IOException {
        String identifier = "testFile";
        Program program = new Program(null);
        program.addIdentifierDeclaration(identifier);
        program.addVariable(identifier, new FileVariable(new File(testFiles, "aTestFile.txt").getAbsolutePath()));

        Rename rename = new Rename(identifier, "aRenamedTestFile.txt");
        rename.validate(program);
        rename.evaluate(program);
        assertTrue(FileUtils.directoryContains(testFiles, new File(testFiles, "aRenamedTestFile.txt")));
        assertFalse(FileUtils.directoryContains(testFiles, new File(testFiles, "aTestFile.txt")));
    }
}
