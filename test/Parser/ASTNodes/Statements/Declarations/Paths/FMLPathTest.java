package Parser.ASTNodes.Statements.Declarations.Paths;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FMLPathTest {

    private Program program;
    private String C_DRIVE_PATH = "C:\\";
    private String NON_EXISTENT_DRIVE_ABS_PATH = "C1231:\\";
    private String USERS_DIR_NAME = "Users";

    private String USERS_DIR_ABS_PATH = "C:\\Users";

    private String RANDOM_FILE_PATH = new File("test/resources/ARandomFile.txt").getAbsolutePath();
    private String TEST_IDENTIFIER = "identifier";

    @BeforeEach
    void setup() {
        program = new Program(new ArrayList<>());
    }

    /* Valid input tests */

    @Test
    void testAbsolutePathConstructor() throws FMLExecutionException, InvalidFMLException {
        FMLPath fmlPath = new FMLPath(C_DRIVE_PATH);
        fmlPath.validate(program);
        fmlPath.evaluate(program);
        assertEquals(C_DRIVE_PATH, fmlPath.getAbsolutePath());
    }

    @Test
    void testRelativePathConstructor() throws FMLExecutionException, InvalidFMLException {
        addFileVariableToProgram(TEST_IDENTIFIER, C_DRIVE_PATH);
        FMLPath fmlPath = new FMLPath(TEST_IDENTIFIER, USERS_DIR_NAME);
        fmlPath.validate(program);
        fmlPath.evaluate(program);
        assertEquals(USERS_DIR_ABS_PATH, fmlPath.getAbsolutePath());
    }

    /* Invalid input tests */

    @Test
    void testThrowsErrorIfRootVariableNotDeclared() {
        assertThrows(InvalidFMLException.class, () -> {
            FMLPath fmlPath = new FMLPath(TEST_IDENTIFIER, USERS_DIR_NAME);
            fmlPath.validate(program);
            fmlPath.evaluate(program);
            fmlPath.getAbsolutePath();
        });
    }

    @Test
    void testThrowsErrorIfRootVariableDoesNotExist() {
        assertThrows(FMLExecutionException.class, () -> {
            addFileVariableToProgram(TEST_IDENTIFIER, NON_EXISTENT_DRIVE_ABS_PATH);
            FMLPath fmlPath = new FMLPath(TEST_IDENTIFIER, USERS_DIR_NAME);
            fmlPath.validate(program);
            fmlPath.evaluate(program);
            fmlPath.getAbsolutePath();
        });
    }

    @Test
    void testThrowsErrorForNonDirectoryRootVariable() {
        assertThrows(FMLExecutionException.class, () -> {
            addFileVariableToProgram(TEST_IDENTIFIER, RANDOM_FILE_PATH);
            FMLPath fmlPath = new FMLPath(TEST_IDENTIFIER, USERS_DIR_NAME);
            fmlPath.validate(program);
            fmlPath.evaluate(program);
            fmlPath.getAbsolutePath();
        });
    }

    private void addFileVariableToProgram(String identifier, String path) throws InvalidFMLException, FMLExecutionException {
        FMLPath rootFMLPath = new FMLPath(path);
        rootFMLPath.validate(program);
        rootFMLPath.evaluate(program);
        FileVariable fileVariable = new FileVariable(rootFMLPath.getAbsolutePath());
        program.addIdentifierDeclaration(identifier);
        program.addVariable(identifier, fileVariable);
    }
}
