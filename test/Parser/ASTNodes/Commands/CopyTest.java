package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.ActionStatement;
import Parser.ASTNodes.Statements.Statement;
import Parser.ASTNodes.Variables.FileVariable;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static TestUtils.TestUtils.copyTestFiles;
import static TestUtils.TestUtils.deleteTestFilesCopy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CopyTest {

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
    void testCopy() throws FMLExecutionException, InvalidFMLException, IOException {
        String targetIdentifier = "target";
        String destinationIdentifier = "destination";
        File targetFile = new File(testFiles, "aTestFile.txt");
        File destFolder = FileUtils.getFile(testFiles, "anEmptyFolder");

        Program program = new Program(null);
        program.addIdentifierDeclaration(targetIdentifier);
        program.addIdentifierDeclaration(destinationIdentifier);
        program.addVariable(targetIdentifier, new FileVariable(targetFile.getAbsolutePath()));
        program.addVariable(destinationIdentifier, new FileVariable(destFolder.getAbsolutePath()));

        Copy copy = new Copy(targetIdentifier, destinationIdentifier);
        copy.validate(program);
        copy.evaluate(program);
        assertTrue(FileUtils.directoryContains(testFiles, new File(testFiles, "aTestFile.txt")));
        assertTrue(FileUtils.directoryContains(destFolder, new File(destFolder, "aTestFile.txt")));
    }
}