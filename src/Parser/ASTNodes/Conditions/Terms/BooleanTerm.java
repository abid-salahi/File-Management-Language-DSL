package Parser.ASTNodes.Conditions.Terms;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Conditions.Booleans.FMLBoolean;
import Parser.ASTNodes.Program;

import static Util.ObjectUtil.nullOrEqual;

/**
 * This is a wrapper for FMLBoolean
 */
public class BooleanTerm extends Term {

    private FMLBoolean value;

    public BooleanTerm(FMLBoolean value) {
        this.value = value;
    }

    @Override
    public boolean getBooleanValue() throws FMLExecutionException {
        return value.getResult();
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (value == null) {
            throw new InvalidFMLException("Boolean Term is null");
        }
        value.validate(program);
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        value.evaluate(program);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanTerm)) {
            return false;
        }
        BooleanTerm other = (BooleanTerm) obj;
        return nullOrEqual(this.value, other.value);
    }

    @Override
    public void reset() throws FMLExecutionException {
        value.reset();
    }
}
