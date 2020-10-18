package Parser.ASTNodes.Conditions.Booleans;

import Exceptions.FMLExecutionException;
import Language.FMLGrammar;
import Parser.ASTNodes.Conditions.Terms.*;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

public class ComparisonTest {

    private static Program mockProgram;
    private static FileVariable mockFileVariable;

    @BeforeAll
    public static void setUp() {
        mockProgram = Mockito.mock(Program.class);
        mockFileVariable = Mockito.mock(FileVariable.class);
    }

    @Test
    public void booleanEqualsComparison() throws FMLExecutionException {
        BooleanTerm left = new BooleanTerm(new True());
        BooleanTerm right = new BooleanTerm(new True());
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("==");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new BooleanTerm(new False());
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void booleanNotEqualsFalseComparison() throws FMLExecutionException {
        BooleanTerm left = new BooleanTerm(new False());
        BooleanTerm right = new BooleanTerm(new True());
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("!=");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new BooleanTerm(new True());
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void booleanUnsupportedComparison() throws FMLExecutionException {
        BooleanTerm left = new BooleanTerm(new False());
        BooleanTerm right = new BooleanTerm(new True());
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString(">");

        Comparison comparison = new Comparison(left, right, operator);
        try {
            comparison.evaluate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void stringEqualsComparison() throws FMLExecutionException {
        StringTerm left = new StringTerm("TEST");
        StringTerm right = new StringTerm("TEST");
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("==");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new StringTerm("TSET");
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void stringNotEqualsComparison() throws FMLExecutionException {
        StringTerm left = new StringTerm("DERP");
        StringTerm right = new StringTerm("TEST");
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("!=");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new StringTerm("TEST");
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void stringUnsupportedComparison() throws FMLExecutionException {
        StringTerm left = new StringTerm("Hi");
        StringTerm right = new StringTerm("Hi");
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString(">");

        Comparison comparison = new Comparison(left, right, operator);
        try {
            comparison.evaluate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void numberEqualsComparison() throws FMLExecutionException {
        NumberTerm left = new NumberTerm(new BigDecimal(10));
        NumberTerm right = new NumberTerm(new BigDecimal(10));
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("==");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(20));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void numberNotEqualsComparison() throws FMLExecutionException {
        NumberTerm left = new NumberTerm(new BigDecimal(20));
        NumberTerm right = new NumberTerm(new BigDecimal(10));
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("!=");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(10));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void numberLessThanComparison() throws FMLExecutionException {
        NumberTerm left = new NumberTerm(new BigDecimal(5));
        NumberTerm right = new NumberTerm(new BigDecimal(10));
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("<");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(10));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());

        left = new NumberTerm(new BigDecimal(20));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void numberLessThanOrEqualComparison() throws FMLExecutionException {
        NumberTerm left = new NumberTerm(new BigDecimal(5));
        NumberTerm right = new NumberTerm(new BigDecimal(10));
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("<=");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(10));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(20));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void numberGreaterThanComparison() throws FMLExecutionException {
        NumberTerm left = new NumberTerm(new BigDecimal(20));
        NumberTerm right = new NumberTerm(new BigDecimal(10));
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString(">");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(10));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());

        left = new NumberTerm(new BigDecimal(5));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void numberGreaterThanOrEqualComparison() throws FMLExecutionException {
        NumberTerm left = new NumberTerm(new BigDecimal(20));
        NumberTerm right = new NumberTerm(new BigDecimal(10));
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString(">=");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(10));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(5));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());
    }

    @Test
    public void attributeNumberComparison() throws FMLExecutionException {
        Mockito.when(mockFileVariable.getTimeCreated()).thenReturn(new BigInteger("20"));
        Mockito.when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);
        Term left = new AttributeTerm("RANDOM IDENTIFIER", FMLGrammar.AttributeName.CREATED);
        Term right = new NumberTerm(new BigDecimal(10));
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString(">");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        operator = FMLGrammar.ComparisonOperator.fromString("<");
        right = new AttributeTerm("RANDOM IDENTIFIER", FMLGrammar.AttributeName.MODIFIED);
        Mockito.when(mockFileVariable.getTimeModified()).thenReturn(new BigInteger("5"));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertFalse(comparison.getResult());

        operator = FMLGrammar.ComparisonOperator.fromString("==");
        right = new AttributeTerm("RANDOM IDENTIFIER", FMLGrammar.AttributeName.SIZE);
        left = new NumberTerm(new BigDecimal(10));
        Mockito.when(mockFileVariable.getSize()).thenReturn(new BigInteger("10"));
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        operator = FMLGrammar.ComparisonOperator.fromString("<=");
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        operator = FMLGrammar.ComparisonOperator.fromString(">=");
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());
    }

    @Test
    public void attributeStringComparison() throws FMLExecutionException {
        Mockito.when(mockFileVariable.getExtension()).thenReturn(".zip");
        Mockito.when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);
        Term left = new AttributeTerm("RANDOM IDENTIFIER", FMLGrammar.AttributeName.EXTENSION);
        Term right = new StringTerm(".zip");
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("==");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        operator = FMLGrammar.ComparisonOperator.fromString("!=");
        right = new AttributeTerm("RANDOM IDENTIFIER", FMLGrammar.AttributeName.NAME);
        Mockito.when(mockFileVariable.getName()).thenReturn(".jar");
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(10));
        comparison = new Comparison(left, right, operator);
        try {
            comparison.evaluate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void attributeBooleanComparison() throws FMLExecutionException {
        Mockito.when(mockFileVariable.isDirectory()).thenReturn(true);
        Mockito.when(mockProgram.getFileVariable(any())).thenReturn(mockFileVariable);
        Term left = new AttributeTerm("RANDOM IDENTIFIER", FMLGrammar.AttributeName.IS_DIRECTORY);
        Term right = new BooleanTerm(new True());
        FMLGrammar.ComparisonOperator operator = FMLGrammar.ComparisonOperator.fromString("==");

        Comparison comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        operator = FMLGrammar.ComparisonOperator.fromString("!=");
        right = new AttributeTerm("RANDOM IDENTIFIER", FMLGrammar.AttributeName.IS_FILE);
        Mockito.when(mockFileVariable.isFile()).thenReturn(false);
        comparison = new Comparison(left, right, operator);
        comparison.evaluate(mockProgram);
        Assert.assertTrue(comparison.getResult());

        left = new NumberTerm(new BigDecimal(10));
        comparison = new Comparison(left, right, operator);
        try {
            comparison.evaluate(mockProgram);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }
}
