package Parser.ASTNodes.Statements.Declarations;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import Parser.ASTNodes.Variables.ListVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReferenceDeclarationTest {

    // TODO: add tests for invalid variable names

    private Program program;
    private FileVariable fileVar;
    private ListVariable listVar;

    private String FILE_IDENTIFIER_1 = "file1";
    private String FILE_IDENTIFIER_2 = "file2";
    private String LIST_IDENTIFIER_1 = "list1[]";
    private String LIST_IDENTIFIER_2 = "list2[]";

    @BeforeEach
    void setup() throws FMLExecutionException {
        program = new Program(new ArrayList<>());
        fileVar = new FileVariable(".");
        listVar = new ListVariable(".");
    }

    @Test
    void testAddsFileIdentifierToProgramOnValidate() throws InvalidFMLException {
        program.addIdentifierDeclaration(FILE_IDENTIFIER_1);
        ReferenceDeclaration rd = new ReferenceDeclaration(FILE_IDENTIFIER_2, FILE_IDENTIFIER_1);
        rd.validate(program);

        // Both identifiers should still be declared
        assertTrue(program.identifierIsDeclared(FILE_IDENTIFIER_2));
        assertTrue(program.identifierIsDeclared(FILE_IDENTIFIER_1));
    }

    @Test
    void testAddsListIdentifierToProgramOnValidate() throws InvalidFMLException {
        program.addIdentifierDeclaration(LIST_IDENTIFIER_1);
        ReferenceDeclaration rd = new ReferenceDeclaration(LIST_IDENTIFIER_2, LIST_IDENTIFIER_1);
        rd.validate(program);

        // Both identifiers should still be declared
        assertTrue(program.identifierIsDeclared(LIST_IDENTIFIER_2));
        assertTrue(program.identifierIsDeclared(LIST_IDENTIFIER_1));
    }

    @Test
    void testAddsFileVariableToProgramOnEvaluate() throws InvalidFMLException, FMLExecutionException {
        program.addIdentifierDeclaration(FILE_IDENTIFIER_1);
        program.addVariable(FILE_IDENTIFIER_1, fileVar);

        ReferenceDeclaration rd = new ReferenceDeclaration(FILE_IDENTIFIER_2, FILE_IDENTIFIER_1);
        rd.validate(program);
        rd.evaluate(program);

        // Both identifiers should still be declared
        assertTrue(program.identifierIsDeclared(FILE_IDENTIFIER_2));
        assertTrue(program.identifierIsDeclared(FILE_IDENTIFIER_1));
        // Both identifiers should have the same filevar object
        assertEquals(program.getFileVariable(FILE_IDENTIFIER_1), program.getFileVariable(FILE_IDENTIFIER_2));
    }

    @Test
    void testAddsListVariableToProgramOnEvaluate() throws InvalidFMLException, FMLExecutionException {
        program.addIdentifierDeclaration(LIST_IDENTIFIER_1);
        program.addVariable(LIST_IDENTIFIER_1, listVar);

        ReferenceDeclaration rd = new ReferenceDeclaration(LIST_IDENTIFIER_2, LIST_IDENTIFIER_1);
        rd.validate(program);
        rd.evaluate(program);

        // Both identifiers should still be declared
        assertTrue(program.identifierIsDeclared(LIST_IDENTIFIER_2));
        assertTrue(program.identifierIsDeclared(LIST_IDENTIFIER_1));
        // Both identifiers should have the same filevar object
        assertEquals(program.getListVariable(LIST_IDENTIFIER_1), program.getListVariable(LIST_IDENTIFIER_2));
    }

    @Test
    void testThrowsErrorIfRightIdentifierNotDeclared() {
        assertThrows(InvalidFMLException.class, () -> {
            ReferenceDeclaration rd = new ReferenceDeclaration(FILE_IDENTIFIER_2, FILE_IDENTIFIER_1);
            rd.validate(program);
        });
    }

    @Test
    void testThrowsErrorIfEquatingListVariableToFileVariable() {
        assertThrows(InvalidFMLException.class, () -> {
            program.addIdentifierDeclaration(LIST_IDENTIFIER_1);
            program.addVariable(LIST_IDENTIFIER_1, listVar);

            ReferenceDeclaration rd = new ReferenceDeclaration(FILE_IDENTIFIER_1, LIST_IDENTIFIER_1);
            rd.validate(program);
        });
    }

    @Test
    void testThrowsErrorIfEquatingFileVariableToListVariable() {
        assertThrows(InvalidFMLException.class, () -> {
            program.addIdentifierDeclaration(FILE_IDENTIFIER_1);
            program.addVariable(FILE_IDENTIFIER_1, fileVar);

            ReferenceDeclaration rd = new ReferenceDeclaration(LIST_IDENTIFIER_1, FILE_IDENTIFIER_1);
            rd.validate(program);
        });
    }
}
