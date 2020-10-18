package Parser.ASTNodes.Conditions.Booleans;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Language.FMLGrammar.ComparisonOperator;
import Parser.ASTNodes.Conditions.Terms.*;
import Parser.ASTNodes.Program;

import java.math.BigDecimal;
import java.math.BigInteger;

import static Util.ObjectUtil.nullOrEqual;

public class Comparison extends FMLBoolean {

    private Term leftTerm;
    private Term rightTerm;
    private final ComparisonOperator operator;
    private Term leftTermOG;
    private Term rightTermOG;

    private boolean result;

    public Comparison(Term leftTerm, Term rightTerm, ComparisonOperator operator) {
        this.leftTerm = this.leftTermOG = leftTerm;
        this.operator = operator;
        this.rightTerm = this.rightTermOG = rightTerm;
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        leftTerm.evaluate(program);
        rightTerm.evaluate(program);
        if (leftTerm instanceof BooleanTerm && rightTerm instanceof BooleanTerm) {
            if (operator.toString().equals("==")) {
                result = leftTerm.getBooleanValue() == rightTerm.getBooleanValue();
            } else if (operator.toString().equals("!=")) {
                result = leftTerm.getBooleanValue() != rightTerm.getBooleanValue();
            } else {
                throw new UnsupportedOperationException(
                        String.format("Expected an == or != comparison operators but received %s", operator.toString()));
            }
        } else if (leftTerm instanceof StringTerm && rightTerm instanceof StringTerm) {
            if (operator.toString().equals("==")) {
                result = leftTerm.getStringValue().equals(rightTerm.getStringValue());
            } else if (operator.toString().equals("!=")) {
                result = !leftTerm.getStringValue().equals(rightTerm.getStringValue());
            } else {
                throw new UnsupportedOperationException(
                        String.format("Expected an == or != comparison operators but received %s", operator.toString()));
            }
        } else if (leftTerm instanceof NumberTerm && rightTerm instanceof NumberTerm) {
            if (operator.toString().equals("==")) {
                result = leftTerm.getNumericValue().equals(rightTerm.getNumericValue());
            } else if (operator.toString().equals("!=")) {
                result = !leftTerm.getNumericValue().equals(rightTerm.getNumericValue());
            } else if (operator.toString().equals("<")) {
                result = leftTerm.getNumericValue().compareTo(rightTerm.getNumericValue()) < 0;
            } else if (operator.toString().equals("<=")) {
                result = leftTerm.getNumericValue().compareTo(rightTerm.getNumericValue()) <= 0;
            } else if (operator.toString().equals(">")) {
                result = leftTerm.getNumericValue().compareTo(rightTerm.getNumericValue()) > 0;
            } else if (operator.toString().equals(">=")) {
                result = leftTerm.getNumericValue().compareTo(rightTerm.getNumericValue()) >= 0;
            } else {
                throw new UnsupportedOperationException(
                        String.format("Expected an ==, !=, <, <=, >, >= comparison operators but received %s", operator.toString()));
            }
        } else if (leftTerm instanceof AttributeTerm || rightTerm instanceof AttributeTerm) {
            Object resultLeft = leftTerm instanceof AttributeTerm ? ((AttributeTerm) leftTerm).getResult() : null;
            Object resultRight = rightTerm instanceof AttributeTerm ? ((AttributeTerm) rightTerm).getResult() : null;

            if (resultLeft != null) {
                if (resultLeft instanceof String) {
                    leftTerm = new StringTerm((String) resultLeft);
                } else if (resultLeft instanceof Boolean) {
                    if ((boolean) resultLeft) {
                        leftTerm = new BooleanTerm(new True());
                    } else {
                        leftTerm = new BooleanTerm(new False());
                    }
                } else if (resultLeft instanceof BigInteger) {
                    leftTerm = new NumberTerm(new BigDecimal((BigInteger) resultLeft));
                } else {
                    throw new UnsupportedOperationException(
                            String.format("Expected AttributeTerm to have result of type Boolean, BigInteger, or String but received %s", operator.toString()));
                }
            }

            if (resultRight != null) {
                if (resultRight instanceof String) {
                    rightTerm = new StringTerm((String) resultRight);
                } else if (resultRight instanceof Boolean) {
                    if ((boolean) resultRight) {
                        rightTerm = new BooleanTerm(new True());
                    } else {
                        rightTerm = new BooleanTerm(new False());
                    }
                } else if (resultRight instanceof BigInteger) {
                    rightTerm = new NumberTerm(new BigDecimal((BigInteger) resultRight));
                } else {
                    throw new UnsupportedOperationException(
                            String.format("Expected AttributeTerm to have result of type Boolean, BigInteger, or String but received %s", operator.toString()));
                }
            }

            if (leftTerm != null && rightTerm != null) {
                this.evaluate(program);
            }
        } else {
            throw new UnsupportedOperationException(
                    String.format("Comparison between values of type %s and %s is not supported.", leftTerm.getClass(), rightTerm.getClass()));
        }
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (leftTerm == null || rightTerm == null || operator == null) {
            throw new InvalidFMLException("Invalid Command structure: leftTerm, rightTerm and Comparator must not be null");
        }
    }

    @Override
    public boolean getResult() {
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Comparison)) {
            return false;
        }
        Comparison other = (Comparison) obj;
        return nullOrEqual(this.leftTerm, other.leftTerm)
                && nullOrEqual(this.rightTerm, other.rightTerm)
                && nullOrEqual(this.operator, other.operator);
    }

    @Override
    public void reset() throws FMLExecutionException {
        this.rightTerm = this.rightTermOG;
        this.leftTerm = this.leftTermOG;
        this.rightTerm.reset();
        this.leftTerm.reset();
    }
}
