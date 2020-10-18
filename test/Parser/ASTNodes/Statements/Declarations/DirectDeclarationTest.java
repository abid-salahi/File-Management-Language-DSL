package Parser.ASTNodes.Statements.Declarations;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.Declarations.Paths.FMLPath;
import Parser.ASTNodes.Variables.FileVariable;
import Parser.ASTNodes.Variables.ListVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DirectDeclarationTest {

    // TODO: add tests for invalid variable names

    Program program;
    String fileIdentifier = "folder1";
    String listIdentifier = "folders[]";
    FMLPath regularFMLPath;
    FMLPath wildcardFMLPath;

    @BeforeEach
    void setup() {
        program = new Program(new ArrayList<>());
        regularFMLPath = new FMLPath("test/resources/FileTestData");
        wildcardFMLPath = new FMLPath("test/resources/FileTestData/*");
    }

    @Nested
    class FileVariableDeclarations {

        @Test
        void testAddsIdentifierToProgramOnValidate() throws InvalidFMLException {
            DirectDeclaration dd = new DirectDeclaration(fileIdentifier, regularFMLPath);
            dd.validate(program);
            assertTrue(program.identifierIsDeclared(fileIdentifier));
        }

        @Test
        void testAddsFileVariableToProgramOnEvaluate() throws FMLExecutionException, InvalidFMLException {
            DirectDeclaration dd = new DirectDeclaration(fileIdentifier, regularFMLPath);
            dd.validate(program);
            dd.evaluate(program);
            assertTrue(program.identifierIsDeclared(fileIdentifier));
            FileVariable fv = program.getFileVariable(fileIdentifier);
            assertNotNull(fv);
        }

        @Test
        void testThrowsErrorIfPathHasWildcard() {
            assertThrows(InvalidFMLException.class, () -> {
                DirectDeclaration dd = new DirectDeclaration(fileIdentifier, wildcardFMLPath);
                dd.validate(program);
            });
        }

    }

    @Nested
    class ListVariableDeclarations {

        @Test
        void testAddsIdentifierToProgramOnValidate() throws InvalidFMLException {
            DirectDeclaration dd = new DirectDeclaration(listIdentifier, wildcardFMLPath);
            dd.validate(program);
            assertTrue(program.identifierIsDeclared(listIdentifier));
        }

        @Test
        void testAddsListVariableToProgramOnEvaluate() throws InvalidFMLException, FMLExecutionException {
            DirectDeclaration dd = new DirectDeclaration(listIdentifier, wildcardFMLPath);
            dd.validate(program);
            dd.evaluate(program);
            assertTrue(program.identifierIsDeclared(listIdentifier));
            ListVariable fv = program.getListVariable(listIdentifier);
            assertNotNull(fv);
        }

        @Test
        void doesNotThrowErrorIfPathHasNoWildcard() throws InvalidFMLException {
            DirectDeclaration dd = new DirectDeclaration(listIdentifier, regularFMLPath);
            dd.validate(program);
            assertTrue(program.identifierIsDeclared(listIdentifier));
        }
    }
}
