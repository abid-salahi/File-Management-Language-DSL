package Parser.ASTNodes.Conditions.Terms;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Language.FMLGrammar;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.Declarations.Paths.FMLPath;
import Parser.ASTNodes.Variables.FileVariable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTermTest {
    private static String validPathStr;
    private static String invalidPathStr;
    private static String validDirectoryPathStr;
    private static Program p;

    @BeforeAll
    static void setup(){
        p = new Program(new ArrayList<>());

        validPathStr = "test\\resources\\FileTestData\\KindleShakespare.mobi";
        FMLPath validPath = new FMLPath(validPathStr);

        invalidPathStr = "test\\resources\\FileTestData\\KindleShakespare.epub";
        FMLPath invalidPath = new FMLPath(invalidPathStr);

        validDirectoryPathStr = "test\\resources\\FileTestData\\Jokes";
        FMLPath validdirpath = new FMLPath(validDirectoryPathStr);

        try {
            validPath.evaluate(p);
            FileVariable validFileVar = new FileVariable(validPath.getAbsolutePath());

            invalidPath.evaluate(p);
            FileVariable invalidFileVar = new FileVariable(invalidPath.getAbsolutePath());

            validdirpath.evaluate(p);
            FileVariable validDirectoryVar = new FileVariable(validdirpath.getAbsolutePath());

            p.addIdentifierDeclaration(validPathStr);
            p.addIdentifierDeclaration(validDirectoryPathStr);
            p.addIdentifierDeclaration(invalidPathStr);

            p.addVariable(validPathStr, validFileVar);
            p.addVariable(invalidPathStr, invalidFileVar);
            p.addVariable(validDirectoryPathStr, validDirectoryVar);
        } catch (FMLExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetBooleanFile(){
        AttributeTerm parAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.PARENT);
        AttributeTerm isdirAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.IS_DIRECTORY);
        AttributeTerm isfileAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.IS_FILE);

        try {
            parAT.evaluate(p);
            isdirAT.evaluate(p);
            isfileAT.evaluate(p);
            assertFalse(isdirAT.getBooleanValue());
            UnsupportedOperationException boolEx = assertThrows(UnsupportedOperationException.class,
                    ()->parAT.getBooleanValue());

            assertEquals(String.format("Expected an attributeName of either IS_FILE " +
                    "or IS_DIRECTORY, received one of type: %s", FMLGrammar.AttributeName.PARENT), boolEx.getMessage());
            assertTrue(isfileAT.getBooleanValue());
        } catch (FMLExecutionException e) {
            fail();
        }
    }

    @Test
    void testGetBooleanDir(){
        AttributeTerm dirnameAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.NAME);
        AttributeTerm dirisdirAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.IS_DIRECTORY);
        AttributeTerm dirisfileAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.IS_FILE);

        try {
            dirnameAT.evaluate(p);
            dirisdirAT.evaluate(p);
            dirisfileAT.evaluate(p);
            UnsupportedOperationException dirboolEx = assertThrows(UnsupportedOperationException.class,
                    ()->dirnameAT.getBooleanValue());

            assertEquals(String.format("Expected an attributeName of either IS_FILE " +
                    "or IS_DIRECTORY, received one of type: %s", FMLGrammar.AttributeName.NAME), dirboolEx.getMessage());
            assertTrue(dirisdirAT.getBooleanValue());
            assertFalse(dirisfileAT.getBooleanValue());
        } catch (FMLExecutionException e) {
            fail();
        }
    }

    @Test
    void testGetStringFile(){
        AttributeTerm parAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.PARENT);
        AttributeTerm nameAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.NAME);
        AttributeTerm extAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.EXTENSION);

        try {
            parAT.evaluate(p);
            nameAT.evaluate(p);
            extAT.evaluate(p);
            UnsupportedOperationException stringEx = assertThrows(UnsupportedOperationException.class,
                    ()->parAT.getStringValue());

            assertEquals(String.format("Expected an attributeName of either NAME " +
                    "or NAME, received one of type: %s", FMLGrammar.AttributeName.PARENT), stringEx.getMessage());
            assertEquals(p.getFileVariable(validPathStr).getName(), nameAT.getStringValue());
            assertEquals(p.getFileVariable(validPathStr).getExtension(), extAT.getStringValue());
        } catch (FMLExecutionException e) {
            fail();
        }
    }
//
    @Test
    void testGetStringDir(){
        AttributeTerm dirparAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.PARENT);
        AttributeTerm dirnameAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.NAME);
        AttributeTerm dirextAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.EXTENSION);

        try {
            dirextAT.evaluate(p);
            dirnameAT.evaluate(p);
            dirextAT.evaluate(p);

            UnsupportedOperationException stringEx = assertThrows(UnsupportedOperationException.class,
                    ()->dirparAT.getStringValue());

            assertEquals(String.format("Expected an attributeName of either NAME " +
                    "or NAME, received one of type: %s", FMLGrammar.AttributeName.PARENT), stringEx.getMessage());
            assertEquals(p.getFileVariable(validDirectoryPathStr).getName(), dirnameAT.getStringValue());
            assertEquals("", dirextAT.getStringValue());
        } catch (FMLExecutionException e) {
            fail();
        }
    }

    @Test
    void testGetNumberFile(){
        AttributeTerm createAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.CREATED);
        AttributeTerm modAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.MODIFIED);
        AttributeTerm sizeAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.SIZE);
        AttributeTerm extAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.EXTENSION);

        try {
            createAT.evaluate(p);
            modAT.evaluate(p);
            sizeAT.evaluate(p);
            extAT.evaluate(p);
            UnsupportedOperationException numbeEx = assertThrows(UnsupportedOperationException.class,
                ()->extAT.getNumericValue());

            assertEquals(String.format("Expected an attributeName of either CREATED, " +
                    "MODIFIED or SIZE; received one of type: %s", FMLGrammar.AttributeName.EXTENSION), numbeEx.getMessage());
            assertEquals(new BigDecimal(p.getFileVariable(validPathStr).getTimeCreated()), createAT.getNumericValue());
            assertEquals(new BigDecimal(p.getFileVariable(validPathStr).getTimeModified()), modAT.getNumericValue());
            assertEquals(new BigDecimal(p.getFileVariable(validPathStr).getSize()), sizeAT.getNumericValue());
        } catch (FMLExecutionException e) {
            fail("Do not expect exception to be thrown for these cases");
        }
    }

    @Test
    void testGetNumberDir(){
        AttributeTerm createAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.CREATED);
        AttributeTerm modAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.MODIFIED);
        AttributeTerm sizeAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.SIZE);
        AttributeTerm extAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.EXTENSION);

        try {
            createAT.evaluate(p);
            modAT.evaluate(p);
            sizeAT.evaluate(p);
            extAT.evaluate(p);

            UnsupportedOperationException numbeEx = assertThrows(UnsupportedOperationException.class,
                ()->extAT.getNumericValue());

            assertEquals(String.format("Expected an attributeName of either CREATED, " +
                    "MODIFIED or SIZE; received one of type: %s", FMLGrammar.AttributeName.EXTENSION), numbeEx.getMessage());
            assertEquals(new BigDecimal(p.getFileVariable(validDirectoryPathStr).getTimeCreated()), createAT.getNumericValue());
            assertEquals(new BigDecimal(p.getFileVariable(validDirectoryPathStr).getTimeModified()), modAT.getNumericValue());
            assertEquals(new BigDecimal(p.getFileVariable(validDirectoryPathStr).getSize()), sizeAT.getNumericValue());
        } catch (FMLExecutionException e) {
            fail("Do not expect exception to be thrown for these cases");
        }
    }

    @Test
    void testGetInvalidNumberFile(){
        AttributeTerm createAT = new AttributeTerm(invalidPathStr, FMLGrammar.AttributeName.CREATED);
        AttributeTerm modAT = new AttributeTerm(invalidPathStr, FMLGrammar.AttributeName.MODIFIED);
        AttributeTerm sizeAT = new AttributeTerm(invalidPathStr, FMLGrammar.AttributeName.SIZE);
        AttributeTerm extAT = new AttributeTerm(invalidPathStr, FMLGrammar.AttributeName.EXTENSION);

        FMLExecutionException createEX = assertThrows(FMLExecutionException.class,
                ()->createAT.evaluate(p));
        FMLExecutionException modEx = assertThrows(FMLExecutionException.class,
                ()->modAT.evaluate(p));
        FMLExecutionException sizeEx = assertThrows(FMLExecutionException.class,
                ()->sizeAT.evaluate(p));

        try {
            extAT.evaluate(p);
            UnsupportedOperationException extEX = assertThrows(UnsupportedOperationException.class,
                    ()->extAT.getNumericValue());

            assertEquals(String.format("Expected an attributeName of either CREATED, " +
                    "MODIFIED or SIZE; received one of type: %s", FMLGrammar.AttributeName.EXTENSION), extEX.getMessage());

        } catch (FMLExecutionException e) {
            fail();
        }

    }

    @Test
    void testGetParentFile(){
        AttributeTerm parentAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.PARENT);
        AttributeTerm modAT = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.MODIFIED);


        try {
            parentAT.evaluate(p);
            modAT.evaluate(p);
            UnsupportedOperationException modEx = assertThrows(UnsupportedOperationException.class,
                    ()->modAT.getBooleanValue());
            assertEquals(p.getFileVariable(validPathStr).getParent(), parentAT.getParentValue());
        } catch (FMLExecutionException e) {
            fail("Do not expect exception to be thrown for these cases");
        }
    }

    @Test
    void testGetParentDir(){
        AttributeTerm parentAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.PARENT);
        AttributeTerm modAT = new AttributeTerm(validDirectoryPathStr, FMLGrammar.AttributeName.MODIFIED);

        try {
            parentAT.evaluate(p);
            modAT.evaluate(p);
            UnsupportedOperationException modEx = assertThrows(UnsupportedOperationException.class,
                    ()->modAT.getParentValue());

            assertEquals(String.format("Expected an attributeName of PARENT," +
                    " received one of type: %s", FMLGrammar.AttributeName.MODIFIED), modEx.getMessage());
            assertEquals(p.getFileVariable(validDirectoryPathStr).getParent(), parentAT.getParentValue());
        } catch (FMLExecutionException e) {
            fail("Do not expect exception to be thrown for these cases");
        }
    }

    @Test
    void testGetParentDirInvalidFile(){
        AttributeTerm parentAT = new AttributeTerm(invalidPathStr, FMLGrammar.AttributeName.PARENT);
        AttributeTerm modAT = new AttributeTerm(invalidPathStr, FMLGrammar.AttributeName.IS_DIRECTORY);

        try {
            parentAT.evaluate(p);
            modAT.evaluate(p);

            UnsupportedOperationException modEx = assertThrows(UnsupportedOperationException.class,
                    ()->modAT.getParentValue());

            assertEquals(String.format("Expected an attributeName of PARENT," +
                    " received one of type: %s", FMLGrammar.AttributeName.IS_DIRECTORY), modEx.getMessage());
            assertEquals(p.getFileVariable(invalidPathStr).getParent(), parentAT.getParentValue());
        } catch (FMLExecutionException e) {
            fail("Do not expect exception to be thrown for these cases");
        }
    }

    @Test
    void testValidate(){
        AttributeTerm nullFile = new AttributeTerm(null, FMLGrammar.AttributeName.PARENT);
        AttributeTerm nullName = new AttributeTerm(validPathStr, null);
        AttributeTerm notinProgram = new AttributeTerm("hello", FMLGrammar.AttributeName.PARENT);
        InvalidFMLException fileEx = assertThrows(InvalidFMLException.class,
                ()->nullFile.validate(p));
        InvalidFMLException nameEx = assertThrows(InvalidFMLException.class,
                ()->nullName.validate(p));
        InvalidFMLException notInEx = assertThrows(InvalidFMLException.class,
                ()->notinProgram.validate(p));

        assertEquals("File is null", fileEx.getMessage());
        assertEquals("Attribute is null", nameEx.getMessage());
        assertEquals("File variable identifier is not declared in program", notInEx.getMessage());

        AttributeTerm valid = new AttributeTerm(validPathStr, FMLGrammar.AttributeName.PARENT);
        try {
            valid.validate(p);
        } catch (InvalidFMLException e) {
            fail("Did not expect validation to fail");
        }
    }
}
